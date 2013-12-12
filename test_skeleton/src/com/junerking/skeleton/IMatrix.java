package com.junerking.skeleton;

public class IMatrix {
	public float a, b, c, d, tx, ty;

	public IMatrix() {
		idt();
	}

	public void idt() {
		a = 1;
		b = 0;
		c = 0;
		d = 1;
		tx = 0;
		ty = 0;
	}

	public float det() {
		return a * d - b * c;
	}

	public IMatrix inv() {
		float det = det();
		if (det == 0)
			throw new RuntimeException("Can't invert a singular matrix");

		float inv_det = 1.0f / det;
		float tmp[] = { 0, 0, 0, 0, 0, 0 };

		tmp[0] = d;
		tmp[1] = -c;
		tmp[2] = c * ty - d * tx;
		tmp[3] = -b;
		tmp[4] = a;
		tmp[5] = b * tx - a * ty;

		a = tmp[0] * inv_det;
		b = tmp[3] * inv_det;
		c = tmp[1] * inv_det;
		d = tmp[4] * inv_det;
		tx = tmp[2] * inv_det;
		ty = tmp[5] * inv_det;
		return this;
	}

	public IMatrix mul(IMatrix m) {
		float aa = a * m.a + b * m.c;
		float bb = a * m.b + b * m.d;

		float cc = c * m.a + d * m.c;
		float dd = c * m.b + d * m.d;

		float ttx = tx * m.a + ty * m.c + m.tx;
		float tty = tx * m.b + ty * m.d + m.ty;

		a = aa;
		b = bb;
		c = cc;
		d = dd;
		tx = ttx;
		ty = tty;
		return this;
	}

	public String toString() {
		return "matrix: (a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", tx=" + tx + ", ty=" + ty + ")";
	}
}
