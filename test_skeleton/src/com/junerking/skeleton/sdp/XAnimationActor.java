package com.junerking.skeleton.sdp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class XAnimationActor extends Actor {
	protected XAnimation.State animation_state;
	protected XAnimation animation;

	protected boolean flip_x = false;
	protected boolean flip_y = false;
	protected boolean removable = false;

	public XAnimationActor(XAnimation animation) {
		this.animation = animation;
		if (animation != null) {
			animation_state = animation.createState();
		}
	}

	public void setFlip(boolean flip_x, boolean flip_y) {
		this.flip_x = flip_x;
		this.flip_y = flip_y;
	}

	public void changeAction(String action) {
		if (animation_state != null) {
			animation_state.changeAction(action);
		}
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	public void setAnimation(XAnimation animation) {
		this.animation = animation;
		if (animation != null) {
			animation_state = animation.createState();
		} else {
			animation_state = null;
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (animation_state != null) {
			animation_state.tick(4000 * delta);
		}
	}

	@Override
	public void draw(SpriteBatch sprite_batch, float parent_alpha) {
		if (animation == null || animation_state == null)
			return;
		animation.render(sprite_batch, x, y, scaleX, scaleY, animation_state.getActionIndex(), animation_state.getFrameId(), flip_x);
	}
}
