package com.junerking.ui;

public class UISettings {
	public static boolean DEBUG = true;

	//button在点中状态下如果没有设置图片的反应，改成true则表示down的状态变暗，亮度由touch_rgb决定
	public static boolean enable_button_down_dark = false;
	public static final float touch_rgb = 0.7f;
}
