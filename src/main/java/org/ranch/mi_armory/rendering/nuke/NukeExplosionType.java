package org.ranch.mi_armory.rendering.nuke;

import org.ranch.mi_armory.rendering.nuke.handlers.*;

public enum NukeExplosionType {
	EXOATMOSPHERIC(new NukeExoatmosphericParticleHandler()),
	ATMOSPHERIC(new NukeAtmosphericParticleHandler()),
	ATMOSPHERIC_STEM(new NukeAtmosphericStemParticleHandler()),
	CRATERING(new NukeCrateringParticleHandler()),
	UNDERWATER(new NukeUnderwaterParticleHandler()),
	UNDERGROUND(new NukeUndergroundParticleHandler());

	private final NukeParticleHandler handler;

	NukeExplosionType(NukeParticleHandler handler) {
		this.handler = handler;
	}

	public NukeParticleHandler getHandler() {
		return handler;
	}
}
