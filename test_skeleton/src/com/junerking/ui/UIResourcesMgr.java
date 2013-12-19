package com.junerking.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class UIResourcesMgr {
	private static UIResourcesMgr _instance;

	public static UIResourcesMgr getInstance() {
		if (_instance == null) {
			_instance = new UIResourcesMgr();
		}
		return _instance;
	}

	public static void dispose() {
		_instance = null;
	}

	private UIResourcesMgr() {
	}

	//=============================================

	public static interface UIResourcesLoader {
		void loadResources();

		TextureRegion getTextureRegion(String name);

		TextureRegion getDebugRegion();

		Sprite getSprite(String name);

		BitmapFont getBitmapFont(String font_name);
	}

	//=============================================

	private UIResourcesLoader loader;

	public void setResourcesLoader(UIResourcesLoader loader) {
		this.loader = loader;
	}

	public void loadResources() {
		if (loader != null) {
			loader.loadResources();
		}
	}

	public TextureRegion getTextureRegion(String name) {
		return loader == null ? null : loader.getTextureRegion(name);
	}

	public Sprite getSprite(String name) {
		return loader == null ? null : loader.getSprite(name);
	}

	public BitmapFont getBitmapFont(String font_name) {
		return loader == null ? null : loader.getBitmapFont(font_name);
	}

	public TextureRegion getDebugRegion() {
		return loader == null ? null : loader.getDebugRegion();
	}
}
