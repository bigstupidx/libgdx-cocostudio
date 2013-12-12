package com.junerking.skeleton;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class DataDef {

	public static class DisplayData {
		public String name;

		public int getDisplayType() {
			return -1;
		}
	}

	public static class SpriteDisplayData extends DisplayData {
		public NodeData skin_data = null;

		public int getDisplayType() {
			return SkeletonFactory.CS_DISPLAY_SPRITE;
		}
	}

	public static class ArmatureDisplayData extends DisplayData {
		public int getDisplayType() {
			return SkeletonFactory.CS_DISPLAY_ARMTURE;
		}
	}

	public static class ParticleDisplayData extends DisplayData {
		public int getDisplayType() {
			return SkeletonFactory.CS_DISPLAY_PARTICLE;
		}
	}

	public static class NodeData {
		public float x;
		public float y;
		public int z_order;

		public float skew_x;
		public float skew_y;
		public float tween_rotate;//skew_x, skew_y, tween_rotate影响rotation

		public float scale_x;
		public float scale_y;

		public boolean using_color = false;
		public float r = 1.0f, g = 1.0f, b = 1.0f, a = 1.0f;

		public NodeData() {
		}

		public NodeData(float x, float y, float scale_x, float scale_y, float skew_x, float skew_y) {
			this.x = x;
			this.y = y;
			this.scale_x = scale_x;
			this.scale_y = scale_y;
			this.skew_x = skew_x;
			this.skew_y = skew_y;
		}

		public void copy(NodeData node) {
			this.x = node.x;
			this.y = node.y;
			this.skew_x = node.skew_x;
			this.skew_y = node.skew_y;
			this.scale_x = node.scale_x;
			this.scale_y = node.scale_y;
			this.z_order = node.z_order;

			this.using_color = node.using_color;
			this.r = node.r;
			this.g = node.g;
			this.b = node.b;
			this.a = node.a;
		}

		public void parseFromJSON(JSONObject js) {
			x = getItemFloatValue(SkeletonFactory.A_X, js, 0);
			y = getItemFloatValue(SkeletonFactory.A_Y, js, 0);
			z_order = getItemIntValue(SkeletonFactory.A_Z, js, 0);
			scale_x = getItemFloatValue(SkeletonFactory.A_SCALE_X, js, 1);
			scale_y = getItemFloatValue(SkeletonFactory.A_SCALE_Y, js, 1);
			skew_x = getItemFloatValue(SkeletonFactory.A_SKEW_X, js, 0);
			skew_y = getItemFloatValue(SkeletonFactory.A_SKEW_Y, js, 0);

			//version below 1.1.0.0
			JSONArray color_array = (JSONArray) js.get(SkeletonFactory.COLOR_INFO);
			if (color_array != null && color_array.iterator().hasNext()) {
				using_color = true;
				JSONObject item = (JSONObject) color_array.iterator().next();
				r = getItemIntValue(SkeletonFactory.A_RED, item, 255) * 1.0f / 255;
				g = getItemIntValue(SkeletonFactory.A_GREEN, item, 255) * 1.0f / 255;
				b = getItemIntValue(SkeletonFactory.A_BLUE, item, 255) * 1.0f / 255;
				a = getItemIntValue(SkeletonFactory.A_ALPHA, item, 255) * 1.0f / 255;
			}

			//version 1.1.0.0
//			JSONObject item = (JSONObject) js.get(SkeletonFactory.COLOR_INFO);
//			System.out.println("=-======= " + item);
//			if (item != null) {
//				using_color = true;
//				r = getItemIntValue(SkeletonFactory.A_RED, item, 255) * 1.0f / 255;
//				g = getItemIntValue(SkeletonFactory.A_GREEN, item, 255) * 1.0f / 255;
//				b = getItemIntValue(SkeletonFactory.A_BLUE, item, 255) * 1.0f / 255;
//				a = getItemIntValue(SkeletonFactory.A_ALPHA, item, 255) * 1.0f / 255;
//			}
		}

		public void subtract(NodeData from, NodeData to) {
			x = to.x - from.x;
			y = to.y - from.y;
			scale_x = to.scale_x - from.scale_x;
			scale_y = to.scale_y - from.scale_y;
			skew_x = to.skew_x - from.skew_x;
			skew_y = to.skew_y - from.skew_y;

			if (from.using_color || to.using_color || using_color) {
				r = to.r - from.r;
				g = to.g - from.g;
				b = to.b - from.b;
				a = to.a - from.a;
				using_color = true;
			} else {
				r = 0;
				g = 0;
				b = 0;
				a = 0;
				using_color = false;
			}

			skew_x %= DOUBLE_PI;

			if (skew_x > Math.PI) {
				skew_x -= DOUBLE_PI;
			}

			if (skew_x < -Math.PI) {
				skew_x += DOUBLE_PI;
			}

			skew_y %= DOUBLE_PI;

			if (skew_y > Math.PI) {
				skew_y -= DOUBLE_PI;
			}

			if (skew_y < -Math.PI) {
				skew_y += DOUBLE_PI;
			}

			if (to.tween_rotate != 0) {
				skew_x += to.tween_rotate * DOUBLE_PI;
				skew_y += to.tween_rotate * DOUBLE_PI;
			}
		}

		public String toString() {
			return "x:" + x + " y:" + y + " skewX:" + skew_x + " skewY:" + skew_y + " scaleX:" + scale_x + " scaleY:"
					+ scale_y;
		}
	}

	private static final float DOUBLE_PI = MathUtils.PI * 2;

	public static final class BoneData extends NodeData {
		public String name;
		public String parent;

		private ArrayList<DisplayData> display_list = new ArrayList<DisplayData>();

		public void addDisplayData(DisplayData display_data) {
			this.display_list.add(display_data);
		}

		public DisplayData getDisplayData(int display_index) {
			if (display_index < 0 || display_index >= display_list.size())
				return null;
			return display_list.get(display_index);
		}

		public ArrayList<DisplayData> getDisplayDataList() {
			return display_list;
		}
	}

	public static class ArmatureData {
		public String name;

		private ArrayList<BoneData> bone_list = new ArrayList<BoneData>();

		public void addBoneData(BoneData bone) {
			this.bone_list.add(bone);
		}

		public BoneData getBoneData(String name) {
			for (int i = 0, n = bone_list.size(); i < n; i++) {
				BoneData bone = bone_list.get(i);
				if (bone != null && bone.name.equals(name)) {
					return bone;
				}
			}
			return null;
		}

		public ArrayList<BoneData> getBoneDataList() {
			return bone_list;
		}
	}

	public static final class FrameData extends NodeData {
		public int frame_index;

		public int display_index;

		//! Every frame's tween easing effect
		public int tween_easing;

		//! Whether it's a tween key frame
		public boolean is_tween;

		public int duration = 1;

		public Color color = new Color();

		public void copy(FrameData data) {
			super.copy(data);
			this.display_index = data.display_index;
		}
	}

	public static final class MovementBoneData {
		private ArrayList<FrameData> frame_list = new ArrayList<FrameData>();
		public String name;
		public float delay;
		public float scale = 1.0f;

		public int duration;

		public void addFrameData(FrameData item) {
			this.frame_list.add(item);
		}

		public FrameData getFrameData(int index) {
			return frame_list.get(index);
		}

		public ArrayList<FrameData> getFrameDataList() {
			return frame_list;
		}
	}

	public static final class MovementData {
		public String name;
		// the frames this movement will last
		public int duration;

		//切换到当前动作的帧数，比如本来是run，切换到walk，不能直接转过来，有一个过渡
		public int duration_to;

		//切换时候的函数
		public int duration_tween;

		//此动作是否循环
		public boolean loop;

		public int tween_easing;

		//scale this movement 
		public float scale;

		private ArrayList<MovementBoneData> movementbone_list = new ArrayList<MovementBoneData>();

		public void addMovementBoneData(MovementBoneData item) {
			movementbone_list.add(item);
		}

		public MovementBoneData getMovementBoneData(String name) {
			for (int i = 0, n = movementbone_list.size(); i < n; i++) {
				MovementBoneData item = movementbone_list.get(i);
				if (item != null && item.name.equals(name)) {
					return item;
				}
			}
			return null;
		}

	}

	public static final class AnimationData {
		public String name;
		private ArrayList<MovementData> movement_list = new ArrayList<MovementData>();

		public void addMovementData(MovementData item) {
			movement_list.add(item);
		}

		public MovementData getMovementData(String name) {
			for (int i = 0, n = movement_list.size(); i < n; i++) {
				MovementData item = movement_list.get(i);
				if (item != null && item.name.equals(name)) {
					return item;
				}
			}
			return null;
		}
	}

	public static final class SubTextureData {
		public String name;
		float width, height;
		float pivot_x, pivot_y;
		//TODO: add contour data
	}

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
}
