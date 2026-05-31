package org.ranch.mi_armory.client.rendering.nuke.handlers;


import org.ranch.mi_armory.client.rendering.nuke.EntityNukeEffects;

public class NukeAtmosphericParticleHandler extends AbstractNukeAtmosphericParticleHandler {
	@Override
	protected double getStandardSpawnHeight(EntityNukeEffects ent) {
		return getCoreHeight(ent.simAge);
	}
}
