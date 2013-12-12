package com.junerking.particle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CCParticleFireworks extends CCParticleSystem {

	public static CCParticleSystem node() {
		return new CCParticleFireworks();
	}

	protected CCParticleFireworks() {
		// this(1500);
		this(1500);
	}

	protected CCParticleFireworks(int p) {
		super(p);
		// duration
		duration = kCCParticleDurationInfinity;

		// Gravity Mode
		this.setEmitterMode(kCCParticleModeGravity);

		// Gravity Mode: gravity
		this.setGravity(CGPoint.ccp(0, -90));

		// Gravity Mode:  radial
		setRadialAccel(0);
		setRadialAccelVar(0);

		//  Gravity Mode: speed of particles
		setSpeed(180);
		setSpeedVar(50);

		// emitter position
		setPosition(CGPoint.ccp(240, 400));

		// angle
		angle = 90;
		angleVar = 20;

		// life of particles
		life = 3.5f;
		lifeVar = 1;

		// emits per frame
		emissionRate = totalParticles / life;

		// color of particles
		startColor.r = 0.5f;
		startColor.g = 0.5f;
		startColor.b = 0.5f;
		startColor.a = 1.0f;
		startColorVar.r = 0.5f;
		startColorVar.g = 0.5f;
		startColorVar.b = 0.5f;
		startColorVar.a = 0.1f;
		endColor.r = 0.1f;
		endColor.g = 0.1f;
		endColor.b = 0.1f;
		endColor.a = 0.2f;
		endColorVar.r = 0.1f;
		endColorVar.g = 0.1f;
		endColorVar.b = 0.1f;
		endColorVar.a = 0.2f;

		// size, in pixels
		startSize = 8.0f;
		startSizeVar = 2.0f;
		endSize = kCCParticleStartSizeEqualToEndSize;

		setTexture(new TextureRegion(new Texture("assets/blocks.png")));
	}

}
