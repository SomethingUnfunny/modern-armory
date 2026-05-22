package org.ranch.mi_armory.rendering.nuke.handlers;

import org.ranch.mi_armory.rendering.nuke.EntityNukeEffects;

public class NukeAtmosphericStemParticleHandler extends AbstractNukeAtmosphericParticleHandler {
	@Override
	protected double getStandardSpawnHeight(EntityNukeEffects ent) {
		return getSimGroundHeight(0, 0, ent) - 3;
	}
}
