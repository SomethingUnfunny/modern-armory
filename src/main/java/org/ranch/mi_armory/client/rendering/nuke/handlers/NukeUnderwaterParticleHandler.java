package org.ranch.mi_armory.client.rendering.nuke.handlers;

import org.joml.Vector2d;
import org.joml.Vector3d;
import org.ranch.mi_armory.client.rendering.Cloudlet;
import org.ranch.mi_armory.client.rendering.nuke.EntityNukeEffects;
import org.ranch.mi_armory.util.GSPIterator;
import org.ranch.mi_armory.util.UnfunMath;

import java.util.List;
import java.util.Random;

public class NukeUnderwaterParticleHandler implements NukeParticleHandler {

	private static final int WATER = 1;
	private static final int MIST = 2;

	private static final float scale = 10;

	private double getFloor(EntityNukeEffects ent) {
		//ent.level().getSeaLevel();
		return 1;
	}

	@Override
	public void updateCloudlets(List<Cloudlet> cloudlets, int age, EntityNukeEffects ent, Random rng) {

		if (age == 0) {
			for (int i = 0; i < 5 * scale; i++) {
				double sx = rng.nextGaussian() * scale / 10;
				double sz = rng.nextGaussian() * scale / 10;
				Vector3d pos = new Vector3d(sx, getFloor(ent), sz);
				Cloudlet c = Cloudlet.of(pos, new Vector3d(0, rng.nextFloat() * 4, 0))
						.color(105, 105, 105, 150)
						.alphaFade(50)
						.scale(5)
						.maxLife(200)
						.id(WATER)
						.build();

				cloudlets.add(c);
			}
		}

		if (age == 0) {
			for (int i = 0; i < 100 * scale; i++) {
				double sx = rng.nextGaussian() * scale;
				double sz = rng.nextGaussian() * scale;
				Vector3d pos = new Vector3d(sx, getFloor(ent), sz);
				double v = UnfunMath.normal(pos.length(), 1 * scale) * scale * 5;
				Cloudlet c = Cloudlet.of(pos, new Vector3d(0, v, 0))
						.color(255, 255, 255, 150)
						.alphaFade(50)
						.scale(5)
						.maxLife(200)
						.id(WATER)
						.build();

				cloudlets.add(c);
			}
		}

		if (age == 30) {
			GSPIterator iterator = new GSPIterator(800);
			while (iterator.hasNext()) {
				Vector2d sPoint = iterator.next();
				Vector3d cPoint = UnfunMath.sphericalToCartesian(sPoint);
				cloudlets.add(Cloudlet.of(new Vector3d(0, scale * 4, 0), cPoint.div(1))
						.color(245, 245, 245, 150)
						.scale(10, 5)
						.id(WATER)
						.alphaFade(10)
						.maxLife(100)
						.build()
				);
			}
		}

		if (age < 200 && age > 30) {
			double sx = rng.nextGaussian() * scale / 2;
			double sz = rng.nextGaussian() * scale / 2;
			double sy = rng.nextGaussian() * scale;
			Vector3d pos = new Vector3d(sx, getFloor(ent) + sy + 10, sz);
			Cloudlet c = Cloudlet.of(pos, new Vector3d(0, 0, 0))
					.color(255, 255, 255, 50)
					.alphaFade(50)
					.scale(10)
					.maxLife(200)
					.id(MIST)
					.build();

			cloudlets.add(c);
		}
	}

	@Override
	public List<Cloudlet> updateCloudlet(Cloudlet cloudlet, EntityNukeEffects ent, Random rng) {
		if (cloudlet.id == WATER) {
			if (cloudlet.pos.y >= getFloor(ent)) {
				cloudlet.vel.sub(0, 0.01 * (cloudlet.pos.y / 10), 0);
			} else {
				cloudlet.vel = new Vector3d(cloudlet.vel.x, 0, cloudlet.vel.z);
				cloudlet.vel.mul(0.5);
			}
		}
		cloudlet.vel.mul(0.99);
		cloudlet.pos = new Vector3d(cloudlet.pos.x, Math.max(cloudlet.pos.y, getFloor(ent)), cloudlet.pos.z);
		return List.of();
	}

	@Override
	public float getFlashSize() {
		return 0;
	}

	@Override
	public int getFlashDuration() {
		return 0;
	}

	@Override
	public int maxAge() {
		return 1000;
	}
}
