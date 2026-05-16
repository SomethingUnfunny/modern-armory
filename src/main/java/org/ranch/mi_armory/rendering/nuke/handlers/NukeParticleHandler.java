package org.ranch.mi_armory.rendering.nuke.handlers;

import org.ranch.mi_armory.rendering.Cloudlet;
import org.ranch.mi_armory.rendering.EntityNukeEffects;

import java.util.List;
import java.util.Random;

// stateless
public interface NukeParticleHandler {
	void updateCloudlets(List<Cloudlet> cloudlets, int age, EntityNukeEffects ent, Random rng);

	List<Cloudlet> updateCloudlet(Cloudlet cloudlet, EntityNukeEffects ent, Random rng);

	float getFlashSize();

	int getFlashDuration();
}
