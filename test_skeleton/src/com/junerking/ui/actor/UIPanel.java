package com.junerking.ui.actor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.junerking.ui.UIResourcesMgr;
import com.junerking.ui.UISettings;

public class UIPanel extends UIWidgetGroup {
	public static final int LAYOUT_ABSOLUTE = 0;
	public static final int LAYOUT_RELATIVE = 1;

	public UIPanel() {
		super();
		transform = true;
	}

	public UIPanel(String name) {
		super(name);
		transform = true;
	}

	protected int layout_type = 0;

	public void setLayoutType(int type) {
		this.layout_type = type;
	}

	public int getLayoutType() {
		return layout_type;
	}

	protected boolean background_scale_9_enable = false;

	public void setBackgroundScale9Enable(boolean backGroundScale9Enable) {
		this.background_scale_9_enable = backGroundScale9Enable;
	}

	protected TextureRegion background_image;
	protected float background_image_offset_x, background_image_offset_y;

	@Override
	public void prepare() {
		super.prepare();
		background_image = UIResourcesMgr.getInstance().getTextureRegion(texture_name);
		if (background_image == null) {
			return;
		}
		background_image_offset_x = (width - background_image.getRegionWidth()) / 2;
		background_image_offset_y = (height - background_image.getRegionHeight()) / 2;
	}

	public void draw(SpriteBatch batch, float parent_alpha) {
		if (transform)
			applyTransform(batch, computeTransform());
		if (UISettings.DEBUG) {
			batch.draw(UIResourcesMgr.getInstance().getDebugRegion(), 0, 0, width, height);
		}
		if (background_image != null) {
			batch.draw(background_image, background_image_offset_x, background_image_offset_y);
		}
		drawChildren(batch, parent_alpha);
		if (transform)
			resetTransform(batch);
//		super.draw(batch, parent_alpha);
	}

}
