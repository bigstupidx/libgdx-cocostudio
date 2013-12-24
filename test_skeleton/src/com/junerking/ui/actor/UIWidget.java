package com.junerking.ui.actor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.junerking.ui.UIClickListener;

// when we customzised a widget, we need call super()
// this register touch handler for us;

public class UIWidget extends Actor {

	private XXClickListener _x_click_listener;

	public UIWidget() {
		_x_click_listener = new XXClickListener();
		_x_click_listener.setTarget(this);
		this.addListener(_x_click_listener);
	}

	public UIWidget(String name) {
		super(name);
		_x_click_listener = new XXClickListener();
		_x_click_listener.setTarget(this);
		this.addListener(_x_click_listener);
	}

	//must be called before rendering, because It set textureRegions for UIImage, UIButton and so on
	//usually I call it just before I want to show it;
	public void prepare() {
		if (parent_widget == null) {
			return;
		}
	}

	//===========================================================
	protected float half_width, half_height;

	@Override
	public void setWidthAndHeight(float width, float height) {
		this.width = width;
		this.height = height;
		this.half_width = width / 2;
		this.half_height = height / 2;
	}

	@Override
	public void setWidth(float width) {
		this.width = width;
		this.half_width = width / 2;
	}

	@Override
	public void setHeight(float height) {
		this.height = height;
		this.half_height = height / 2;
	}

	//===========================================================

	protected UIWidget parent_widget = null;

	public UIWidget getParentWidget() {
		return parent_widget;
	}

	public void setParentWidget(UIWidget parent_widget) {
		this.parent_widget = parent_widget;
	}

	protected int tag = -1, action_tag = -1;

	public void setWidgetTag(int tag) {
		this.tag = tag;
	}

	public void setActionTag(int tag) {
		this.tag = tag;
	}

	public void setTouchable(Touchable touchable) {
		this.touchable = touchable;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected String texture_name;

	public void setTextureName(String texture_name) {
		this.texture_name = texture_name;
	}

	protected float anchor_point_x = 0.5f;
	protected float anchor_point_y = 0.5f;

	public void setAnchorPoint(float anchor_point_x, float anchor_point_y) {
		this.anchor_point_x = anchor_point_x;
		this.anchor_point_y = anchor_point_y;
	}

	protected int position_type = 0;
	protected float position_percent_x = 0;
	protected float position_percent_y = 0;

	public void setPositionType(int position_type) {
		this.position_type = position_type;
	}

	public void setPositionPercent(float percent_x, float percent_y) {
		this.position_percent_x = percent_x;
		this.position_percent_y = percent_y;
	}

	protected int size_type = 0;
	protected float size_percent_x = 0;
	protected float size_percent_y = 0;

	public void setSizeType(int size_type) {
		this.size_type = size_type;
	}

	public void setSizePercent(float percent_x, float percent_y) {
		this.size_percent_x = percent_x;
		this.size_percent_y = percent_y;
	}

	//==========================================================================

	public UIWidget findWidgetByName(String name) {
		if (this.name != null && this.name.equals(name)) {
			return this;
		}
		return null;
	}

	public UIWidget findWidgetByTag(int tag_id) {
		if (this.tag != -1 && this.tag == tag_id)
			return this;
		return null;
	}

	public UIWidget findActionWidgetByTag(int action_tag_id) {
		if (this.action_tag != -1 && this.action_tag == action_tag_id)
			return this;
		return null;
	}

	//==========================================================================

	protected boolean is_marked_remove = false;

	public void setMarkedToRemove(boolean remove) {
		this.is_marked_remove = remove;
	}

	public boolean isMarkedRemove() {
		return is_marked_remove;
	}

	//==========================================================================

	protected boolean is_touching = false;

	public boolean touchDown(InputEvent event, float x, float y, int pointer) {
		is_touching = true;
		return true;
	}

	public boolean touchDragged(float x, float y, int pointer) {
		is_touching = true;
		return true;
	}

	public void touchUp(float x, float y, int pointer) {
		is_touching = false;
		if (_click_listener != null) {
			_click_listener.click(this, x, y);
		}
	}

	public void touchCancel() {
	}

	//==========================================================================

	protected UIClickListener _click_listener;

	public void setUIClickListener(UIClickListener click_listener) {
		this._click_listener = click_listener;
	}

	public void clicked(float x, float y) {
		System.out.println("=====-clicked!!!!" + this);
		if (_click_listener == null)
			return;
		_click_listener.click(this, x, y);
	}

	//可以设置一个区域，相对于自己的位置的一个区域touch_bound
	//设置之后就可以不受控件本身大小的限制，切图时留空白是一种办法，但是这种更好
	protected Rectangle tbound = new Rectangle();
	protected boolean touch_bound_setted = false;

	public void setBound(float x, float y, float width, float height) {
		touch_bound_setted = true;
		this.tbound.x = x;
		this.tbound.y = y;
		this.tbound.width = width;
		this.tbound.height = height;
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && getTouchable() == Touchable.disabled)
			return null;
		if (touch_bound_setted) {
			if (x < tbound.x || y < tbound.y || x > tbound.width + tbound.x || y > tbound.height + tbound.y) {
				return null;
			}
			return this;

		} else {
			if (x < -half_width || x > half_width || y < -half_height || y > half_height) {
				return null;
			}
			return this;
		}
	}

	//==========================================================================

	protected static boolean isNameSame(String name1, String name2) {
		if (name1 == null || name2 == null)
			return false;
		return name1.equals(name2);
	}

	//==========================================================================

	private static class XXClickListener extends ClickListener {
		private UIWidget target;

		public void setTarget(UIWidget target) {
			this.target = target;
		}

		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			super.touchDown(event, x, y, pointer, button);
			if (target != null) {
				target.touchDown(event, x, y, pointer);
			}
			return true;
		}

		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			super.touchDragged(event, x, y, pointer);
			if (target != null) {
				target.touchDragged(x, y, pointer);
			}
		}

		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			super.touchUp(event, x, y, pointer, button);
			if (target != null) {
				target.touchUp(x, y, pointer);
			}
		}
	}
}
