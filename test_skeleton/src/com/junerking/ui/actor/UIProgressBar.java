package com.junerking.ui.actor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.junerking.ui.UIResourcesMgr;

public class UIProgressBar extends UIWidgetGroup {
	public static final int DIRECTION_HORIZONTAL = 0;
	public static final int DIRECTION_VERTICAL = 1;

	public UIProgressBar() {
		super();
	}

	public UIProgressBar(String name) {
		super(name);
	}

	//==============================================
	protected int percent = 0;

	public void setPercent(int percent) {
		this.percent = percent;
		if (percent < 0)
			percent = 0;
		if (percent > 100)
			percent = 100;
	}

	public int getPercent() {
		return percent;
	}

	protected int direction;

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	//==========================================
	protected TextureRegion region;

	@Override
	public void prepare() {
		super.prepare();
		region = UIResourcesMgr.getInstance().getTextureRegion(texture_name);
		if (region != null) {
			progress_srcx = region.getRegionX();
			progress_srcy = region.getRegionY();
			progress_srcwidth = region.getRegionWidth();
			progress_srcheight = region.getRegionHeight();
		}
	}

	protected int progress_srcx, progress_srcy, progress_srcwidth, progress_srcheight;

	@Override
	public void draw(SpriteBatch batch, float parent_alpha) {
		super.draw(batch, parent_alpha);

		if (region == null)
			return;

		batch.setColor(color);

		if (direction == DIRECTION_HORIZONTAL) {
			final int p_width = (int) ((1.0f * percent / 100) * progress_srcwidth);
			if (p_width < 1) {
				return;
			}
			batch.draw(region.getTexture(), x - progress_srcwidth * anchor_point_x, y - progress_srcheight
					* anchor_point_y, progress_srcx, progress_srcy, p_width, progress_srcheight);
		} else {
			final int p_height = (int) ((percent * 1.0f / 100) * progress_srcheight);
			if (p_height < 1) {
				return;
			}
			batch.draw(region.getTexture(), x - progress_srcwidth * anchor_point_x, y - progress_srcheight
					* anchor_point_y, progress_srcx, progress_srcy, progress_srcwidth, p_height);
		}

	}
}
