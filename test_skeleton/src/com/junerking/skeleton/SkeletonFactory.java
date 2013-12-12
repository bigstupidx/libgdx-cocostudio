package com.junerking.skeleton;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.junerking.skeleton.DataDef.AnimationData;
import com.junerking.skeleton.DataDef.ArmatureData;
import com.junerking.skeleton.DataDef.ArmatureDisplayData;
import com.junerking.skeleton.DataDef.BoneData;
import com.junerking.skeleton.DataDef.DisplayData;
import com.junerking.skeleton.DataDef.FrameData;
import com.junerking.skeleton.DataDef.MovementBoneData;
import com.junerking.skeleton.DataDef.MovementData;
import com.junerking.skeleton.DataDef.NodeData;
import com.junerking.skeleton.DataDef.SpriteDisplayData;
import com.junerking.skeleton.DataDef.SubTextureData;

public class SkeletonFactory {
	public static final int CS_DISPLAY_SPRITE = 0;
	public static final int CS_DISPLAY_ARMTURE = 1;
	public static final int CS_DISPLAY_PARTICLE = 2;
	public static final int CS_DISPLAY_SHADER = 3;
	public static final int CS_DISPLAY_MAX = 4;

	private static final HashMap<String, Skeleton> skeleton_map = new HashMap<String, Skeleton>();

	public static Skeleton createSkeleton(String name) {
		Skeleton result = skeleton_map.get(name);
		if (result == null) {
			result = parseSkeleton(name);
		}
		return result;
	}

	private static Skeleton parseSkeleton(String file_name) {
		String suffix = (String) file_name.subSequence(file_name.lastIndexOf("."), file_name.length());
		System.out.println("++++filename:" + file_name + "  suffix:" + suffix);
		if (suffix != null && (suffix.equals(".json") || suffix.equals(".ExportJson"))) {
			return parseFromJSONFile(file_name);
		}
		return null;
	}

	//TODO
	private static Skeleton parseFromXMLFile(String file_name) {
		return null;
	}

