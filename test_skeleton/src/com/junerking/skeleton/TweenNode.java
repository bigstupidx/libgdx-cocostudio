package com.junerking.skeleton;

import com.badlogic.gdx.math.MathUtils;
import com.junerking.skeleton.DataDef.NodeData;

public class TweenNode extends NodeData {

	private static final float DOUBLE_PI = MathUtils.PI * 2;

	public int tween_rotate;

	public TweenNode() {

	}

	public TweenNode(float x, float y, float scale_x, float scale_y, float skew_x, float skew_y) {
		super(x, y, scale_x, scale_y, skew_x, skew_y);
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

		if (to instanceof TweenNode) {
			TweenNode tween_node = (TweenNode) to;
			skew_x += tween_node.tween_rotate * DOUBLE_PI;
			skew_y += tween_node.tween_rotate * DOUBLE_PI;
		}
	}
}
