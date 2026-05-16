package org.ranch.mi_armory.rendering.nuke;

import org.ranch.mi_armory.rendering.nuke.handlers.AbstractNukeAtmosphericParticleHandler;
import org.ranch.mi_armory.rendering.nuke.handlers.NukeExoatmosphericParticleHandler;
import org.ranch.mi_armory.rendering.nuke.handlers.NukeParticleHandler;

public enum NukeExplosionType {
	EXOATMOSPHERIC(new NukeExoatmosphericParticleHandler()),
	ATMOSPHERIC(null),
	ATMOSPHERIC_STEM(null),
	CRATERING(null),
	UNDERWATER(null),
	UNDERGROUND(null);

	private final NukeParticleHandler handler;

	NukeExplosionType(NukeParticleHandler handler) {
		this.handler = handler;
	}

	public NukeParticleHandler getHandler() {
		return new AbstractNukeAtmosphericParticleHandler(); // hehe
	}
}
