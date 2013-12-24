package com.junerking.test;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.junerking.particle.CCParticleSystem;
import com.junerking.particle.utils.ParticleManager;
import com.junerking.skeleton.Armature;
import com.junerking.skeleton.Skeleton;
import com.junerking.skeleton.SkeletonFactory;
import com.junerking.skeleton.sdp.XAnimation;
import com.junerking.textureatlas.TextureAtlasManager;
import com.junerking.ui.UIClickListener;
import com.junerking.ui.UIResourcesMgr;
import com.junerking.ui.actor.UIHelper;
import com.junerking.ui.actor.UIWidget;

public class Test implements ApplicationListener, InputProcessor, UIClickListener {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 320;

	private static final int TOTAL_TEST_COUNT = 6;

	private OrthographicCamera camera;
	private SpriteBatch sprite_batch;

	private CCParticleSystem particle;
	private Armature armature, armature2;
	private TextureRegion background;

	private TextureAtlas texture_atlas;
	private Sprite texture_atlas_sprite;

	private XAnimation.State animation_state;
	private XAnimation animation;

	//test_4
	private Stage cocosui_stage;
	private UIWidget cocosui_test;

	private Texture bk;
	private ProgressCircleBar circle_bar;

	private int test_index = 0, test_index_small = 0;

	@Override
	public void create() {
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
		camera.update();

		sprite_batch = new SpriteBatch();
		createTestCase(5);

		Gdx.input.setInputProcessor(this);
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
			Skeleton skeleton = SkeletonFactory.createSkeleton("assets/new.ExportJson");
			//armature和armature2仅在播放速度上有区别，其他没什么区别
			armature = skeleton.buildArmature("new");
			armature.setTextureAtlas(new TextureAtlas("assets/xiaotu.pack"));
			armature.setPosition(240, 400);
			armature.setScale(0.68f, 0.68f);
			armature.animation.gotoAndPlay("Animation1", -1, -1, false, -1);
			armature.animation.setProcessTimeScale(0.1f);

			armature2 = skeleton.buildArmature("new");
			armature2.setTextureAtlas(new TextureAtlas("assets/xiaotu.pack"));
			armature2.setPosition(240, 200);
			armature2.setScale(0.68f, -0.68f);
			armature2.animation.gotoAndPlay("Animation1", -1, -1, true, -1);
			armature2.animation.setProcessTimeScale(1.5f);
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
		}
			break;

		case 4: {
			cocosui_stage = new Stage(WIDTH, HEIGHT, false, sprite_batch, false);
			cocosui_stage.setCamera(camera);
			UIResourcesMgr.getInstance().setResourcesLoader(new ResourcesLoader());
			cocosui_test = UIHelper.createUIWidget("assets/DemoLogin.ExportJson");
//			cocosui_test = UIHelper.createUIWidget("assets/DemoShop.ExportJson");
//			cocosui_test = UIHelper.createUIWidget("assets/DemoHead_UI.ExportJson");
			cocosui_test = UIHelper.createUIWidget("assets/SampleChangeEquip_1.ExportJson");
			cocosui_test.prepare();

//			UIButton login_button = (UIButton) cocosui_test.findWidgetByName("login_Button");
//			login_button.setPosition(0, 0);
//			login_button.setUIClickListener(this);
			cocosui_stage.addActor(cocosui_test);
		}
			break;

		case 5: {
			TextureAtlas atlas = new TextureAtlas("assets/ui.pack");
			circle_bar = new ProgressCircleBar();
			circle_bar.setPosition(240, 160);
			circle_bar.setTextureRegion(atlas.findRegion("btggo"));
		}
			break;
		}
	}

	@Override
	public void click(Actor actor, float x, float y) {
		System.out.println("==== " + actor + " " + x + "  " + y);
	}

	private float xx = 0;

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		sprite_batch.setProjectionMatrix(camera.combined);
		sprite_batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

		switch (test_index) {
		case 0: {
			xx += 2;
			if (xx > 480)
				xx = 0;
			if (particle.isCompleted()) {
				particle.stopSystem();
				particle.resetSystem();
			}
			sprite_batch.begin();
			particle.update(Gdx.graphics.getDeltaTime());
			particle.draw(sprite_batch, 1.0f);
			sprite_batch.end();
		}
			break;
		case 1: {
			sprite_batch.begin();
			armature.act(Gdx.graphics.getDeltaTime());
			armature.draw(sprite_batch, 1.0f);
			armature2.act(Gdx.graphics.getDeltaTime());
			armature2.draw(sprite_batch, 1.0f);
			sprite_batch.end();
		}
			break;
		case 2:
			texture_atlas_sprite.setPosition(0, 0);
			texture_atlas_sprite.draw(sprite_batch);
			break;

		case 3:
			sprite_batch.begin();
			sprite_batch.draw(bk, 0, 0);
			sprite_batch.draw(background, 0, 0, 100, 0, 100, 100, 0, 100, 0, 0, 1, 1);
			animation_state.tick(((long) (Gdx.graphics.getDeltaTime() * 1000)) * 3);
			animation.render(sprite_batch, 0, 0, 1.0f, 1.0f, animation_state.getActionIndex(),
					animation_state.getFrameId(), false);
			sprite_batch.end();
			break;

		case 4:
			cocosui_stage.act(Gdx.graphics.getDeltaTime());
			cocosui_stage.draw();
			break;

		case 5:
			sprite_batch.begin();
			circle_bar.draw(sprite_batch, 1.0f);
			sprite_batch.end();
			break;
		}
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		switch (test_index) {
		case 4: {
			cocosui_stage.touchDown(screenX, screenY, pointer, button);
		}
			break;

		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		switch (test_index) {
		case 4: {
			cocosui_stage.touchUp(screenX, screenY, pointer, button);
		}
			break;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		switch (test_index) {
		case 4: {
			cocosui_stage.touchDragged(screenX, screenY, pointer);
		}
			break;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
