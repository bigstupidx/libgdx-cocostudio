package com.junerking.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface UIStateChangeListener {
	//1.UICheckBox，state==1表示选中，state==0表示未选中
	void stateChanged(Actor actor, int state);
}
