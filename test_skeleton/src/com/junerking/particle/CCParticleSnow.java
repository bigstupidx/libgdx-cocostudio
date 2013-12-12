package com.junerking.particle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CCParticleSnow extends CCParticleSystem {

	public static CCParticleSystem node() {
		return new CCParticleSnow();
	}

	protected CCParticleSnow() {
		this(700);
	}

	protected CCParticleSnow(int p) {
		super(p);

		// duration
		duration = kCCParticleDurationInfinity;

		// set gravity mode.
		emitterMode = kCCParticleModeGravity;

		// Gravity Mode: gravity
		this.setGravity(CGPoint.ccp(0, -1));

		// Gravity Mode: speed of particles
		setSpeed(5);
		setSpeedVar(1);

		// Gravity Mode: radial
		setRadialAccel(0);
		setRadialAccelVar(1);

		// Gravity mode: tagential
		setTangentialAccel(0);
		setTangentialAccelVar(1);

		posVar = CGPoint.ccp(240, 0);

		// angle
		angle = -90;
		angleVar = 5;

		// life of particles
		life = 45;
		lifeVar = 15;

		// size, in pixels
		startSize = 10.0f;
		startSizeVar = 5.0f;
		endSize = kCCParticleStartSizeEqualToEndSize;

		// emits per second
		emissionRate = 10;

		// color of particles
		startColor.r = 1.0f;
		startColor.g = 1.0f;
		startColor.b = 1.0f;
		startColor.a = 1.0f;
		startColorVar.r = 0.0f;
		startColorVar.g = 0.0f;
		startColorVar.b = 0.0f;
		startColorVar.a = 0.0f;
		endColor.r = 1.0f;
		endColor.g = 1.0f;
		endColor.b = 1.0f;
		endColor.a = 0.0f;
		endColorVar.r = 0.0f;
		endColorVar.g = 0.0f;
		endColorVar.b = 0.0f;
		endColorVar.a = 0.0f;

		setTexture(new TextureRegion(new Texture("assets/blocks.png")));
	}

}
