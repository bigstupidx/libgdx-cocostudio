package com.junerking.skeleton.sdp;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;

public class IAnimation {

	private ArrayList<NodeData> action_list;
	private int total_frame, rate;

	public static final class FrameData {
		int frame_index;
		float x, y, scale_x, scale_y, skew_x, skew_y, rot;
		float r, g, b, a;
		boolean visible;
	}

	public static final class NodeData {
		int id;
		ArrayList<FrameData> frame_list = new ArrayList<FrameData>();

		public void addFrameData(FrameData data) {
			frame_list.add(data);
		}
	}

	//===================================================================================
	//----------------------parse-----------------------

	public static XAnimation createAnimation(String file_name) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			System.out.print("" + Gdx.files.internal(""));
			Document doc = db.parse(Gdx.files.internal(file_name).file());
			doc.normalize();
			return getCharData(doc, file_name.substring(0, file_name.lastIndexOf('/') + 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XAnimation getCharData(Document doc, String file_name_pre) throws ParserConfigurationException, SAXException {
		XAnimation data = null;

		NodeList headers = doc.getElementsByTagName("XAnimation");
		data = new XAnimation(getIntValue((Element) (headers.item(0)), "frame_count", 1));

		HashMap<Integer, Vector2> component_list = new HashMap<Integer, Vector2>();
		//components
		{
			NodeList _components = doc.getElementsByTagName("components");
			Element components = (Element) _components.item(0);
			String texture_atlas_name = components.getAttribute("texture_atlas");
			TextureAtlas texture_atlas = new TextureAtlas(file_name_pre + texture_atlas_name);
			NodeList nodes = components.getElementsByTagName("component");
			for (int i = 0, n = nodes.getLength(); i < n; i++) {
				Element element = (Element) (nodes.item(i));
				int component_id = getIntValue(element, "type", -1);
				int w = getIntValue(element, "w", 0);
				int h = getIntValue(element, "h", 0);
				Vector2 vc = new Vector2(w, h);
				component_list.put(component_id, vc);
				String component_name = element.getAttribute("name");
				AtlasRegion region = texture_atlas.findRegion(component_name);
				if (region != null) {
					data.addAtlasComponent(component_id, region);
				}
			}
		}
		return data;
	}

	public static int getIntValue(Element element, String key, int defaultValue) {
		if (element.hasAttribute(key)) {
			return Integer.parseInt(element.getAttribute(key));
		}
		return defaultValue;
	}

	public static float getFloatValue(Element element, String key, float defaultValue) {
		if (element.hasAttribute(key)) {
			return Float.parseFloat(element.getAttribute(key));
		}
		return defaultValue;
	}

	public static boolean getBooleanValue(Element element, String key, boolean defaultValue) {
		if (element.hasAttribute(key)) {
			return Boolean.parseBoolean(element.getAttribute(key));
		}
		return defaultValue;
	}

	public static String getStringValue(Element element, String key) {
		return element.getAttribute(key);
	}
}
