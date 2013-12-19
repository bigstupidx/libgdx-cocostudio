package com.junerking.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public interface UITouchListener {
	boolean touchDown(InputEvent event, float x, float y, int pointer, int button);

	void touchDragged(InputEvent event, float x, float y, int pointer);

	void touchUp(InputEvent event, float x, float y, int pointer, int button);
}