	private static Skeleton parseFromJSONFile(String file_name) {
		try {
			JSONParser parser = new JSONParser();
			FileHandle file_handler = Gdx.files.internal(file_name);
			JSONObject object = (JSONObject) parser.parse(new InputStreamReader(file_handler.read()));

			Skeleton skeleton = new Skeleton();

			{//armature
				JSONArray armture_array = (JSONArray) object.get(ARMATURE_DATA);
				Iterator<?> itr = armture_array.iterator();
				while (itr.hasNext()) {
					skeleton.addArmatureData(parseArmatureData((JSONObject) itr.next()));
				}
			}

			{//animation
				JSONArray animation_array = (JSONArray) object.get(ANIMATION_DATA);
				Iterator<?> itr = animation_array.iterator();
				while (itr.hasNext()) {
					skeleton.addAnimationData(parseAnimationData((JSONObject) itr.next()));
				}
			}

			{//texture
				JSONArray texture_array = (JSONArray) object.get(TEXTURE_DATA);
				Iterator<?> itr = texture_array.iterator();
				while (itr.hasNext()) {
					skeleton.addSubTextureData(parseSubTextureData((JSONObject) itr.next()));
				}
			}

			return skeleton;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//=====================armature=====================================

	private static ArmatureData parseArmatureData(JSONObject js) {
		ArmatureData result = new ArmatureData();
		result.name = (String) js.get(A_NAME);
		JSONArray bone_array = (JSONArray) js.get(BONE_DATA);
		Iterator<?> itr = bone_array.iterator();
		while (itr.hasNext()) {
			result.addBoneData(parseBoneData((JSONObject) itr.next()));
		}
		return result;
	}

	private static BoneData parseBoneData(JSONObject js) {
		BoneData result = new BoneData();
		result.name = (String) js.get(A_NAME);
		result.parent = (String) js.get(A_PARENT);
		result.parseFromJSON(js);
		JSONArray display_array = (JSONArray) js.get(DISPLAY_DATA);
		Iterator<?> itr = display_array.iterator();
		while (itr.hasNext()) {
			result.addDisplayData(parseDisplayData((JSONObject) itr.next()));
		}
		return result;
	}

	private static DisplayData parseDisplayData(JSONObject js) {
		int display_type = getItemIntValue(A_DISPLAY_TYPE, js, -1);

		DisplayData result = null;

		switch (display_type) {
		case CS_DISPLAY_SPRITE: {
			String name = (String) js.get(A_NAME);
			SpriteDisplayData display = new SpriteDisplayData();
			display.name = name.substring(0, name.lastIndexOf("."));
			int index = display.name.lastIndexOf('/');
			if (index >= 0 && index < display.name.length()) {
				display.name = display.name.substring(index + 1);
			}
			JSONArray skin_array = (JSONArray) js.get(SKIN_DATA);
			if (skin_array != null && skin_array.iterator().hasNext()) {
				display.skin_data = new NodeData();
				display.skin_data.parseFromJSON((JSONObject) skin_array.iterator().next());
			}
			result = display;
		}
			break;

		case CS_DISPLAY_ARMTURE: {
			String name = (String) js.get(A_NAME);
			result = new ArmatureDisplayData();
			result.name = name;
		}
			break;
		}
		return result;
	}

	//=====================animation====================================

	private static AnimationData parseAnimationData(JSONObject js) {
		AnimationData result = new AnimationData();
		result.name = (String) js.get(A_NAME);
		JSONArray movement = (JSONArray) js.get(MOVEMENT_DATA);
		Iterator<?> itr = movement.iterator();
		while (itr.hasNext()) {
			result.addMovementData(parseMovementData((JSONObject) itr.next()));
		}
		return result;
	}

	private static MovementData parseMovementData(JSONObject js) {
		MovementData result = new MovementData();
		result.name = (String) js.get(A_NAME);
		result.loop = getItemBooleanValue(A_LOOP, js, false);
		result.duration = getItemIntValue(A_DURATION, js, 0);
		result.duration_to = getItemIntValue(A_DURATION_TO, js, 0);
		result.duration_tween = getItemIntValue(A_DURATION_TWEEN, js, -1);
		result.scale = getItemFloatValue(A_MOVEMENT_SCALE, js, 1.0f);
		result.tween_easing = getItemIntValue(A_TWEEN_EASING, js, -2);

		JSONArray movement_bone_array = (JSONArray) js.get(MOVEMENT_BONE_DATA);
		Iterator<?> itr = movement_bone_array.iterator();
		while (itr.hasNext()) {
			result.addMovementBoneData(parseMovementBoneData((JSONObject) itr.next()));
		}
		return result;
	}

	private static MovementBoneData parseMovementBoneData(JSONObject js) {
		MovementBoneData result = new MovementBoneData();
		result.name = (String) js.get(A_NAME);
		result.delay = getItemFloatValue(A_MOVEMENT_DELAY, js, 0);

		JSONArray frame_array = (JSONArray) js.get(FRAME_DATA);
		Iterator<?> itr = frame_array.iterator();
		while (itr.hasNext()) {
			result.addFrameData(parseFrameData((JSONObject) itr.next()));
		}
		return result;
	}

	private static FrameData parseFrameData(JSONObject js) {
		FrameData result = new FrameData();
		result.frame_index = getItemIntValue(A_FRAME_INDEX, js, -1);
		result.display_index = getItemIntValue(A_DISPLAY_INDEX, js, 0);
		result.tween_easing = getItemIntValue(A_TWEEN_EASING, js, -2);
		result.parseFromJSON(js);

		//for test
//		result.scale_x = 1.0f;
//		result.scale_y = 1.0f;
//		result.skew_x = 0;
//		result.skew_y = 0;
		return result;
	}

	//======================texture=====================================

	private static SubTextureData parseSubTextureData(JSONObject js) {
		SubTextureData result = new SubTextureData();
		result.name = (String) js.get(A_NAME);
		result.width = getItemFloatValue(A_WIDTH, js, 0);
		result.height = getItemFloatValue(A_HEIGHT, js, 0);
		result.pivot_x = getItemFloatValue(A_PIVOT_X, js, 0);
		result.pivot_y = getItemFloatValue(A_PIVOT_Y, js, 0);
		int index = result.name.lastIndexOf('/');
		if (index >= 0 && index < result.name.length()) {
			result.name = result.name.substring(index + 1, result.name.length());
		}
		return result;
	}

	//=============================data class==========================

	public static float getItemFloatValue(final String key, final JSONObject js, float default_value) {
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
		}
		return default_value;
	}

	public static int getItemIntValue(final String key, final JSONObject js, int default_value) {
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
		Object value = js.get(key);
		return value == null ? null : (String) value;
	}

	static final String VERSION = "version";

	static final String ARMATURES = "armatures";
	static final String ARMATURE = "armature";
	static final String BONE = "b";
	static final String DISPLAY = "d";

	static final String ANIMATIONS = "animations";
	static final String ANIMATION = "animation";
	static final String MOVEMENT = "mov";
	static final String FRAME = "f";

	static final String TEXTURE_ATLAS = "TextureAtlas";
	static final String SUB_TEXTURE = "SubTexture";

	static final String A_NAME = "name";
	static final String A_DURATION = "dr";
	static final String A_FRAME_INDEX = "fi";
	static final String A_DURATION_TO = "to";
	static final String A_DURATION_TWEEN = "drTW";
	static final String A_LOOP = "lp";
	static final String A_MOVEMENT_SCALE = "sc";
	static final String A_MOVEMENT_DELAY = "dl";
	static final String A_DISPLAY_INDEX = "dI";

	// static final String A_VERT = "vert";
	// static final String A_FRAG = "frag";
	static final String A_PLIST = "plist";

	static final String A_PARENT = "parent";
	static final String A_SKEW_X = "kX";
	static final String A_SKEW_Y = "kY";
	static final String A_SCALE_X = "cX";
	static final String A_SCALE_Y = "cY";
	static final String A_Z = "z";
	static final String A_EVENT = "evt";
	static final String A_SOUND = "sd";
	static final String A_SOUND_EFFECT = "sdE";
	static final String A_TWEEN_EASING = "twE";
	//static final String A_TWEEN_ROTATE = "twR";
	static final String A_IS_ARMATURE = "isArmature";
	static final String A_DISPLAY_TYPE = "displayType";
	static final String A_MOVEMENT = "mov";

	static final String A_X = "x";
	static final String A_Y = "y";

	static final String A_COCOS2DX_X = "cocos2d_x";
	static final String A_COCOS2DX_Y = "cocos2d_y";

	static final String A_WIDTH = "width";
	static final String A_HEIGHT = "height";
	static final String A_PIVOT_X = "pX";
	static final String A_PIVOT_Y = "pY";

	static final String A_COCOS2D_PIVOT_X = "cocos2d_pX";
	static final String A_COCOS2D_PIVOT_Y = "cocos2d_pY";

	static final String A_BLEND_TYPE = "bd";

	static final String A_ALPHA = "a";
	static final String A_RED = "r";
	static final String A_GREEN = "g";
	static final String A_BLUE = "b";
	static final String A_ALPHA_OFFSET = "aM";
	static final String A_RED_OFFSET = "rM";
	static final String A_GREEN_OFFSET = "gM";
	static final String A_BLUE_OFFSET = "bM";
	static final String A_COLOR_TRANSFORM = "colorTransform";
	static final String A_TWEEN_FRAME = "tweenFrame";
	//static final String A_ROTATION = "rotation";
	//static final String A_USE_COLOR_INFO = "uci";

	static final String CONTOUR = "con";
	static final String CONTOUR_VERTEX = "con_vt";

	//static final String MOVEMENT_EVENT_FRAME = "movementEventFrame";
	//static final String SOUND_FRAME = "soundFrame";

	static final String FL_NAN = "NaN";

	static final String FRAME_DATA = "frame_data";
	static final String MOVEMENT_BONE_DATA = "mov_bone_data";
	static final String MOVEMENT_DATA = "mov_data";
	static final String ANIMATION_DATA = "animation_data";
	static final String DISPLAY_DATA = "display_data";
	static final String SKIN_DATA = "skin_data";
	static final String BONE_DATA = "bone_data";
	static final String ARMATURE_DATA = "armature_data";
	static final String CONTOUR_DATA = "contour_data";
	static final String TEXTURE_DATA = "texture_data";
	static final String VERTEX_POINT = "vertex";
	static final String COLOR_INFO = "color";

	static final String CONFIG_FILE_PATH = "config_file_path";

}
