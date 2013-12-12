package com.junerking.particle;

import com.badlogic.gdx.graphics.Color;

public class ccColor4FUtil {
	public static void copy(Color src, Color dst) {
		dst.a = src.a;
		dst.r = src.r;
		dst.g = src.g;
		dst.b = src.b;
	}

	public static void set(Color dst, float r, float g, float b, float a) {
		dst.a = a;
		dst.r = r;
		dst.g = g;
		dst.b = b;
	}
}
