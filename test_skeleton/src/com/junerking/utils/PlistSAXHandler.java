package com.junerking.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PlistSAXHandler extends DefaultHandler {

	private static final boolean DEBUG = true;

	private boolean isRootElement = false;

	private boolean keyElementBegin = false;

	private String key;

	Stack<Object> stack = new Stack<Object>();

	private boolean valueElementBegin = false;

	private Object root;

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getMapResult() {
		return (HashMap<String, Object>) root;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getArrayResult() {
		return (List<Object>) root;
	}

	@Override
	public void startDocument() throws SAXException {
		if (DEBUG)
			System.out.println("开始解析");
	}

	@Override
	public void endDocument() throws SAXException {
		if (DEBUG)
			System.out.println("结束解析");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (DEBUG)
			System.out.println("uri: " + uri + "   localName:" + localName + "    startElement:" + qName);

		if ("plist".equals(qName)) {
			isRootElement = true;
			return;
		}

		if ("dict".equals(qName)) {
			if (isRootElement) {
				stack.push(new HashMap<String, Object>());// 压栈
				isRootElement = !isRootElement;
			} else {
				Object object = stack.peek();
				HashMap<String, Object> dict = new HashMap<String, Object>();
				if (object instanceof ArrayList)
					((ArrayList<Object>) object).add(dict);
				else if (object instanceof HashMap)
					((HashMap<String, Object>) object).put(key, dict);
				stack.push(dict);
			}
			return;
		}

		if ("key".equals(qName)) {
			keyElementBegin = true;
			return;
		}

		if ("true".equals(qName)) {
			HashMap<String, Object> parent = (HashMap<String, Object>) stack.peek();
			parent.put(key, true);
			return;
		}

		if ("false".equals(qName)) {
			HashMap<String, Object> parent = (HashMap<String, Object>) stack.peek();
			parent.put(key, false);
			return;
		}

		if ("array".equals(qName)) {
			if (isRootElement) {
				ArrayList<Object> obj = new ArrayList<Object>();
				stack.push(obj);
				isRootElement = !isRootElement;
			} else {
				HashMap<String, Object> parent = (HashMap<String, Object>) stack.peek();
				ArrayList<Object> obj = new ArrayList<Object>();
				stack.push(obj);
				parent.put(key, obj);
			}
			return;
		}

		if ("string".equals(qName) || "integer".equals(qName) || "real".equals(qName)) {
			valueElementBegin = true;
			return;
		}
	}

	/*
	 * 字符串解析(non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (DEBUG) {
			String value = new String(ch, start, length);
			System.out.println("characters: " + value);
		}

		if (length > 0) {
			if (keyElementBegin) {
				key = new String(ch, start, length);
				if (DEBUG)
					System.out.println("key:" + key);
			}

			if (valueElementBegin) {
				if (HashMap.class.equals(stack.peek().getClass())) {
					HashMap<String, Object> parent = (HashMap<String, Object>) stack.peek();
					String value = new String(ch, start, length);
					parent.put(key, value);

				} else if (ArrayList.class.equals(stack.peek().getClass())) {
					ArrayList<Object> parent = (ArrayList<Object>) stack.peek();
					String value = new String(ch, start, length);
					parent.add(value);
				}
				if (DEBUG)
					System.out.println("value:" + new String(ch, start, length));
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("plist".equals(qName)) {
		}
		if ("key".equals(qName)) {
			keyElementBegin = false;
		}
		if ("string".equals(qName) || "integer".equals(qName) || "real".equals(qName)) {
			valueElementBegin = false;
		}
		if ("array".equals(qName)) {
			root = stack.pop();
		}
		if ("dict".equals(qName)) {
			root = stack.pop();
		}
		if (DEBUG)
			System.out.println();
	}
}