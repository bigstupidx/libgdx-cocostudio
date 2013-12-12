package com.junerking.skeleton;

import java.util.ArrayList;

import com.junerking.skeleton.DataDef.AnimationData;
import com.junerking.skeleton.DataDef.ArmatureData;
import com.junerking.skeleton.DataDef.BoneData;
import com.junerking.skeleton.DataDef.SubTextureData;

public class Skeleton {
	private int version;

	public Skeleton() {
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	private ArrayList<ArmatureData> _armatures = new ArrayList<ArmatureData>();
	private ArrayList<AnimationData> _animations = new ArrayList<AnimationData>();
	private ArrayList<SubTextureData> _subtextures = new ArrayList<SubTextureData>();

	public Armature buildArmature(String name) {
		ArmatureData armature_data = null;
		for (int i = 0; i < _armatures.size(); i++) {
			ArmatureData data = _armatures.get(i);
			if (data.name.equals(name)) {
				armature_data = data;
				break;
			}
		}
		if (null == armature_data)
			return null;
		Armature armature = new Armature(name);
		AnimationData animation_data = getAnimationByName(name);
		armature.animation.setAnimationData(animation_data);
		armature.sub_texture_list = _subtextures;

		ArrayList<BoneData> bone_data_list = armature_data.getBoneDataList();
		ArrayList<Bone> bone_list = new ArrayList<Bone>();
		for (int i = 0; i < bone_data_list.size(); i++) {
			bone_list.add(buildBone(bone_data_list.get(i)));
		}
		for (int i = 0; i < bone_list.size(); i++) {
			BoneData bone_data = bone_data_list.get(i);
			Bone bone = bone_list.get(i);
			if (null != bone_data.parent && !bone_data.parent.equals("")) {
				int index_parent = -1;
				for (int j = 0; j < bone_data_list.size(); j++) {
					if (bone_data_list.get(j).name.equals(bone_data.parent)) {
						index_parent = j;
						break;
					}
				}
				Bone bone_parent = bone_list.get(index_parent);
				bone_parent.addChild(bone);
				armature.addToBones(bone, false);
			} else {
				armature.addToBones(bone, true);
			}
		}

		armature.updateZOrder();

		return armature;
	}

	protected Bone buildBone(BoneData bone_data) {
		Bone result = new Bone();
		result.origin.copy(bone_data);
		result.name = bone_data.name;
		result.display_list = bone_data.getDisplayDataList();
		return result;
	}

	public ArmatureData getArmatureByName(String name) {
		for (int i = 0; i < _armatures.size(); i++) {
			ArmatureData armature = _armatures.get(i);
			if (armature.name.compareTo(name) == 0) {
				return armature;
			}
		}
		return null;
	}

	public AnimationData getAnimationByName(String name) {
		for (int i = 0; i < _animations.size(); i++) {
			AnimationData animation = _animations.get(i);
			if (animation.name.equals(name)) {
				return animation;
			}
		}
		return null;
	}

	public void addAnimationData(AnimationData animation) {
		_animations.add(animation);
	}

	public void addArmatureData(ArmatureData armature) {
		_armatures.add(armature);
	}

	public void addSubTextureData(SubTextureData subtexture) {
		_subtextures.add(subtexture);
	}

}
