package com.junerking.ui.actor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.junerking.ui.UIResourcesMgr;

public class UILabelBMFont extends UIWidgetGroup {

	public UILabelBMFont() {
		super();
	}

	public UILabelBMFont(String name) {
		super(name);
	}

	@Override
	public void prepare() {
		super.prepare();
		if (bitmap_font_name != null) {
			bitmap_font = UIResourcesMgr.getInstance().getBitmapFont(bitmap_font_name);
		}
	}

	protected String bitmap_font_name;

	public void setBitmapFontName(String bitmap_font_name) {
		this.bitmap_font_name = bitmap_font_name;
	}

	protected BitmapFont bitmap_font;

	public void setBitmapFont(BitmapFont font) {
		this.bitmap_font = font;
	}

	protected String text;

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void draw(SpriteBatch sprite_batch, float parent_alpha) {
		if (bitmap_font == null || text == null)
			return;
		float x = this.x - anchor_point_x * width;
		float y = this.y - anchor_point_y * height + height;
		sprite_batch.setColor(color);
		bitmap_font.draw(sprite_batch, text, x, y);
	}

}
