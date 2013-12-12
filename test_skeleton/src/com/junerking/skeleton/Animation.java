package com.junerking.skeleton;

import java.util.ArrayList;

import com.junerking.skeleton.DataDef.AnimationData;
import com.junerking.skeleton.DataDef.MovementBoneData;
import com.junerking.skeleton.DataDef.MovementData;

public class Animation extends ProcessBase {

	private Armature _armature;
	private MovementData _movement_data;
	private AnimationData _animation_data;

	public Animation(Armature armature) {
		this._armature = armature;
	}

	@Override
	public void setProcessTimeScale(float time_scale) {
		ArrayList<Bone> bones = _armature._bone_in_depth_list;
		for (int i = 0; i < bones.size(); i++) {
			bones.get(i)._tween.setProcessTimeScale(time_scale);
		}
		this._time_process_scale = time_scale;
	}

	public void setAnimationData(AnimationData data) {
		this._animation_data = data;
	}

	public AnimationData getAnimationData() {
		return _animation_data;
	}

	@Override
	public void gotoAndPlay(Object movement_name, int duration_to, int duration_tween, boolean loop, int tween_easing) {
		if (_animation_data == null)
			return;
		_movement_data = _animation_data.getMovementData((String) movement_name);
		if (_movement_data == null)
			return;

		duration_to = duration_to < 0 ? _movement_data.duration_to : duration_to;
		duration_tween = duration_tween < 0 ? _movement_data.duration_tween : duration_tween;
		duration_tween = duration_tween == 0 ? _movement_data.duration : duration_tween;
		tween_easing = tween_easing == -2 ? _movement_data.tween_easing : tween_easing;
		loop &= _movement_data.loop;

		_time_scale = _movement_data.scale;

		super.gotoAndPlay(null, duration_to, duration_tween, loop, tween_easing);

		_duration = _movement_data.duration;
		if (_duration == 1) {
			_loop = SINGLE;
		} else {
			if (loop) {
				_loop = LIST_LOOP_START;
			} else {
				_loop = LIST_START;
				--_duration;
			}
			_duration_tween = duration_tween;
		}

		for (int i = 0; i < _armature._bone_dic.size(); i++) {
			Bone bone = _armature._bone_dic.get(i);
			if (bone == null)
				continue;
			MovementBoneData movement_bone_data = _movement_data.getMovementBoneData(bone.name);
			movement_bone_data.duration = _movement_data.duration;
			bone._tween.setTimeScale(_time_scale);
			bone._tween.setProcessTimeScale(_time_process_scale);
			bone._tween.gotoAndPlay(movement_bone_data, duration_to, duration_tween, loop, tween_easing);
		}
	}

	@Override
	protected void updateHandler() {
		if (_current_percent >= 1.0f) {
			switch (_loop) {
			case LIST_START: {
				_loop = LIST;
				_current_percent = (_current_percent - 1.0f) * _total_frames / _duration_tween;
				if (_current_percent > 1) {
				} else {
					_total_frames = _duration_tween;
					//listener
				}
			}
				break;
			case LIST:
			case SINGLE: {
				_current_percent = 1;
				_is_complete = true;
			}
				break;
			case LIST_LOOP_START: {
				_loop = 0;
				_total_frames = _duration_tween > 0 ? _duration_tween : 1;
				_current_percent %= 1;
			}
				break;
			default: {
				_loop += _current_percent;
				_current_percent %= 1;
				_to_index = 0;
			}
				break;
			}
		}
	}

}
