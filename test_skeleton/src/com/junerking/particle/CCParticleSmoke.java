package com.junerking.particle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CCParticleSmoke extends CCParticleSystem {

	public static CCParticleSystem node() {
		return new CCParticleSmoke();
	}

	protected CCParticleSmoke() {
		this(200);
	}

	protected CCParticleSmoke(int p) {
		super(p);

		// duration
		duration = kCCParticleDurationInfinity;

		// Emitter mode: Gravity Mode
		emitterMode = kCCParticleModeGravity;

		// Gravity Mode: gravity
		this.setGravity(CGPoint.zero());

		// Gravity Mode: radial acceleration
		setRadialAccel(0);
		setRadialAccelVar(0);

		// Gravity Mode: speed of particles
		setSpeed(25);
		setSpeedVar(10);

		// angle
		angle = 90;
		angleVar = 5;

		posVar = CGPoint.ccp(20, 0);

		// life of particles
		life = 4;
		lifeVar = 1;

		// size, in pixels
		startSize = 60.0f;
		startSizeVar = 10.0f;
		endSize = kCCParticleStartSizeEqualToEndSize;

		// emits per frame
		emissionRate = totalParticles / life;

		// color of particles
		startColor.r = 0.8f;
		startColor.g = 0.8f;
		startColor.b = 0.8f;
		startColor.a = 1.0f;
		startColorVar.r = 0.02f;
		startColorVar.g = 0.02f;
		startColorVar.b = 0.02f;
		startColorVar.a = 0.0f;
		endColor.r = 0.0f;
		endColor.g = 0.0f;
		endColor.b = 0.0f;
		endColor.a = 1.0f;
		endColorVar.r = 0.0f;
		endColorVar.g = 0.0f;
		endColorVar.b = 0.0f;
		endColorVar.a = 0.0f;

		setTexture(new TextureRegion(new Texture("assets/blocks.png")));
	}

}
