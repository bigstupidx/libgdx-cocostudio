package com.junerking.skeleton;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.junerking.skeleton.DataDef.FrameData;
import com.junerking.skeleton.DataDef.MovementBoneData;
import com.junerking.skeleton.DataDef.NodeData;

public class Tween extends ProcessBase {
	private static final float HALF_PI = MathUtils.PI * 0.5f;

	protected FrameData node;
	private FrameData _from;
	private FrameData _between;

	private MovementBoneData movement_bone_data;
	private int _between_duration;
	private int _total_duration;

	private int _frame_tween_easing = -2;
	private Bone _bone;

	public Tween(Bone bone) {
		this._bone = bone;
		_from = new FrameData();
		_between = new FrameData();
		node = new FrameData();
	}

	//duration_to：其他动作切换到现在动作需要的frame数目
	//duration_tween：当前动作的所有总共frame数目，和上层的movementdata中的frame数一致
	@Override
	public void gotoAndPlay(Object movement_bone_data, int duration_to, int duration_tween, boolean loop, int tween_easing) {
		this.movement_bone_data = (MovementBoneData) movement_bone_data;

		if (this.movement_bone_data == null)
			return;

		super.gotoAndPlay(null, duration_to, duration_tween, loop, tween_easing);

		_total_duration = 0;
		_between_duration = 0;
		_from_index = 0;
		_to_index = 0;

		int frame_list_size = this.movement_bone_data.getFrameDataList().size();

		FrameData _next_key_frame = this.movement_bone_data.getFrameData(0);

		_duration = this.movement_bone_data.duration;

		if (frame_list_size == 1) {
			_loop = SINGLE;
			if (duration_to == 0) {
				setBetween(_next_key_frame, _next_key_frame);
			} else {
				setBetween(node, _next_key_frame);
			}
			_frame_tween_easing = 1;

		} else if (frame_list_size > 1) {
			if (loop) {
				_loop = LIST_LOOP_START;
			} else {
				_loop = LIST_START;
				--_duration;
			}

			_duration_tween = (int) (duration_tween * this.movement_bone_data.scale);

			if (loop && this.movement_bone_data.delay != 0) {
				setBetween(node, tweenNodeTo(updateFrameData(1 - this.movement_bone_data.delay), _between));

			} else {
				if (duration_to == 0) {
					setBetween(_next_key_frame, _next_key_frame);
				} else {
					setBetween(node, _next_key_frame);
				}
			}
		}
		tweenNodeTo(0, null);
	}

	@Override
	protected void updateHandler() {
		if (_current_percent >= 1) {
			//TODO：更多的loop类型
			switch (_loop) {
			case SINGLE: {
				_current_percent = 1;
				_is_complete = true;
			}
				break;
			case LIST_START: {
				_loop = LIST;
				if (_duration_tween <= 0) {
					_current_percent = 1;
				} else {
					_current_percent = (_current_percent - 1) * _total_frames / _duration_tween;
				}

				if (_current_percent >= 1) {
					_current_percent = 1;
					_is_complete = true;
					break;
				}
				_total_frames = _duration_tween;
				_total_duration = 0;
				_to_index = 0;
				_from_index = 0;
			}
				break;
			case LIST: {
				_current_percent = 1;
				_is_complete = true;
			}
				break;
			case LIST_LOOP_START: {
				_loop = 0;
				_total_frames = _duration_tween > 0 ? _duration_tween : 1;
				if (movement_bone_data.delay != 0) {
					_current_frame = (1 - movement_bone_data.delay) * _total_frames;
					_current_percent += _current_frame / _total_frames;
				}
				_current_percent %= 1;
			}
				break;
			default: {
				_loop += _current_percent;
				_current_percent %= 1;
				_total_duration = 0;
				_between_duration = 0;
				_to_index = _from_index = 0;
			}
				break;
			}
		} else if (_loop < LIST) {
			_current_percent = (float) Math.sin(_current_percent * HALF_PI);
		}

		//上面的percent表示整个总的序列帧的百分比
		//下面的percent意义已经不再是总体的百分比了，而是当前帧的百分比

		if (_loop >= LIST) {
			_current_percent = updateFrameData(_current_percent);
		}

		if (_frame_tween_easing != -2) {
			tweenNodeTo(_current_percent, null);
		}
	}

