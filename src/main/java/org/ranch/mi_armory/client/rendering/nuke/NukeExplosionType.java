package org.ranch.mi_armory.client.rendering.nuke;

import org.ranch.mi_armory.client.rendering.nuke.handlers.*;

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

/*
public enum NukeExplosionType {
	EXOATMOSPHERIC,
	ATMOSPHERIC,
	ATMOSPHERIC_STEM,
	CRATERING,
	UNDERWATER,
	UNDERGROUND;

	public NukeParticleHandler getHandler() {
		return switch (this) {
			case EXOATMOSPHERIC -> new NukeExoatmosphericParticleHandler();
			case ATMOSPHERIC -> new NukeAtmosphericParticleHandler();
			case ATMOSPHERIC_STEM -> new NukeAtmosphericStemParticleHandler();
			case CRATERING -> new NukeCrateringParticleHandler();
			case UNDERWATER -> new NukeUnderwaterParticleHandler();
			case UNDERGROUND -> new NukeUndergroundParticleHandler();
		};
	}
}
 */