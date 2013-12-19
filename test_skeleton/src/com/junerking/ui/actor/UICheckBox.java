package com.junerking.ui.actor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.junerking.ui.UIResourcesMgr;
import com.junerking.ui.UIStateChangeListener;

public class UICheckBox extends UIWidgetGroup {
	protected boolean selected = false;

	public UICheckBox() {
		super();
	}

	public UICheckBox(String name) {
		super(name);
	}

	//========================================================================

	@Override
	public void prepare() {
		super.prepare();
		if (background_box != null) {
			bg_box = UIResourcesMgr.getInstance().getTextureRegion(background_box);
		}
		if (background_box_disabled != null) {
			bg_box_disabled = UIResourcesMgr.getInstance().getTextureRegion(background_box_disabled);
		}
		if (background_box_selected != null) {
			bg_box_selected = UIResourcesMgr.getInstance().getTextureRegion(background_box_selected);
		}
		if (front_cross != null) {
			fc = UIResourcesMgr.getInstance().getTextureRegion(front_cross);
		}
		if (front_cross_disabled != null) {
			fc_disabled = UIResourcesMgr.getInstance().getTextureRegion(front_cross_disabled);
		}
	}

	protected String background_box;
	protected String background_box_disabled;
	protected String background_box_selected;
	protected String front_cross;
	protected String front_cross_disabled;

	protected TextureRegion bg_box, bg_box_disabled, bg_box_selected;
	protected TextureRegion fc_disabled, fc;

	public void setBackgroundBoxName(String name) {
		this.background_box = name;
	}

	public void setBackgroundBoxDisabledName(String name) {
		this.background_box_disabled = name;
	}

	public void setBackgroundBoxSelectedName(String name) {
		this.background_box_selected = name;
	}

	public void setFrontCrossName(String name) {
		this.front_cross = name;
	}

	public void setFrontCrossDisabledName(String name) {
		this.front_cross_disabled = name;
	}

	public void setBackgroundBoxRegion(TextureRegion region) {
		this.bg_box = region;
	}

	public void setBackgroundBoxDisabledRegion(TextureRegion region) {
		this.bg_box_disabled = region;
	}

	public void setBackgroundboxSelectedRegion(TextureRegion region) {
		this.bg_box_selected = region;
	}

	public void setFrontCrossRegion(TextureRegion region) {
		this.fc = region;
	}

	public void setFrontCrossDisabledRegion(TextureRegion region) {
		this.fc_disabled = region;
	}

	//=============================================================

	protected UIStateChangeListener _ui_change_listener;

	public void setStateChangeListener(UIStateChangeListener listener) {
		this._ui_change_listener = listener;
	}

	public boolean getSelectedState() {
		return selected;
	}

	public void setSelectedState(boolean selected) {
		this.selected = selected;
	}

	//=============================================================

	public void touchUp(float x, float y, int pointer) {
		super.touchUp(x, y, pointer);
		selected = !selected;

		if (_ui_change_listener != null) {
			_ui_change_listener.stateChanged(this, selected ? 1 : 0);
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parent_alpha) {
		batch.setColor(color);
		if (touchable == Touchable.disabled) {
			if (bg_box_disabled != null) {
				drawRegion(batch, bg_box_disabled);
			} else if (bg_box != null) {
				drawRegion(batch, bg_box);
			}
			if (fc_disabled != null) {
				drawRegion(batch, fc_disabled);
			}

		} else if (selected) {
			if (bg_box_selected != null) {
				drawRegion(batch, bg_box_selected);

			} else if (bg_box != null) {
				drawRegion(batch, bg_box);
			}

			if (fc != null) {
				drawRegion(batch, fc);

			} else if (fc_disabled != null) {
				drawRegion(batch, fc_disabled);
			}
		} else {
			if (bg_box != null) {
				drawRegion(batch, bg_box);
			}
		}
		super.draw(batch, parent_alpha);
	}

	protected void drawRegion(SpriteBatch batch, TextureRegion region) {
		float ww = region.getRegionWidth();
		float hh = region.getRegionHeight();
		float xx = ww * anchor_point_x;
		float yy = hh * anchor_point_y;
		batch.draw(region, x - xx, y - yy, xx, yy, ww, hh, scaleX, scaleY, rotation);
	}

}
