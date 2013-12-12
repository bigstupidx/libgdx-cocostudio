package com.junerking.particle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CCParticleMeteor extends CCParticleSystem {

	public static CCParticleSystem node() {
		return new CCParticleMeteor();
	}

	protected CCParticleMeteor() {
		this(150);
	}

	protected CCParticleMeteor(int p) {
		super(p);

		// duration
		duration = kCCParticleDurationInfinity;

		// Gravity Mode
		emitterMode = kCCParticleModeGravity;

		// Gravity Mode: gravity
		this.setGravity(CGPoint.ccp(-200, 200));

		// Gravity Mode: speed of particles
		setSpeed(15);
		setSpeedVar(5);

		// Gravity Mode: radial
		setRadialAccel(0);
		setRadialAccelVar(0);

		// Gravity Mode: tagential
		setTangentialAccel(0);
		setTangentialAccelVar(0);

		// angle
		angle = 90;
		angleVar = 360;

		posVar = CGPoint.zero();

		// life of particles
		life = 2;
		lifeVar = 1;

		// size, in pixels
		startSize = 60.0f;
		startSizeVar = 10.0f;
		endSize = kCCParticleStartSizeEqualToEndSize;

		// emits per second
		emissionRate = totalParticles / life;

		// color of particles
		startColor.r = 0.2f;
		startColor.g = 0.4f;
		startColor.b = 0.7f;
		startColor.a = 1.0f;
		startColorVar.r = 0.0f;
		startColorVar.g = 0.0f;
		startColorVar.b = 0.2f;
		startColorVar.a = 0.1f;
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
