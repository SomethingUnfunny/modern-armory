package org.ranch.mi_armory.rendering.nuke.handlers;

import org.ranch.mi_armory.rendering.Cloudlet;
import org.ranch.mi_armory.rendering.EntityNukeEffects;

import java.util.List;
import java.util.Random;

public class NukeCrateringParticleHandler implements NukeParticleHandler {

	@Override
	public void updateCloudlets(List<Cloudlet> cloudlets, int age, EntityNukeEffects ent, Random rng) {

	}

	@Override
	public List<Cloudlet> updateCloudlet(Cloudlet cloudlet, EntityNukeEffects ent, Random rng) {
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
}
