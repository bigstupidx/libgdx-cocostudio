package com.junerking.skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.junerking.skeleton.DataDef.SubTextureData;

public class Armature extends Actor {
	public String name;
	private ArrayList<Bone> _root_bone_list = new ArrayList<Bone>();
	protected ArrayList<Bone> _bone_in_depth_list = new ArrayList<Bone>();
	protected ArrayList<Bone> _bone_dic = new ArrayList<Bone>();
	public ArrayList<SubTextureData> sub_texture_list;

	public Animation animation;
	protected boolean bones_list_changed = false;

	private TextureAtlas _texture_atlas;

	private float x, y;

	private boolean remove_when_completed = true;

	public void setRemoveWhenCompleted(boolean remove) {
		this.remove_when_completed = remove;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Armature(String name) {
		this.name = name;
		animation = new Animation(this);
	}

	public void setTimeScale(float time_scale) {
		if (animation == null)
			return;
		animation.setProcessTimeScale(time_scale);
	}

	public float getTimeScale() {
		if (animation == null)
			return 0;
		return animation.getProcessTimeScale();
	}

	public void addToBones(Bone bone, boolean is_root) {
		bone._armature = this;
		_bone_in_depth_list.add(bone);
		_bone_dic.add(bone);
		if (is_root) {
			_root_bone_list.add(bone);
		}
		if (bones_list_changed) {
			Collections.sort(_bone_in_depth_list, comparator);
		}
	}

	public void setTextureAtlas(TextureAtlas texture_atlas) {
		this._texture_atlas = texture_atlas;
		for (int i = _bone_dic.size() - 1; i >= 0; i--) {
			_bone_dic.get(i).changeDisplay(-2);
		}
	}

	public SubTextureData getSubTextureData(String name) {
		for (int i = sub_texture_list.size() - 1; i >= 0; i--) {
			SubTextureData sub_texture_data = sub_texture_list.get(i);
			if (sub_texture_data.name.equals(name)) {
				return sub_texture_data;
			}
		}
		return null;
	}

	public TextureAtlas getTextureAtlas() {
		return _texture_atlas;
	}

	public Bone getBone(String name) {
		for (int i = 0; i < _bone_dic.size(); i++) {
			Bone bone = _bone_dic.get(i);
			if (bone.name.compareTo(name) == 0) {
				return bone;
			}
		}
		return null;
	}

	private static final float TIME_DELTA = 0.0166666666666666666666666666666f;
	private float time_delta;

	public void act(float p_delta) {
		super.act(p_delta);

		time_delta = p_delta / TIME_DELTA;

		for (int i = 0; i < _root_bone_list.size(); i++) {
			_root_bone_list.get(i).update(time_delta);
		}

		animation.update(time_delta);
		if (animation._is_complete && remove_when_completed) {
			this.remove();
		}
	}

	private Matrix4 temp_matrix = new Matrix4();
	private Matrix4 local_matrix = new Matrix4();

	@Override
	public void draw(SpriteBatch sprite_batch, float parent_alpha) {
		temp_matrix.set(sprite_batch.getTransformMatrix());
		local_matrix.set(temp_matrix).translate(x, y, 0).scale(scaleX, scaleY, 0);

		sprite_batch.setTransformMatrix(local_matrix);
		for (int i = 0; i < _bone_in_depth_list.size(); i++) {
			_bone_in_depth_list.get(i).render(sprite_batch, parent_alpha);
		}
		sprite_batch.setTransformMatrix(temp_matrix);
	}

	public void updateZOrder() {
		if (_bone_in_depth_list == null)
			return;
		Collections.sort(_bone_in_depth_list, comparator);
	}

	private MyComparator comparator = new MyComparator();

	public class MyComparator implements Comparator<Bone> {
		public int compare(Bone a, Bone b) {
			return a.global.z_order > b.global.z_order ? 1 : -1;
		}
	}
}
