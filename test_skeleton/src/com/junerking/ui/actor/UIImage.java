package com.junerking.ui.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.junerking.ui.UIResourcesMgr;

public class UIImage extends UIWidgetGroup {

	public TextureRegion texture_region;
	public Sprite texture_sprite;

	public UIImage() {
		super();
	}

	public UIImage(String name) {
		super(name);
	}

	public UIImage(TextureRegion texture_region) {
		super();
		this.texture_region = texture_region;
		initWidthAndHeight();
	}

	public UIImage(Sprite texture_sprite) {
		super();
		this.texture_sprite = texture_sprite;
		initWidthAndHeight();
	}

	@Override
	public void prepare() {
		super.prepare();
		if (texture_name != null) {
			texture_region = UIResourcesMgr.getInstance().getTextureRegion(texture_name);
		}
		if (texture_region != null) {
			ww = texture_region.getRegionWidth();
			hh = texture_region.getRegionHeight();
			xx = ww * anchor_point_x;
			yy = hh * anchor_point_y;
		}
	}

	//========================================================== 

	protected boolean scale_9_enable = false;

	public void setScale9Enable(boolean enable) {
		this.scale_9_enable = enable;
	}

	public void setTextureRegion(TextureRegion texture_region) {
		this.texture_region = texture_region;
		this.texture_sprite = null;
		initWidthAndHeight();
	}

	public void setSprite(Sprite sprite) {
		this.texture_sprite = sprite;
		this.texture_region = null;
		initWidthAndHeight();
	}

	public void initWidthAndHeight() {
		if (texture_region != null) {
			width = texture_region.getRegionWidth();
			height = texture_region.getRegionHeight();
		} else if (texture_sprite != null) {
			width = texture_sprite.getWidth();
			height = texture_sprite.getHeight();
		}
	}

	protected boolean flip_x = false, flip_y = false;

	//not available
	public void setFlip(boolean flip_x, boolean flip_y) {
		this.flip_x = flip_x;
		this.flip_y = flip_y;
	}

	protected float xx, yy, ww, hh;

	@Override
	public void draw(SpriteBatch batch, float parent_alpha) {
//		System.out.println(x + "  " + y + "  " + tag + "  " + name);
		if (texture_region != null) {
			batch.setColor(color.r, color.g, color.b, color.a * parent_alpha);
			batch.draw(texture_region, x - xx, y - yy, xx, yy, ww, hh, scaleX * (flip_x ? -1 : 1), scaleY
					* (flip_y ? -1 : 1), rotation);

		} else if (texture_sprite != null) {
			texture_sprite.setColor(color);
			texture_sprite.setPosition(x - xx, y - yy);
			texture_sprite.setOrigin(xx, yy);
			texture_sprite.setScale(scaleX, scaleY);

			if (flip_x || flip_y) {
				texture_sprite.flip(flip_x, flip_y);
			}
			texture_sprite.draw(batch);

			if (flip_x || flip_y) {
				texture_sprite.flip(false, false);
			}
		}
		super.draw(batch, parent_alpha);
	}
}
