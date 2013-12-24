package com.junerking.test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ProgressCircleBar extends Actor {
	public static final int TYPE_COUNT_DOWN = 0;
	public static final int TYPE_PROGRESS = 1;

	protected TextureRegion progress_texture;
	protected int type, percent;

	public ProgressCircleBar() {
	}

	public void setTextureRegion(TextureRegion region) {
		this.progress_texture = region;
		if (region != null) {
			region_center_u = (region.getU() + region.getU2()) / 2;
			region_center_v = (region.getV() + region.getV2()) / 2;

			region_u_hwidth = (region.getU2() - region.getU()) / 2;
			region_v_hheight = (region.getV2() - region.getV()) / 2;

			region_hwidth = region.getRegionWidth() / 2;
			region_hheight = region.getRegionHeight() / 2;
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setProgress(int percent) {
		this.percent = percent;
	}

	protected float fcolor = color.toFloatBits();
	protected float region_center_u, region_center_v, region_u_hwidth, region_v_hheight, region_hwidth, region_hheight;
	protected float[] vertices = new float[5 * 20];

	@Override
	public void draw(SpriteBatch sprite_batch, float parent_alpha) {
		if (progress_texture == null)
			return;
		int rotation = (int) ((1.0f * percent / 100) * 360);
		percent = (percent + 1) % 100;
		if (type == TYPE_COUNT_DOWN) {
			if (rotation > 315) {
				rotation = 360 - rotation;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float xx = tandeg * region_hheight;
				float pp = xx / region_hwidth;
				setVertices(0, x, y, region_center_u, (region_center_v));
				setVertices(1, x - xx, y + region_hheight, region_center_u - region_u_hwidth * pp,
						(region_center_v - region_v_hheight));
				setVertices(2, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(3, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 4);

			} else if (rotation > 270) {
				setVertices(0, x, y, region_center_u, (region_center_v));
				setVertices(2, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(1, x - region_hwidth, y + region_hheight, region_center_u - region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(3, x, y, region_center_u, (region_center_v));

				rotation = rotation - 270;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hwidth;
				float pp = yy / region_hheight;
				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(6, x - region_hwidth, y + region_hheight, region_center_u - region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(5, x - region_hwidth, y + yy, region_center_u - region_u_hwidth, (region_center_v - pp
						* region_v_hheight));
				setVertices(7, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 8);

			} else if (rotation > 225) {
				setVertices(0, x, y, region_center_u, (region_center_v));
				setVertices(3, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x - region_hwidth, y + region_hheight, region_center_u - region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(1, x - region_hwidth, y, region_center_u - region_u_hwidth, (region_center_v));

				rotation = 270 - rotation;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hwidth;
				float pp = yy / region_hheight;
				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(6, x - region_hwidth, y, region_center_u - region_u_hwidth, (region_center_v));
				setVertices(5, x - region_hwidth, y - yy, region_center_u - region_u_hwidth, (region_center_v + pp
						* region_v_hheight));
				setVertices(7, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 8);

			} else if (rotation > 180) {
				setVertices(0, x, y, region_center_u, (region_center_v));
				setVertices(3, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x - region_hwidth, y + region_hheight, region_center_u - region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(1, x - region_hwidth, y, region_center_u - region_u_hwidth, (region_center_v));

				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(6, x - region_hwidth, y, region_center_u - region_u_hwidth, (region_center_v));
				setVertices(5, x - region_hwidth, y - region_hheight, region_center_u - region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(7, x, y, region_center_u, (region_center_v));

				rotation = rotation - 180;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hheight;
				float pp = yy / region_hwidth;
				setVertices(8, x, y, region_center_u, (region_center_v));
				setVertices(10, x - region_hwidth, y - region_hheight, region_center_u - region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(9, x - yy, y - region_hheight, region_center_u - region_u_hwidth * pp,
						(region_center_v + region_v_hheight));
				setVertices(11, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 12);

			} else if (rotation > 135) {
				setVertices(0, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(3, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x - region_hwidth, y + region_hheight, region_center_u - region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(1, x - region_hwidth, y - region_hheight, region_center_u - region_u_hwidth,
						(region_center_v + region_v_hheight));

				rotation = 180 - rotation;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hheight;
				float pp = yy / region_hwidth;
				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(6, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(5, x + yy, y - region_hheight, region_center_u + region_u_hwidth * pp,
						(region_center_v + region_v_hheight));
				setVertices(7, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 8);

			} else if (rotation > 90) {
				setVertices(0, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(3, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x - region_hwidth, y + region_hheight, region_center_u - region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(1, x - region_hwidth, y - region_hheight, region_center_u - region_u_hwidth,
						(region_center_v + region_v_hheight));

				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(6, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(5, x + region_hwidth, y - region_hheight, region_center_u + region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(7, x, y, region_center_u, (region_center_v));

				rotation = rotation - 90;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hwidth;
				float pp = yy / region_hheight;
				setVertices(8, x, y, region_center_u, (region_center_v));
				setVertices(10, x + region_hwidth, y - region_hheight, region_center_u + region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(9, x + region_hwidth, y - yy, region_center_u + region_u_hwidth, (region_center_v + pp
						* region_v_hheight));
				setVertices(11, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 12);

			} else if (rotation > 45) {
				setVertices(0, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(3, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x - region_hwidth, y + region_hheight, region_center_u - region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(1, x - region_hwidth, y - region_hheight, region_center_u - region_u_hwidth,
						(region_center_v + region_v_hheight));

				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(7, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(6, x + region_hwidth, y - region_hheight, region_center_u + region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(5, x + region_hwidth, y, region_center_u + region_u_hwidth, (region_center_v));

				rotation = 90 - rotation;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hwidth;
				float pp = yy / region_hheight;
				setVertices(8, x, y, region_center_u, (region_center_v));
				setVertices(10, x + region_hwidth, y, region_center_u + region_u_hwidth, (region_center_v));
				setVertices(9, x + region_hwidth, y + yy, region_center_u + region_u_hwidth, (region_center_v - pp
						* region_v_hheight));
				setVertices(11, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 12);

			} else {
				setVertices(0, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(3, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x - region_hwidth, y + region_hheight, region_center_u - region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(1, x - region_hwidth, y - region_hheight, region_center_u - region_u_hwidth,
						(region_center_v + region_v_hheight));

				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(7, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(6, x + region_hwidth, y - region_hheight, region_center_u + region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(5, x + region_hwidth, y, region_center_u + region_u_hwidth, (region_center_v));

				setVertices(8, x, y, region_center_u, (region_center_v));
				setVertices(10, x + region_hwidth, y, region_center_u + region_u_hwidth, (region_center_v));
				setVertices(9, x + region_hwidth, y + region_hheight, region_center_u + region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(11, x, y, region_center_u, (region_center_v));

				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hheight;
				float pp = yy / region_hwidth;
				setVertices(12, x, y, region_center_u, (region_center_v));
				setVertices(14, x + region_hwidth, y + region_hheight, region_center_u + region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(13, x + yy, y + region_hheight, region_center_u + pp * region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(15, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 16);
			}
		} else {
			if (rotation <= 45) {
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);

				float xx = tandeg * region_hheight;
				float pp = xx / region_hwidth;
				setVertices(0, x, y, region_center_u, (region_center_v));
				setVertices(1, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x + xx, y + region_hheight, region_center_u + region_u_hwidth * pp,
						(region_center_v - region_v_hheight));
				setVertices(3, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 4);

			} else if (rotation <= 90) {
				setVertices(0, x, y, region_center_u, (region_center_v));
				setVertices(1, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x + region_hwidth, y + region_hheight, region_center_u + region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(3, x, y, region_center_u, (region_center_v));

				rotation = 90 - rotation;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hwidth;
				float pp = yy / region_hheight;
				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(5, x + region_hwidth, y + region_hheight, region_center_u + region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(6, x + region_hwidth, y + yy, region_center_u + region_u_hwidth, (region_center_v - pp
						* region_v_hheight));
				setVertices(7, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 8);

			} else if (rotation < 135) {
				setVertices(0, x, y, region_center_u, (region_center_v));
				setVertices(1, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x + region_hwidth, y + region_hheight, region_center_u + region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(3, x + region_hwidth, y, region_center_u + region_u_hwidth, (region_center_v));

				rotation = rotation - 90;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hwidth;
				float pp = yy / region_hheight;
				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(5, x + region_hwidth, y, region_center_u + region_u_hwidth, (region_center_v));
				setVertices(6, x + region_hwidth, y - yy, region_center_u + region_u_hwidth, (region_center_v + pp
						* region_v_hheight));
				setVertices(7, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 8);

			} else if (rotation < 180) {
				setVertices(0, x, y, region_center_u, (region_center_v));
				setVertices(1, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x + region_hwidth, y + region_hheight, region_center_u + region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(3, x + region_hwidth, y, region_center_u + region_u_hwidth, (region_center_v));

				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(5, x + region_hwidth, y, region_center_u + region_u_hwidth, (region_center_v));
				setVertices(6, x + region_hwidth, y - region_hheight, region_center_u + region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(7, x, y, region_center_u, (region_center_v));

				rotation = 180 - rotation;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hheight;
				float pp = yy / region_hwidth;
				setVertices(8, x, y, region_center_u, (region_center_v));
				setVertices(9, x + region_hwidth, y - region_hheight, region_center_u + region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(10, x + yy, y - region_hheight, region_center_u + region_u_hwidth * pp,
						(region_center_v + region_v_hheight));
				setVertices(11, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 12);

			} else if (rotation < 225) {
				setVertices(0, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(1, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x + region_hwidth, y + region_hheight, region_center_u + region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(3, x + region_hwidth, y - region_hheight, region_center_u + region_u_hwidth,
						(region_center_v + region_v_hheight));

				rotation = rotation - 180;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hheight;
				float pp = yy / region_hwidth;
				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(5, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(6, x - yy, y - region_hheight, region_center_u - region_u_hwidth * pp,
						(region_center_v + region_v_hheight));
				setVertices(7, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 8);

			} else if (rotation < 270) {
				setVertices(0, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(1, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x + region_hwidth, y + region_hheight, region_center_u + region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(3, x + region_hwidth, y - region_hheight, region_center_u + region_u_hwidth,
						(region_center_v + region_v_hheight));

				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(5, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(6, x - region_hwidth, y - region_hheight, region_center_u - region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(7, x, y, region_center_u, (region_center_v));

				rotation = 270 - rotation;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hwidth;
				float pp = yy / region_hheight;
				setVertices(8, x, y, region_center_u, (region_center_v));
				setVertices(9, x - region_hwidth, y - region_hheight, region_center_u - region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(10, x - region_hwidth, y - yy, region_center_u - region_u_hwidth, (region_center_v + pp
						* region_v_hheight));
				setVertices(11, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 12);

			} else if (rotation < 315) {
				setVertices(0, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(1, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x + region_hwidth, y + region_hheight, region_center_u + region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(3, x + region_hwidth, y - region_hheight, region_center_u + region_u_hwidth,
						(region_center_v + region_v_hheight));

				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(5, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(6, x - region_hwidth, y - region_hheight, region_center_u - region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(7, x - region_hwidth, y, region_center_u - region_u_hwidth, (region_center_v));

				rotation = rotation - 270;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hwidth;
				float pp = yy / region_hheight;
				setVertices(8, x, y, region_center_u, (region_center_v));
				setVertices(9, x - region_hwidth, y, region_center_u - region_u_hwidth, (region_center_v));
				setVertices(10, x - region_hwidth, y + yy, region_center_u - region_u_hwidth, (region_center_v - pp
						* region_v_hheight));
				setVertices(11, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 12);

			} else {
				setVertices(0, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(1, x, y + region_hheight, region_center_u, (region_center_v - region_v_hheight));
				setVertices(2, x + region_hwidth, y + region_hheight, region_center_u + region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(3, x + region_hwidth, y - region_hheight, region_center_u + region_u_hwidth,
						(region_center_v + region_v_hheight));

				setVertices(4, x, y, region_center_u, (region_center_v));
				setVertices(5, x, y - region_hheight, region_center_u, (region_center_v + region_v_hheight));
				setVertices(6, x - region_hwidth, y - region_hheight, region_center_u - region_u_hwidth,
						(region_center_v + region_v_hheight));
				setVertices(7, x - region_hwidth, y, region_center_u - region_u_hwidth, (region_center_v));

				setVertices(8, x, y, region_center_u, (region_center_v));
				setVertices(9, x - region_hwidth, y, region_center_u - region_u_hwidth, (region_center_v));
				setVertices(10, x - region_hwidth, y + region_hheight, region_center_u - region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(11, x, y, region_center_u, (region_center_v));

				rotation = 360 - rotation;
				float tandeg = MathUtils.sinDeg(rotation) / MathUtils.cosDeg(rotation);
				float yy = tandeg * region_hheight;
				float pp = yy / region_hwidth;
				setVertices(12, x, y, region_center_u, (region_center_v));
				setVertices(13, x - region_hwidth, y + region_hheight, region_center_u - region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(14, x - yy, y + region_hheight, region_center_u - pp * region_u_hwidth,
						(region_center_v - region_v_hheight));
				setVertices(15, x, y, region_center_u, (region_center_v));
				sprite_batch.draw(progress_texture.getTexture(), vertices, 0, 5 * 16);
			}
		}
	}

	private void setVertices(int index, float x, float y, float u, float v) {
		vertices[index * 5 + 0] = x;
		vertices[index * 5 + 1] = y;
		vertices[index * 5 + 2] = fcolor;
		vertices[index * 5 + 3] = u;
		vertices[index * 5 + 4] = v;
	}
}
