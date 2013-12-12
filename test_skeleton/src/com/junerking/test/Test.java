package com.junerking.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.junerking.particle.CCParticleSystem;
import com.junerking.particle.utils.ParticleManager;
import com.junerking.skeleton.Armature;
import com.junerking.skeleton.Skeleton;
import com.junerking.skeleton.SkeletonFactory;
import com.junerking.skeleton.sdp.XAnimation;
import com.junerking.skeleton.sdp.XAnimationActor;
import com.junerking.textureatlas.TextureAtlasManager;

public class Test implements ApplicationListener {
	private static final int TOTAL_TEST_COUNT = 4;

	private OrthographicCamera camera;
	private SpriteBatch sprite_batch;

	private CCParticleSystem particle;
	private Armature armature, armature2;
	private TextureRegion background;

	private TextureAtlas texture_atlas;
	private Sprite texture_atlas_sprite;

	private XAnimation.State animation_state;
	private XAnimation animation;
	private XAnimationActor animation_actor;

	private Texture bk;

	private int test_index = 0, test_index_small = 0;

	@Override
	public void create() {
		camera = new OrthographicCamera(480, 800);
		camera.position.set(240, 400, 0);
		camera.update();

		sprite_batch = new SpriteBatch();

		createTestCase(1);
	}

	private void createTestCase(int test_index) {
		if (test_index < 0)
			test_index = 0;
		if (test_index >= TOTAL_TEST_COUNT)
			test_index = TOTAL_TEST_COUNT - 1;

		this.test_index = test_index;
		background = new TextureRegion(new Texture(Gdx.files.internal("assets/bk.png")));

		switch (test_index) {
		case 0: {
			//粒子效果的测试
			//粒子系统测试，粒子系统的大部分代码从cocos2d-android-1中抠出来的
			particle = ParticleManager.create("assets/icearrow.plist", null);
			particle.setTexture(new TextureRegion(new Texture("assets/icearrow.png")));
			particle.setPositionType(CCParticleSystem.kCCPositionTypeRelative);
			particle.setPosition(240, 400);
		}
			break;

		case 1: {
			//2D骨骼动画的测试，基于cocostudio，暂时还没有将粒子系统合并到此2d骨骼动画中
			Skeleton skeleton = SkeletonFactory.createSkeleton("assets/skeleton.ExportJson");

			//armature和armature2仅在播放速度上有区别，其他没什么区别
			armature = skeleton.buildArmature("skeleton");
			armature.setTextureAtlas(new TextureAtlas("assets/xiaotu.pack"));
			armature.setPosition(240, 400);
			armature.setScale(0.68f, 0.68f);
			armature.animation.gotoAndPlay("fly", -1, -1, false, -1);
			armature.animation.setProcessTimeScale(0.1f);

			armature2 = skeleton.buildArmature("skeleton");
			armature2.setTextureAtlas(new TextureAtlas("assets/xiaotu.pack"));
			armature2.setPosition(240, 200);
			armature2.setScale(0.68f, -0.68f);
			armature2.animation.gotoAndPlay("fly", -1, -1, false, -1);
			armature2.animation.setProcessTimeScale(0.2f);
		}
			break;
		case 2: {
			texture_atlas = TextureAtlasManager.loadTextureAtlas("assets/skill_thunde.plist");
			texture_atlas_sprite = texture_atlas.createSprite("thunder_shandian");
		}
			break;

		case 3: {
			bk = new Texture("assets/icestrom1.png");
			animation = XAnimation.createAnimation("assets/icestrom.xml");
			animation_state = animation.createState();
			animation_state.changeAction("rom");

			animation_actor = new XAnimationActor(animation);
		}
			break;
		}
	}

	private float xx = 0;

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		sprite_batch.setProjectionMatrix(camera.combined);
		sprite_batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

		sprite_batch.begin();

		switch (test_index) {
		case 0: {
			xx += 2;
			if (xx > 480)
				xx = 0;
			if (particle.isCompleted()) {
				particle.stopSystem();
				particle.resetSystem();
			}
			particle.update(Gdx.graphics.getDeltaTime());
			particle.draw(sprite_batch, 1.0f);
		}
			break;
		case 1: {
			armature.act(Gdx.graphics.getDeltaTime());
			armature.draw(sprite_batch, 1.0f);
			armature2.act(Gdx.graphics.getDeltaTime());
			armature2.draw(sprite_batch, 1.0f);
		}
			break;
		case 2:
			texture_atlas_sprite.setPosition(0, 0);
			texture_atlas_sprite.draw(sprite_batch);
			break;

		case 3:
			sprite_batch.draw(bk, 0, 0);
			sprite_batch.draw(background, 0, 0, 100, 0, 100, 100, 0, 100, 0, 0, 1, 1);
			animation_state.tick(((long) (Gdx.graphics.getDeltaTime() * 1000)) * 3);
			animation.render(sprite_batch, 0, 0, 1.0f, 1.0f, animation_state.getActionIndex(),
					animation_state.getFrameId(), false);
			break;
		}

		sprite_batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
