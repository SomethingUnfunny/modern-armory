package org.ranch.mi_armory.client.rendering.nuke.handlers;

import org.joml.Vector2d;
import org.joml.Vector3d;
import org.ranch.mi_armory.client.rendering.Cloudlet;
import org.ranch.mi_armory.client.rendering.nuke.EntityNukeEffects;
import org.ranch.mi_armory.util.GSPIterator;
import org.ranch.mi_armory.util.UnfunMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NukeExoatmosphericParticleHandler implements NukeParticleHandler {

	@Override
	public void updateCloudlets(List<Cloudlet> cloudlets, int age, EntityNukeEffects ent, Random rng) {
		if (age == 1) {
			GSPIterator iterator = new GSPIterator(200);
			while (iterator.hasNext()) {
				Vector2d sPoint = iterator.next();
				Vector3d cPoint = UnfunMath.sphericalToCartesian(sPoint);
				cloudlets.add(
						Cloudlet.of(new Vector3d(), cPoint)
								.scale(4)
								.maxLife(600)
								.alphaFade(100)
								.build()
				);
			}
		}
	}

	@Override
	public List<Cloudlet> updateCloudlet(Cloudlet cloudlet, EntityNukeEffects ent, Random rng) {
		cloudlet.vel.mul(0.9 + (rng.nextFloat() - 0.5) * 0.1);
		return new ArrayList<>();
	}

	@Override
	public float getFlashSize() {
		return 100;
	}

	@Override
	public int getFlashDuration() {
		return 40;
	}

	@Override
	public int maxAge() {
		return 600;
	}
}
