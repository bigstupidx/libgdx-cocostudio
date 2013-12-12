package com.junerking.particle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CCParticleSpiral extends CCParticleSystem {

	public static CCParticleSystem node() {
		return new CCParticleSpiral();
	}

	protected CCParticleSpiral() {
		this(500);
	}

	protected CCParticleSpiral(int p) {
		super(p);

		// duration
		duration = kCCParticleDurationInfinity;

		// Gravity Mode
		emitterMode = kCCParticleModeGravity;

		// Gravity Mode: gravity
		this.setGravity(CGPoint.zero());

		// Gravity Mode: speed of particles
		setSpeed(150);
		setSpeedVar(0);

		// Gravity Mode: radial
		setRadialAccel(-380);
		setRadialAccelVar(0);

		// Gravity Mode: tagential
		setTangentialAccel(45);
		setTangentialAccelVar(0);

		// angle
		angle = 90;
		angleVar = 0;

		posVar = CGPoint.zero();

		// life of particles
		life = 12;
		lifeVar = 0;

		// size, in pixels
		startSize = 20.0f;
		startSizeVar = 0.0f;
		endSize = kCCParticleStartSizeEqualToEndSize;

		// emits per second
		emissionRate = totalParticles / life;

		// color of particles
		startColor.r = 0.5f;
		startColor.g = 0.5f;
		startColor.b = 0.5f;
		startColor.a = 1.0f;
		startColorVar.r = 0.5f;
		startColorVar.g = 0.5f;
		startColorVar.b = 0.5f;
		startColorVar.a = 0.0f;
		endColor.r = 0.5f;
		endColor.g = 0.5f;
		endColor.b = 0.5f;
		endColor.a = 1.0f;
		endColorVar.r = 0.5f;
		endColorVar.g = 0.5f;
		endColorVar.b = 0.5f;
		endColorVar.a = 0.0f;

		setTexture(new TextureRegion(new Texture("assets/blocks.png")));
		setBlendAdditive(false);
	}

}
