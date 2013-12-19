package com.junerking.ui.actor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.junerking.ui.UIResourcesMgr;
import com.junerking.ui.UISettings;

public class UIScrollView extends UIWidgetGroup {
	protected float max_y_height, max_scrolly;
	protected int cols, max_col = 1;
	protected OrthographicCamera camera;

	public UIScrollView() {
		super();
		transform = true;
		clear();
	}

	public UIScrollView(String name) {
		super(name);
		transform = true;
		clear();
	}

	public void clear() {
		super.clear();
		max_scrolly = 0;
		max_y_height = 0;
		cols = 0;
	}

	@Override
	public void addActor(UIWidget item) {
		super.addActor(item);
//		item.y = max_y_height + bound.y;
//		item.x = bound.x + cols * item.getWidth();
//		if (++cols == max_col) {
//			cols = 0;
//			max_y_height += item.getHeight();
//		}
		max_y_height = Math.max(max_y_height, item.y + item.getHeight());
		max_scrolly = Math.max(max_scrolly, max_y_height - bound.height);
		relative_y = -max_scrolly;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	private float padding_side_bar;
	private TextureRegion sidebar_region;

	public void setSideBar(TextureRegion textureRegion, float padding_side_bar) {
		this.padding_side_bar = padding_side_bar;
		this.sidebar_region = textureRegion;
	}

	@Override
	public void draw(SpriteBatch sprite_batch, float parent_alpha) {
//		if (child_list.size() == 0)
//			return;

		if (camera == null && getStage() != null) {
			camera = (OrthographicCamera) getStage().getCamera();
		}

		applyTransform(sprite_batch, computeTransform());

		ScissorStack.calculateScissors(camera, sprite_batch.getTransformMatrix(), bound, scissor_rect);

		if (UISettings.DEBUG) {
			sprite_batch.end();
			sprite_batch.begin();
			sprite_batch.draw(UIResourcesMgr.getInstance().getDebugRegion(), bound.x, bound.y, bound.width,
					bound.height);
		}

		if (scissor_rect.width < 1 || scissor_rect.height < 1) {
			resetTransform(sprite_batch);
			return;
		}

		ScissorStack.pushScissors(scissor_rect);
		for (int i = 0, n = child_list.size(); i < n; i++) {
			final UIWidget item = child_list.get(i);
			if (!item.visible)
				continue;

			float pre_x = item.x;
			float pre_y = item.y;
			float position_y = item.y + relative_y;
			if (position_y + item.getHeight() < bound.y || position_y > bound.y + bound.height)
				continue;

			item.setPosition(item.x, position_y);
			item.draw(sprite_batch, parent_alpha);
			item.setPosition(pre_x, pre_y);
		}
		sprite_batch.flush();
		ScissorStack.popScissors();

		drawSideBar(sprite_batch, parent_alpha);

		resetTransform(sprite_batch);
	}

	protected void drawSideBar(SpriteBatch sprite_batch, float parent_alpha) {
		if (sidebar_region == null || max_y_height < bound.height)
			return;
		float sidebar_x = bound.x + bound.width - padding_side_bar + relative_x;
		float sidebar_y;
		float sidebar_height = bound.height / 2;
		if (relative_y >= 0) {
			sidebar_y = bound.y + 3;
			sidebar_height = Math.max(10, (1 - relative_y / bound.height) * sidebar_region.getRegionHeight());

		} else if (relative_y > -max_scrolly) {
			sidebar_y = (-relative_y / max_scrolly) * (bound.height - 6 - sidebar_region.getRegionHeight()) + bound.y
					+ 3;
			sidebar_height = sidebar_region.getRegionHeight();

		} else {
			sidebar_height = Math.max(10,
					(1 + (relative_y + max_scrolly) / bound.height) * sidebar_region.getRegionHeight());
			sidebar_y = bound.y + bound.height - sidebar_height - 3;
		}
		sprite_batch.setColor(parent_alpha, parent_alpha, parent_alpha, parent_alpha);
		sprite_batch.draw(sidebar_region, sidebar_x, sidebar_y, sidebar_region.getRegionWidth(), sidebar_height);
		sprite_batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void init() {
		relative_y = 0;
		running = double_running = false;
	}

	private boolean running = false;
	private boolean double_running = false;

	@Override
	public void act(float timeSpan) {
		super.act(timeSpan);
		if (running) {//fling
			float ax = last_vy < 0 ? 1f : -1f;
			float newvel = last_vy + ax * timeSpan;
			relative_y += newvel * timeSpan;

			if (newvel * last_vy <= 0.0f) {
				running = false;
				if (relative_y > 0 || relative_y < -max_scrolly) {
					double_running = true;
				}
			} else {
				last_vy = newvel;
			}
		}
		if (double_running) {//回退
			if (relative_y > 0) {
				relative_y -= (relative_y) / 5;
			} else if (relative_y < -max_scrolly) {
				relative_y -= (relative_y + max_scrolly) / 5;
			} else {
				double_running = false;
			}
		}
		forceScrollBound();
	}

	protected void forceScrollBound() {
		if (relative_y < -bound.height - max_scrolly)
			relative_y = -bound.height - max_scrolly;
		if (relative_y > bound.height)
			relative_y = bound.height;

		if (relative_y < -max_scrolly) {
			last_vy = -0.01f;
		} else if (relative_y > 0) {
			last_vy = 0.01f;
		}
	}

	public float getRelativeX() {
		return relative_x;
	}

	public float getRelativeY() {
		return relative_y;
	}

	public float getMaxScrollHeight() {
		return max_scrolly;
	}

	protected float relative_y, relative_x, base_y, last_vy, last_ry;
	private long lastTimeStamp;
	protected float down_x, down_y;
	public final float xeps = 5f, yeps = 10f;

	private UIWidget lastTouchedChild = null;

	public boolean dispatchTouchEvent(InputEvent event, float x, float y, int pointer, boolean up) {
		x -= this.x;
		y -= this.y;
		for (int i = child_list.size() - 1; i >= 0; i--) {
			UIWidget child = child_list.get(i);
			if (!child.visible)
				continue;
			float xx = x - child.x;
			float yy = y - child.y - relative_y;

			UIWidget result = (UIWidget) child.hit(xx, yy, true);
			if (result == null)
				continue;

			if (up) {
				if (lastTouchedChild != result) {
					lastTouchedChild.touchUp(Integer.MIN_VALUE, 0, pointer);
				} else {
					lastTouchedChild.touchUp(0, 0, pointer);
				}

			} else {
				if (result.touchDown(event, xx, yy, pointer)) {
					lastTouchedChild = result;
					return true;
				}
			}
		}
		lastTouchedChild = null;
		return false;
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer) {
		down_x = x;
		down_y = y;
		boolean has_touch_focus = dispatchTouchEvent(event, x - relative_x, y - relative_y, pointer, false);
		if (!has_touch_focus) {
			lastTimeStamp = System.currentTimeMillis();
			base_y = y;
			last_vy = 0;
			last_ry = relative_y;
			running = false;
			double_running = false;
		}
		return true;
	}

	@Override
	public boolean touchDragged(float x, float y, int pointer) {
		if (lastTouchedChild != null) {
			if ((Math.abs(down_x - x) > xeps || Math.abs(down_y - y) > yeps)) {
				lastTouchedChild.touchUp(Integer.MIN_VALUE, 0, pointer);
				lastTouchedChild = null;
				lastTimeStamp = System.currentTimeMillis();
				base_y = y;
				last_vy = 0;
				last_ry = relative_y;
				running = false;
				double_running = false;
			}
		} else {
			updateDrag(y, System.currentTimeMillis());
		}
		return true;
	}

	@Override
	public void touchUp(float x, float y, int pointer) {
		if (lastTouchedChild != null) {
			dispatchTouchEvent(null, x - relative_x, y - relative_y, pointer, true);
			return;
		}
		updateDrag(y, System.currentTimeMillis());
		running = true;
		return;
	}

	public void updateDrag(float y, long eventTime) {
		relative_y += y - base_y;
		base_y = y;
		long timeSpan = eventTime - lastTimeStamp;
		if (timeSpan != 0)
			last_vy = last_vy * 0.6f + (relative_y - last_ry) / (timeSpan) * 0.4f;
		last_ry = relative_y;
		lastTimeStamp += timeSpan;
	}

	private Rectangle scissor_rect = new Rectangle();
	protected Rectangle bound = new Rectangle();

	public void setBound(float x, float y, float width, float height) {
		this.bound.x = x;
		this.bound.y = y;
		this.bound.width = width;
		this.bound.height = height;
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (x < bound.x || y < bound.y || x > bound.width + bound.x || y > bound.height + bound.y) {
			return null;
		}
		return this;
	}
}
