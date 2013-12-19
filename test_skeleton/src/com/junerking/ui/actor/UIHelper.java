package com.junerking.ui.actor;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class UIHelper {
	private static HashMap<String, UIWidget> ui_map = new HashMap<String, UIWidget>();

	public static UIWidget createUIWidget(String name) {
		UIWidget result = ui_map.get(name);
		if (result == null) {
			result = parseUI(name);
		}
		return result;
	}

	private static UIWidget parseUI(String file_name) {
		String suffix = (String) file_name.subSequence(file_name.lastIndexOf("."), file_name.length());
		System.out.println("++++filename:" + file_name + "  suffix:" + suffix);
		if (suffix != null && (suffix.equals(".json") || suffix.equals(".ExportJson"))) {
			return parseFromJSONFile(file_name);
		}
		return null;
	}

	private static UIWidget parseFromJSONFile(String file_name) {
		try {
			JSONParser parser = new JSONParser();
			FileHandle file_handler = Gdx.files.internal(file_name);
			JSONObject object = (JSONObject) parser.parse(new InputStreamReader(file_handler.read()));

			int version = getVersion((String) object.get(VERSION));

			JSONObject widget_tree = (JSONObject) object.get(WIDGET_TREE);
			return parseFromWidget(widget_tree);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static UIWidget parseFromWidget(JSONObject data) {
		String class_name = (String) data.get(CLASS_NAME);
		JSONObject options = (JSONObject) data.get(OPTIONS);
		if (class_name == null) {
			return null;
		}

		UIWidgetGroup result = null;

		if (class_name.equals("Panel")) {
			UIPanel panel = new UIPanel();
			setPropsForPanel(panel, options);
			result = panel;

		} else if (class_name.equals("Button")) {
			UIButton button = new UIButton();
			setPropsForButton(button, options);
			result = button;

		} else if (class_name.equals("ImageView")) {
			UIImage widget = new UIImage();
			setPropsForImageView(widget, options);
			result = widget;

		} else if (class_name.equals("LabelBMFont")) {
			UILabelBMFont widget = new UILabelBMFont();
			setPropsForLabelBMFont(widget, options);
			widget.setTouchable(Touchable.disabled);
			result = widget;

		} else if (class_name.equals("CheckBox")) {
			UICheckBox widget = new UICheckBox();
			setPropsForCheckBox(widget, options);
			result = widget;

		} else if (class_name.equals("LoadingBar")) {
			UIProgressBar widget = new UIProgressBar();
			setPropsForProgressBar(widget, options);
			result = widget;

		} else if (class_name.equals("ScrollView")) {
			UIScrollView widget = new UIScrollView();
			setPropsForScrollView(widget, options);
			result = widget;

		} else {
			return null;
		}

		JSONArray children = (JSONArray) data.get("children");
		Iterator<?> itr = children.iterator();
		while (itr.hasNext()) {
			UIWidget child = parseFromWidget((JSONObject) itr.next());
			if (child != null) {
				result.addActor(child);
			}
		}
		return result;
	}

	private static void setPropsForWidget(UIWidgetGroup widget, JSONObject options) {
		widget.setWidgetTag(getItemIntValue("tag", options, -1));
		widget.setActionTag(getItemIntValue("actiontag", options, -1));
		widget.setTouchable(getItemBooleanValue("touchAble", options, false) ? Touchable.enabled : Touchable.disabled);
		widget.setWidthAndHeight(getItemFloatValue("width", options, 0), getItemFloatValue("height", options, 0));
		widget.setAnchorPoint(getItemFloatValue("anchorPointX", options, 0.5f),
				getItemFloatValue("anchorPointY", options, 0.5f));
		final String name = getItemStringValue("name", options);
		widget.setName(name == null ? "default" : name);
		System.out.println("" + name + "  " + getItemFloatValue("x", options, 0));
		widget.setPosition(getItemFloatValue("x", options, 0), getItemFloatValue("y", options, 0));
		widget.setScale(getItemFloatValue("scaleX", options, 1.0f), getItemFloatValue("scaleY", options, 1.0f));
		widget.setRotation(getItemFloatValue("rotation", options, 0));
		widget.setVisible(getItemBooleanValue("visible", options, true));
		widget.setZIndex(getItemIntValue("ZOrder", options, -1));
	}

	private static void setColorPropsForWidget(UIWidgetGroup widget, JSONObject options) {
		int aa = getItemIntValue("opacity", options, 255);
		int rr = getItemIntValue("colorR", options, 255);
		int gg = getItemIntValue("colorG", options, 255);
		int bb = getItemIntValue("colorB", options, 255);
		widget.getColor().set(rr * 1.0f / 255, gg * 1.0f / 255, bb * 1.0f / 255, aa * 1.0f / 255);

	}

	private static void setPropsForPanel(UIPanel widget, JSONObject options) {
		setPropsForWidget(widget, options);
		setColorPropsForWidget(widget, options);
		{
			System.out.println("====" + widget.getName());
			String name1 = getItemStringValue("backGroundImage", options);
			JSONObject data = (JSONObject) options.get("backGroundImageData");
			if (data != null) {
				int res_type = getItemIntValue("resourceType", data, 0);
				String name2 = getItemStringValue("path", data);
				widget.setTextureName(getNameWithoutSuffix(name2 == null ? name1 : name2));
			}
		}
	}

	private static void setPropsForButton(UIButton widget, JSONObject options) {
		setPropsForWidget(widget, options);
		System.out.println("" + widget.getWidth() + "  " + widget.getHeight());
		setColorPropsForWidget(widget, options);
		boolean scale_9_enable = getItemBooleanValue("scale9Enable", options, false);
		widget.setScale9Enable(scale_9_enable);
		{
			String name1 = getItemStringValue("normal", options);
			JSONObject data = (JSONObject) options.get("normalData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", data);
			widget.setNormalTextureName(getNameWithoutSuffix(name2 == null ? name1 : name2));
		}
		{
			String name1 = getItemStringValue("pressed", options);
			JSONObject data = (JSONObject) options.get("pressedData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", data);
			widget.setDownTextureName(getNameWithoutSuffix(name2 == null ? name1 : name2));
		}
		{
			String name1 = getItemStringValue("disabled", options);
			JSONObject data = (JSONObject) options.get("disabledData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", options);
			widget.setDisableTextureName(getNameWithoutSuffix(name2 == null ? name1 : name2));
		}
	}

	private static void setPropsForImageView(UIImage widget, JSONObject options) {
		setPropsForWidget(widget, options);
		setColorPropsForWidget(widget, options);
		boolean scale_9_enable = getItemBooleanValue("scale9Enable", options, false);
		widget.setScale9Enable(scale_9_enable);
		{
			String name1 = getItemStringValue("fileName", options);
			JSONObject data = (JSONObject) options.get("fileNameData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", data);
			widget.setTextureName(getNameWithoutSuffix(name2 == null ? name1 : name2));
		}
	}

	private static void setPropsForLabelBMFont(UILabelBMFont widget, JSONObject options) {
		setPropsForWidget(widget, options);
		setColorPropsForWidget(widget, options);
		widget.setText(getItemStringValue("text", options));
		{
			JSONObject data = (JSONObject) options.get("fileNameData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", data);
			widget.setBitmapFontName(name2);
		}
	}

	private static void setPropsForCheckBox(UICheckBox widget, JSONObject options) {
		setPropsForWidget(widget, options);
		setColorPropsForWidget(widget, options);

		{
			String name1 = getItemStringValue("backGroundBox", options);
			JSONObject data = (JSONObject) options.get("backGroundBoxData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", data);
			widget.setBackgroundBoxName(getNameWithoutSuffix(name2 == null ? name1 : name2));
		}
		{
			String name1 = getItemStringValue("backGroundBoxDisabled", options);
			JSONObject data = (JSONObject) options.get("backGroundBoxDisabledData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", data);
			widget.setBackgroundBoxDisabledName(getNameWithoutSuffix(name2 == null ? name1 : name2));
		}
		{
			String name1 = getItemStringValue("backGroundBoxSelected", options);
			JSONObject data = (JSONObject) options.get("backGroundBoxSelectedData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", data);
			widget.setBackgroundBoxSelectedName(getNameWithoutSuffix(name2 == null ? name1 : name2));
		}
		{
			String name1 = getItemStringValue("frontCross", options);
			JSONObject data = (JSONObject) options.get("frontCrossData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", data);
			widget.setFrontCrossName(getNameWithoutSuffix(name2 == null ? name1 : name2));
		}
		{
			String name1 = getItemStringValue("frontCrossDisabled", options);
			JSONObject data = (JSONObject) options.get("frontCrossDisabledData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", data);
			widget.setFrontCrossDisabledName(getNameWithoutSuffix(name2 == null ? name1 : name2));
		}
	}

	private static void setPropsForProgressBar(UIProgressBar widget, JSONObject options) {
		setPropsForWidget(widget, options);
		setColorPropsForWidget(widget, options);

		{
			JSONObject data = (JSONObject) options.get("textureData");
			int res_type = getItemIntValue("resourceType", data, 0);
			String name2 = getItemStringValue("path", data);
			widget.setTextureName(getNameWithoutSuffix(name2));
		}

		widget.setDirection(getItemIntValue("direction", options, 0));
		widget.setPercent(getItemIntValue("percent", options, 0));
	}

	private static void setPropsForScrollView(UIScrollView widget, JSONObject options) {
		setPropsForWidget(widget, options);
		setColorPropsForWidget(widget, options);
		widget.setBound(0, 0, getItemFloatValue("width", options, 0), getItemFloatValue("height", options, 0));
	}

	public static String getNameWithoutSuffix(String name) {
		if (name == null)
			return null;
		return name.substring(0, name.lastIndexOf("."));
	}

	private static int getVersion(String version_num) {
		String[] xx = version_num.split(".");
		int num = 0;
		for (int i = xx.length - 1; i >= 0; i--) {
			num = num * 100 + getNum(xx[i]);
		}
		return num;
	}

	private static int getNum(String num) {
		int result = 0;
		for (int i = num.length() - 1; i >= 0; i--) {
			result = result * 10 + (num.charAt(i) - '0');
		}
		return result;
	}

	//=============================data class==========================

	public static float getItemFloatValue(final String key, final JSONObject js, float default_value) {
		if (js == null)
			return default_value;
		Object value = js.get(key);
		if (value != null) {
			if (value instanceof Double) {
				return ((Double) value).floatValue();
			}

			if (value instanceof Float) {
				return ((Float) value).floatValue();
			}

			if (value instanceof String) {
				return Float.parseFloat((String) value);
			}

			if (value instanceof Integer) {
				return Integer.parseInt((String) value);
			}

			if (value instanceof Long) {
				return ((Long) value).intValue();
			}
		}
		return default_value;
	}

	public static int getItemIntValue(final String key, final JSONObject js, int default_value) {
		if (js == null)
			return default_value;
		Object value = js.get(key);
		if (value != null) {
			if (value instanceof Long) {
				return ((Long) value).intValue();
			}

			if (value instanceof Integer) {
				return ((Integer) value).intValue();
			}

			if (value instanceof String) {
				if (value.equals("NaN")) {
					return -2;
				}
				return Integer.parseInt((String) value);
			}
		}
		return default_value;
	}

	public static boolean getItemBooleanValue(final String key, final JSONObject js, boolean default_value) {
		if (js == null)
			return default_value;
		Object value = js.get(key);
		if (value != null) {
			if (value instanceof Boolean) {
				return ((Boolean) value).booleanValue();
			}
			if (value instanceof String) {
				return Boolean.parseBoolean((String) value);
			}
		}
		return default_value;
	}

	public static String getItemStringValue(final String key, final JSONObject js) {
		if (js == null)
			return null;
		Object value = js.get(key);
		return value == null ? null : (String) value;
	}

	private static String VERSION = "version";

	private static String WIDGET_TREE = "widgetTree";

	private static String CLASS_NAME = "classname";
	private static String NAME = "name";
	private static String CHILDREN = "children";
	private static String OPTIONS = "options";

}
