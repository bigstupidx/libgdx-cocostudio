package com.junerking.particle;

public class ccMacros {
	public static final float FLT_EPSILON = 0.000001f;
	public static final int INT_MIN = -2147483648;
	public static final int CC_MAX_PARTICLE_SIZE = 64;

	/// java doesn't support swap primitive types.
	/// public static void CC_SWAP(T x, T y);

	/**
	 * @def CCRANDOM_MINUS1_1 returns a random float between -1 and 1
	 */
	public static final float CCRANDOM_MINUS1_1() {
		return (float) Math.random() * 2.0f - 1.0f;
	}

	/**
	 * @def CCRANDOM_0_1 returns a random float between 0 and 1
	 */
	public static final float CCRANDOM_0_1() {
		return (float) Math.random();
	}

	/**
	 * @def M_PI_2 Math.PI divided by 2
	 */
	public static final float M_PI_2 = (float) (Math.PI / 2);

	/**
	 * @def CC_DEGREES_TO_RADIANS converts degrees to radians
	 */
	public static final float CC_DEGREES_TO_RADIANS(float angle) {
		return (angle / 180.0f * (float) Math.PI);
	}

	/**
	 * @def CC_RADIANS_TO_DEGREES converts radians to degrees
	 */
	public static final float CC_RADIANS_TO_DEGREES(float angle) {
		return (angle / (float) Math.PI * 180.0f);
	}
}
