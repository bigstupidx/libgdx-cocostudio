package com.junerking.skeleton;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.junerking.skeleton.DataDef.BoneData;
import com.junerking.skeleton.DataDef.DisplayData;
import com.junerking.skeleton.DataDef.NodeData;
import com.junerking.skeleton.DataDef.SubTextureData;

public class Bone {
	public String name;

	protected NodeData global, node;
	public BoneData origin;

	private Bone _parent = null;
	protected Tween _tween;

	protected IMatrix _global_transform_matrix = new IMatrix();
	protected IMatrix _transform_matrix_for_child = new IMatrix();

	private ArrayList<Bone> _child = new ArrayList<Bone>();

	public ArrayList<DisplayData> display_list;

	private boolean visible = true;

	private SubTextureData current_subtexture_data = null;//用于描述绘图的锚点
	private AtlasRegion current_region = null;

	protected Armature _armature;

	protected Color color = new Color(1.0f, 1.0f, 1.0f, 1.0f);

	public Bone() {
		_tween = new Tween(this);
		origin = new BoneData();
		global = new NodeData();
		node = new NodeData();
		node.scale_x = 0;
		node.scale_y = 0;
	}

	public void setColor(float r, float g, float b, float a) {
		this.color.set(r, g, b, a);
	}

	public void changeDisplay(int index) {
		if (index == -2)
			index = 0;

		if (index < 0) {
			visible = false;
		} else {
			visible = true;
		}

		DisplayData display_data = display_list.get(index);

		if (_armature != null) {
			current_subtexture_data = _armature.getSubTextureData(display_data.name);
			current_region = _armature.getTextureAtlas().findRegion(display_data.name);

			//fix bug: in cocostudio, for the frame animation, the texture_data.with and height is zero
			if (current_region != null && current_subtexture_data != null) {
				current_subtexture_data.width = current_region.originalWidth;
				current_subtexture_data.height = current_region.originalHeight;
			}
		} else {
			current_subtexture_data = null;
			current_region = null;
		}
		transform = true;
	}

	public void updateZOrder(int z_order) {
		z_order += origin.z_order;
		if (z_order == global.z_order) {
			return;
		}

		global.z_order = z_order;

		if (_armature != null) {
			_armature.updateZOrder();
		}
	}

	public boolean isMatrixTransformChanged() {
		return transform;
	}

	public void addChild(Bone child) {
		child.removeFromParent();
		_child.add(child);
		child.setParent(this);

	}

	public void removeChild(Bone child) {
		_child.remove(child);
		child.setParent(null);
	}

	public void removeFromParent() {
		if (_parent != null) {
			_parent.removeChild(this);
		}
	}

	public void setParent(Bone parent) {
		this._parent = parent;
	}

	public void update(float delta) {
		_tween.update(delta);

		global.x = origin.x + node.x + _tween.node.x;
		global.y = origin.y + node.y + _tween.node.y;
		global.skew_x = origin.skew_x + node.skew_x + _tween.node.skew_x;
		global.skew_y = origin.skew_y + node.skew_y + _tween.node.skew_y;
		global.scale_x = node.scale_x + _tween.node.scale_x;
		global.scale_y = node.scale_y + _tween.node.scale_y;

		float cos_x = (float) Math.cos(global.skew_x);
		float sin_x = (float) Math.sin(global.skew_x);
		float cos_y = (float) Math.cos(global.skew_y);
		float sin_y = (float) Math.sin(global.skew_y);

		_global_transform_matrix.a = global.scale_x * cos_y;
		_global_transform_matrix.b = global.scale_x * sin_y;
		_global_transform_matrix.c = global.scale_y * sin_x;
		_global_transform_matrix.d = global.scale_y * cos_x;
		_global_transform_matrix.tx = global.x;
		_global_transform_matrix.ty = global.y;

		if (_parent != null) {
			_global_transform_matrix.mul(_parent._transform_matrix_for_child);
		}

		if (_child.size() > 0) {
			_transform_matrix_for_child.a = cos_y;
			_transform_matrix_for_child.b = sin_y;
			_transform_matrix_for_child.c = sin_x;
			_transform_matrix_for_child.d = cos_x;
			_transform_matrix_for_child.tx = global.x;
			_transform_matrix_for_child.ty = global.y;
			if (_parent != null) {
				_transform_matrix_for_child.mul(_parent._transform_matrix_for_child);
			}
		}

		for (int i = 0, n = _child.size(); i < n; i++) {
			_child.get(i).update(delta);
		}
	}

	protected boolean transform = true;
	protected float[] _v = new float[8];

	public void render(SpriteBatch sprite_batch, float parent_alpha) {
		if (!visible || current_region == null || current_subtexture_data == null || color.a * parent_alpha == 0)
			return;

		if (transform) {
			float xx = -current_subtexture_data.pivot_x * current_subtexture_data.width;
			float yy = -current_subtexture_data.pivot_y * current_subtexture_data.height;

			float xw = xx + current_subtexture_data.width;
			float yh = yy + current_subtexture_data.height;

			if (current_region.packedWidth != current_region.originalWidth || current_region.packedHeight != current_region.originalHeight) {
				xw -= current_region.originalWidth - current_region.packedWidth - current_region.offsetX;
				yh -= current_region.originalHeight - current_region.packedHeight - current_region.offsetY;
				xx += current_region.offsetX;
				yy += current_region.offsetY;
			}
			_v[0] = _global_transform_matrix.a * xx + _global_transform_matrix.c * yy + _global_transform_matrix.tx;
			_v[1] = _global_transform_matrix.b * xx + _global_transform_matrix.d * yy + _global_transform_matrix.ty;

			_v[2] = _global_transform_matrix.a * xw + _global_transform_matrix.c * yy + _global_transform_matrix.tx;
			_v[3] = _global_transform_matrix.b * xw + _global_transform_matrix.d * yy + _global_transform_matrix.ty;

			_v[4] = _global_transform_matrix.a * xw + _global_transform_matrix.c * yh + _global_transform_matrix.tx;
			_v[5] = _global_transform_matrix.b * xw + _global_transform_matrix.d * yh + _global_transform_matrix.ty;

			_v[6] = _global_transform_matrix.a * xx + _global_transform_matrix.c * yh + _global_transform_matrix.tx;
			_v[7] = _global_transform_matrix.b * xx + _global_transform_matrix.d * yh + _global_transform_matrix.ty;
		}
		float aa = color.a * parent_alpha;
		sprite_batch.setColor(color.r * aa, color.g * aa, color.b * aa, aa);

		sprite_batch.draw(current_region, _v[0], _v[1], _v[2], _v[3], _v[4], _v[5], _v[6], _v[7], current_region.getU(),
				current_region.getV2(), current_region.getU2(), current_region.getV());
	}
}
