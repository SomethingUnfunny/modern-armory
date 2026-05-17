package org.ranch.mi_armory.rendering.nuke.handlers;

import org.ranch.mi_armory.rendering.EntityNukeEffects;

public class NukeAtmosphericParticleHandler extends AbstractNukeAtmosphericParticleHandler {
	@Override
	protected double getStandardSpawnHeight(EntityNukeEffects ent) {
		return getCoreHeight(ent.age);
	}
}
