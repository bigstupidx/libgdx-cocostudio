package com.junerking.ui.actor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.junerking.ui.UIResourcesMgr;

public class UIButton extends UIWidgetGroup {

	public UIButton() {
		super();
	}

	public UIButton(String name) {
		super(name);
	}

	@Override
	public void prepare() {
		super.prepare();
		normal_texture_region = UIResourcesMgr.getInstance().getTextureRegion(normal_name);
		down_texture_region = UIResourcesMgr.getInstance().getTextureRegion(down_name);
		disable_texture_region = UIResourcesMgr.getInstance().getTextureRegion(disable_name);

		if (normal_texture_region != null) {
			ww = normal_texture_region.getRegionWidth();
			hh = normal_texture_region.getRegionHeight();
			xx = ww * anchor_point_x;
			yy = hh * anchor_point_y;
		}
	}

	protected boolean scale_9_enable = false;

	public void setScale9Enable(boolean enable) {
		this.scale_9_enable = enable;
	}

	//================================================================

	protected TextureRegion normal_texture_region;//正常情况下button状态
	protected TextureRegion down_texture_region;//按下状态
	protected TextureRegion disable_texture_region;//不可触摸状态
	protected String normal_name, down_name, disable_name;

	public void setNormalTextureName(String normal_name) {
		this.normal_name = normal_name;
	}

	public void setDownTextureName(String down_name) {
		this.down_name = down_name;
	}

	public void setDisableTextureName(String disable_name) {
		this.disable_name = disable_name;
	}

	public void setTextureNames(String normal, String pressed, String disable) {
		this.normal_name = normal;
		this.down_name = pressed;
		this.disable_name = disable;
	}

	public void setButtonRegion(TextureRegion normalTextureRegion, TextureRegion downTextureRegion,
			TextureRegion disableTextureRegion) {
		this.normal_texture_region = normalTextureRegion;
		this.down_texture_region = downTextureRegion;
		this.disable_texture_region = disableTextureRegion;
		if (normal_texture_region != null) {
			ww = normal_texture_region.getRegionWidth();
			hh = normal_texture_region.getRegionHeight();
			xx = ww * anchor_point_x;
			yy = hh * anchor_point_y;
		}
	}

	public void setNormalTextureRegion(TextureRegion normal_texture_region) {
		this.normal_texture_region = normal_texture_region;
		if (normal_texture_region != null) {
			ww = normal_texture_region.getRegionWidth();
			hh = normal_texture_region.getRegionHeight();
			xx = ww * anchor_point_x;
			yy = hh * anchor_point_y;
		}

	}

	public void setDownTextureRegion(TextureRegion down_texture_region) {
		this.down_texture_region = down_texture_region;
	}

	public void setDisableTextureRegion(TextureRegion disable_texture_region) {
		this.disable_texture_region = disable_texture_region;
	}

	protected float touch_r = 1.0f, touch_g = 1.0f, touch_b = 1.0f;
	public boolean is_downing_keep = false;
	public float touch_scale = 1.0f;
	public boolean shrink = false;

	public void setTouchColor(float r, float g, float b) {
		this.touch_r = r;
		this.touch_g = g;
		this.touch_b = b;
	}

	protected float xx, yy, ww, hh;

	@Override
	public void draw(SpriteBatch batch, float parent_alpha) {
		if (touchable == Touchable.disabled) {
			if (disable_texture_region != null) {
				batch.setColor(color.r, color.g, color.b, color.a * parent_alpha);
				batch.draw(disable_texture_region, x - xx, y - yy, xx, yy, ww, hh, touch_scale * scaleX, touch_scale
						* scaleY, rotation);
			}

			else if (down_texture_region != null) {
				batch.setColor(color.r, color.g, color.b, color.a * parent_alpha);
				batch.draw(down_texture_region, x - xx, y - yy, xx, yy, ww, hh, touch_scale * scaleX, touch_scale
						* scaleY, rotation);
			}

			else if (normal_texture_region != null) {
				batch.setColor(touch_r, touch_g, touch_b, color.a * parent_alpha);
				batch.draw(normal_texture_region, x - xx, y - yy, xx, yy, ww, hh, touch_scale * scaleX, touch_scale
						* scaleY, rotation);
			}

		} else {
			if (is_touching || is_downing_keep) {
				if (is_touching) {
					batch.setColor(touch_r, touch_g, touch_b, color.a * parent_alpha);
				} else {
					batch.setColor(color.r, color.g, color.b, color.a * parent_alpha);
				}
				if (down_texture_region != null) {
					if (shrink) {
						batch.draw(down_texture_region, x + width / 50, y + height / 50, width * 24 / 25,
								height * 24 / 25);
					} else {
						batch.draw(down_texture_region, x - xx, y - yy, xx, yy, ww, hh, touch_scale, touch_scale,
								rotation);
					}
				} else if (normal_texture_region != null) {
					if (shrink) {
						batch.draw(normal_texture_region, x + width / 50, y + height / 50, width * 24 / 25,
								height * 24 / 25);
					} else {
						batch.draw(normal_texture_region, x - xx, y - yy, xx, yy, ww, hh, touch_scale * scaleX,
								touch_scale * scaleY, rotation);
					}
				}
			} else if (normal_texture_region != null) {
				batch.setColor(color.r, color.g, color.b, color.a * parent_alpha);
				batch.draw(normal_texture_region, x - xx, y - yy, xx, yy, ww, hh, scaleX, scaleY, rotation);
			}
		}
		super.draw(batch, parent_alpha);
	}
}
