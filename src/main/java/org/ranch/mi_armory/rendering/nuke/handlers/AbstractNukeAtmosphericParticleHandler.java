package org.ranch.mi_armory.rendering.nuke.handlers;

import org.joml.Vector3d;
import org.ranch.mi_armory.rendering.Cloudlet;
import org.ranch.mi_armory.rendering.EntityNukeEffects;

import java.util.List;
import java.util.Random;

public class AbstractNukeAtmosphericParticleHandler implements NukeParticleHandler {


	@Override
	public void updateCloudlets(List<Cloudlet> cloudlets, int age, EntityNukeEffects ent, Random rng) {

	}

	@Override
	public List<Cloudlet> updateCloudlet(Cloudlet cloudlet, EntityNukeEffects ent, Random rng) {
		return List.of();
	}

	@Override
	public float getFlashSize() {
		return 15;
	}

	@Override
	public int getFlashDuration() {
		return 20;
	}

	public static class TorusCloudlet extends Cloudlet {

		public float angle;
		public float rangeMod;

		public TorusCloudlet(Vector3d pos, Vector3d vel) {
			super(pos, vel);
		}

		public static class TorusCloudletBuilder extends CloudletBuilder {
			private float angle;
			private float rangeMod;

			public TorusCloudletBuilder(Vector3d pos, Vector3d vel) {
				super(pos, vel);
			}

			public TorusCloudletBuilder setAngle(float angle) {
				this.angle = angle;
				return this;
			}

			public TorusCloudletBuilder setRangeMod(float rangeMod) {
				this.rangeMod = rangeMod;
				return this;
			}

			@Override
			public Cloudlet build() {
				TorusCloudlet cloud = (TorusCloudlet) super.build();
				cloud.angle = angle;
				return cloud;
			}
		}
	}
}