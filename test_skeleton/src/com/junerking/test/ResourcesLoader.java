package com.junerking.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.junerking.ui.UIResourcesMgr.UIResourcesLoader;

public class ResourcesLoader implements UIResourcesLoader {

	private static final int FONT_COUNT = 4;
	String[] font_name = { "Heiti10.fnt", "Heiti16.fnt", "Heiti18.fnt", "Heiti19.fnt" };
	BitmapFont[] fonts = new BitmapFont[FONT_COUNT];

	private TextureAtlas atlas = null;

	public ResourcesLoader() {
		loadResources();
	}

	@Override
	public void loadResources() {
		atlas = new TextureAtlas(Gdx.files.internal("assets/cocosui.pack"));
		for (int i = 0; i < FONT_COUNT; i++) {
			fonts[i] = new BitmapFont(Gdx.files.internal("assets/" + font_name[i]), false);
		}
	}

	@Override
	public TextureRegion getTextureRegion(String name) {
		return atlas.findRegion(name);
	}

	@Override
	public TextureRegion getDebugRegion() {
		return atlas.findRegion("debug");
	}

	@Override
	public Sprite getSprite(String name) {
		return atlas.createSprite(name);
	}

	@Override
	public BitmapFont getBitmapFont(String name) {
		for (int i = 0; i < FONT_COUNT; i++) {
			if (font_name[i].equals(name)) {
				return fonts[i];
			}
		}
		return null;
	}

}