	private float updateFrameData(float current_percent) {
		float played = current_percent * _duration;

		//played表示当前对于总体的进度
		//_total_duration表示当前的最大帧，比如现在正处于关键帧1和5之间，那么此值就是5；
		//_between_duration表示当前的进度，比如现在正处于关键帧1和5之间，那么此值就是 5-1 = 4；
		//如果played超过5，表示要进入下一个关键帧，可能是5-x，这个时候就需要更新between的值

		if (played >= _total_duration || played < _total_duration + _between_duration) {
			FrameData from = null, to = null;
			ArrayList<FrameData> frame_list = movement_bone_data.getFrameDataList();
			int length = frame_list.size();

			if (played < frame_list.get(0).frame_index) {
				from = to = frame_list.get(0);
				setBetween(from, to);
				return current_percent;

			} else if (played >= frame_list.get(length - 1).frame_index) {
				from = to = frame_list.get(length - 1);
				setBetween(from, to);
				return current_percent;
			}

			do {
				from = frame_list.get(_from_index);
				_total_duration = from.frame_index;
				if (++_to_index >= length) {
					_to_index = 0;
				}
				_from_index = _to_index;
				to = frame_list.get(_to_index);

				if (played == from.frame_index) {
					break;
				}
			} while (played < from.frame_index || played >= to.frame_index);

			_between_duration = to.frame_index - from.frame_index;

			_frame_tween_easing = from.tween_easing;

			setBetween(from, to);
		}

		current_percent = _between_duration == 0 ? 0 : (played - _total_duration) / _between_duration;

		if (_frame_tween_easing != -2) {
			int tween_easing = _tween_easing == -2 ? _frame_tween_easing : _tween_easing;
			if (tween_easing != 0) {
				current_percent = getEaseValue(current_percent, tween_easing);
			}
		}

		if (current_percent < 0) {
			current_percent %= 1;
			current_percent += 1;
		}
		return current_percent;
	}

	private void setBetween(FrameData from, FrameData to) {
		_from.copy(from);
		if (to instanceof FrameData) {
			if (((FrameData) to).display_index < 0) {
				_between.subtract(from, from);
				return;
			}
		}
		_between.subtract(_from, to);
		arriveKeyFrame(_from);
	}

	private void arriveKeyFrame(NodeData from) {
		if (from != null && from instanceof FrameData) {
			FrameData frame = (FrameData) from;
			if (_bone != null) {
				_bone.changeDisplay(frame.display_index);
				_bone.updateZOrder(frame.z_order);
			}
		}
	}

	private FrameData tweenNodeTo(float percent, FrameData node) {
		if (node == null) {
			node = this.node;
		}
		node.x = _from.x + percent * _between.x;
		node.y = _from.y + percent * _between.y;

		node.skew_x = _from.skew_x + percent * _between.skew_x;
		node.skew_y = _from.skew_y + percent * _between.skew_y;
		node.scale_x = _from.scale_x + percent * _between.scale_x;
		node.scale_y = _from.scale_y + percent * _between.scale_y;

		if (_between.using_color && _bone != null) {
			tweenColorTo(percent, node);
		}
		return node;
	}

	private void tweenColorTo(float percent, NodeData node) {
		node.r = _from.r + percent * _between.r;
		node.g = _from.g + percent * _between.g;
		node.b = _from.b + percent * _between.b;
		node.a = _from.a + percent * _between.a;

		_bone.setColor(node.r, node.g, node.b, node.a);
	}

	//TODO：更多的ease类型
	private float getEaseValue(float percent, int ease_type) {
		if (ease_type > 1) {
			percent = (float) (0.5f * (1 - Math.cos(percent * Math.PI)));
			ease_type -= 1;

		} else if (ease_type > 0) {
			percent = (float) Math.sin(percent * HALF_PI);

		} else {
			percent = (float) (1 - Math.cos(percent * HALF_PI));
			ease_type = -ease_type;
		}
		return percent * ease_type + (1 - ease_type);
	}

}
