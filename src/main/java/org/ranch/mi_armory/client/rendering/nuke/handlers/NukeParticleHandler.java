package org.ranch.mi_armory.client.rendering.nuke.handlers;

import org.ranch.mi_armory.client.rendering.Cloudlet;
import org.ranch.mi_armory.client.rendering.nuke.EntityNukeEffects;

import java.util.List;
import java.util.Random;

// statelessz
public interface NukeParticleHandler {
	void updateCloudlets(List<Cloudlet> cloudlets, int age, EntityNukeEffects ent, Random rng);

	List<Cloudlet> updateCloudlet(Cloudlet cloudlet, EntityNukeEffects ent, Random rng);

	float getFlashSize();

	int getFlashDuration();

	int maxAge();
}
