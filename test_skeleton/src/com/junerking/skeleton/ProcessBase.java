package com.junerking.skeleton;

public class ProcessBase {
	protected static final int SINGLE = -4;
	protected static final int LIST_START = -3;
	protected static final int LIST_LOOP_START = -2;
	protected static final int LIST = -1;

	protected float _current_frame, _current_percent;

	//临时的总整数，可变，有时候等于duration_to，有时候等于duration_tween
	protected int _total_frames;

	//当前动作的所有帧数
	protected int _duration_tween;

	//总帧数
	protected int _duration;

	//循环类型
	protected int _loop;

	//ease类型，-2表示无类型，使用线形
	protected int _tween_easing;

	protected int _from_index, _to_index;

	protected boolean _is_complete, _is_pause;

	//进度变量，每次叠加，与设计师设计的动画有关，此变量不应该由程序控制
	protected float _time_scale;

	//程序设置的变量，比如在某些情况下将动作变慢变快，由程序控制动画播放速度
	protected float _time_process_scale;

	public ProcessBase() {
		_time_scale = 1.0f;
		_time_process_scale = 1.0f;
		_is_complete = true;
		_is_pause = false;
		_current_frame = 0;
	}

	public void gotoAndPlay(Object animation, int duration, int duration_tween, boolean loop, int tween_easing) {
		_is_complete = false;
		_is_pause = false;
		_current_frame = 0;
		_duration_tween = duration_tween;
		_total_frames = duration;
		_tween_easing = tween_easing;
	}

	public void setTimeScale(float time_scale) {
		this._time_scale = time_scale;
	}

	public float getTimeScale() {
		return _time_scale;
	}

	public void setProcessTimeScale(float time_scale) {
		this._time_process_scale = time_scale;
	}

	public float getProcessTimeScale() {
		return _time_process_scale;
	}

	public boolean isPlaying() {
		return !_is_complete && !_is_pause;
	}

	public void play() {
		if (_is_complete) {
			_is_complete = false;
			_current_frame = 0;
		}
		_is_pause = false;
	}

	public void stop() {
		_is_pause = true;
	}

	public void update(float delta) {
		if (_is_complete || _is_pause)
			return;

		if (_total_frames <= 0) {
			_current_frame = _total_frames = 1;
			_current_percent = 1.0f;
		} else {
			_current_frame += delta * _time_scale * _time_process_scale;
			_current_percent = _current_frame / _total_frames;
			_current_frame %= _total_frames;
		}
		updateHandler();
	}

	protected void updateHandler() {
		//override me 
	}

}
