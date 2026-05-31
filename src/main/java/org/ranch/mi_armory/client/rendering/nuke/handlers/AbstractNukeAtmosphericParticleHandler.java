package org.ranch.mi_armory.client.rendering.nuke.handlers;

import org.joml.Vector3d;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.client.rendering.Cloudlet;
import org.ranch.mi_armory.client.rendering.nuke.EntityNukeEffects;

import java.util.List;
import java.util.Random;

public abstract class AbstractNukeAtmosphericParticleHandler implements NukeParticleHandler {

	private static final int STANDARD = 0;
	private static final int SHOCK = 1;
	private static final int RING = 2;
	private static final int CONDENSATION = 3;

	protected static final double S = 1.5;
	protected static final double CS = 1.5;

	protected double getCoreHeight(int age) {
		return 3.0 + age * (0.15 / S);
	}

	protected double getTorusWidth(int age) {
		return 3.0 + age * (0.05 / S);
	}

	protected double getRollerSize(int age) {
		return getTorusWidth(age) * 0.35;
	}

	protected double getConvectionHeight(int age) {
		return getCoreHeight(age) + getRollerSize(age);
	}

	protected double getHeat(int age) {
		int maxAge = maxAge();
		int maxHeat = (int) (50.0 * CS);
		return maxHeat - Math.pow((double) (maxHeat * age) / Math.max(maxAge, 1), 1.0);
	}

	@Override
	public int maxAge() {
		return (int) (900.0 * S);
	}

	protected double getSimulationSpeed(int age) {
		int maxAge = maxAge();
		int simSlow = maxAge / 4;
		int simStop = maxAge / 2;
		if (age > simStop) return 0.0;
		if (age > simSlow) return 1.0 - (double) (age - simSlow) / (simStop - simSlow);
		return 1.0;
	}

	protected double getGreying(int age) {
		int maxAge = maxAge();
		int greyStart = maxAge * 3 / 4;
		return age > greyStart ? 1.0 + (double) (age - greyStart) / (maxAge - greyStart) : 1.0;
	}

	protected float getGlobalAlpha(int age) {
		int maxAge = maxAge();
		int fadeOut = maxAge * 3 / 4;
		if (age > fadeOut) {
			float fac = (float) (age - fadeOut) / (float) (maxAge - fadeOut);
			return 1.0f - fac;
		}
		return 1.0f;
	}

	protected int getSimGroundHeight(int x, int z, EntityNukeEffects ent) {
		return (int) (ent.level().getChunk((int) (ent.getX() + x) >> 4, (int) (ent.getZ() + z) >> 4).getHeight(
				net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE,
				(int) (ent.getX() + x) & 15, (int) (ent.getZ() + z) & 15
		) - ent.getY());
	}

	protected abstract double getStandardSpawnHeight(EntityNukeEffects ent);

	@Override
	public void updateCloudlets(List<Cloudlet> cloudlets, int age, EntityNukeEffects ent, Random rng) {
		int maxAge = maxAge();
		double simSpeed = getSimulationSpeed(age);
		double coreHeight = getCoreHeight(age);
		double torusWidth = getTorusWidth(age);
		double rollerSize = getRollerSize(age);

		// standard
		double range = (torusWidth - rollerSize) * 0.25;
		int toSpawn = (int) Math.ceil(10.0 * simSpeed * simSpeed);
		int lifetime = Math.min(age * age + 200, maxAge - age + 200);

		for (int i = 0; i < toSpawn; i++) {
			double x = rng.nextGaussian() * range;
			double z = rng.nextGaussian() * range;
			float angle = (float) (rng.nextDouble() * Math.PI * 2);
			float startScale = 1.0f + age * 0.005f * (float) CS;
			float endScale = startScale + 5.0f * (float) CS;

			TorusCloudlet c = (TorusCloudlet) new TorusCloudlet.TorusCloudletBuilder(
					new Vector3d(x, getStandardSpawnHeight(ent), z), new Vector3d())
					.angle(angle)
					.rangeMod(0.3f + rng.nextFloat() * 0.7f)
					.id(STANDARD)
					.maxLife(lifetime)
					.scale(startScale, endScale)
					.build();
			cloudlets.add(c);
		}

		// shockwave
		if (age < 150) {
			int shockLife = Math.max(300 - age * 20, 50);
			int cloudCount = age * 5;

			for (int i = 0; i < cloudCount; i++) {
				float rot = (float) (Math.PI * 2 * rng.nextDouble());
				double dist = MiArmory.speedOfSound(age);
				double sx = Math.cos(rot) * dist;
				double sz = Math.sin(rot) * dist;
				int sy = getSimGroundHeight((int) sx, (int) sz, ent);

				float motMult = age > 15 ? 0.75f : 0.0f;

				TorusCloudlet c = (TorusCloudlet) new TorusCloudlet.TorusCloudletBuilder(
						new Vector3d(sx, sy + 2, sz), new Vector3d())
						.angle(rot)
						.rangeMod(1)
						.id(SHOCK)
						.maxLife(shockLife)
						.scale(7.0f, 7.0f + 2.0f)
						.motionMultiplier(motMult)
						.build();
				cloudlets.add(c);
			}
		}

		// ring
		if (age < 130.0 * S) {
			int ringLife = (int) (lifetime * S);
			float startScale = 1.0f + age * 0.0025f * (float) (CS * CS);
			float endScale = startScale + 3.0f * (float) (CS * CS);

			for (int i = 0; i < 2; i++) {
				float angle = (float) (rng.nextDouble() * Math.PI * 2);

				TorusCloudlet c = (TorusCloudlet) new TorusCloudlet.TorusCloudletBuilder(
						new Vector3d(0, 0 + coreHeight, 0), new Vector3d())
						.angle(angle)
						.rangeMod(0.3f + rng.nextFloat() * 0.7f)
						.id(RING)
						.maxLife(ringLife)
						.scale(startScale, endScale)
						.build();
				cloudlets.add(c);
			}
		}

		// condensation lower
		if (age > (int) (130.0 * S) && age < (int) (600.0 * S)) {
			for (int i = 0; i < 20; i++) {
				for (int j = 0; j < 4; j++) {
					float angle = (float) (Math.PI * 2 * rng.nextDouble());
					double tilt = 0.06981317007977318 * j;
					double armR = torusWidth + rollerSize * (5.0 + rng.nextDouble());
					double cx = Math.cos(angle) * (armR * Math.cos(tilt));
					double cy = coreHeight - 5.0 + j * S + Math.sin(tilt) * armR;
					double cz = Math.sin(angle) * (armR * Math.cos(tilt));
					int condLife = (int) ((20 + age / 10) * (1.0 + rng.nextDouble() * 0.1));
					float startScale = 0.125f * (float) CS;
					float endScale = startScale + 3.0f * (float) CS;

					TorusCloudlet c = (TorusCloudlet) new TorusCloudlet.TorusCloudletBuilder(
							new Vector3d(cx, cy, cz), new Vector3d())
							.angle(angle)
							.id(CONDENSATION)
							.maxLife(condLife)
							.scale(startScale, endScale)
							.build();
					cloudlets.add(c);
				}
			}
		}

		// condensation upper
		if (age > (int) (200.0 * S) && age < (int) (600.0 * S)) {
			for (int i = 0; i < 20; i++) {
				for (int j = 0; j < 4; j++) {
					float angle = (float) (Math.PI * 2 * rng.nextDouble());
					double tilt = 0.06981317007977318 * j;
					double armR = torusWidth + rollerSize * (3.0 + rng.nextDouble() * 0.5);
					double cx = Math.cos(angle) * (armR * Math.cos(tilt));
					double cy = coreHeight + 25.0 + j * CS + Math.sin(tilt) * armR;
					double cz = Math.sin(angle) * (armR * Math.cos(tilt));
					int condLife = (int) ((20 + age / 10) * (1.0 + rng.nextDouble() * 0.1));
					float startScale = 0.125f * (float) CS;
					float endScale = startScale + 3.0f * (float) CS;

					TorusCloudlet c = (TorusCloudlet) new TorusCloudlet.TorusCloudletBuilder(
							new Vector3d(cx, cy, cz), new Vector3d())
							.angle(angle)
							.id(CONDENSATION)
							.maxLife(condLife)
							.scale(startScale, endScale)
							.build();
					cloudlets.add(c);
				}
			}
		}
	}


	@Override
	public List<Cloudlet> updateCloudlet(Cloudlet cloudlet, EntityNukeEffects ent, Random rng) {
		if (!(cloudlet instanceof TorusCloudlet c)) return List.of();

		int age = ent.simAge;
		double coreHeight = getCoreHeight(age);
		double torusWidth = getTorusWidth(age);
		double rollerSize = getRollerSize(age);
		double convectionHeight = getConvectionHeight(age);
		double simSpeed = getSimulationSpeed(age);
		double heat = getHeat(age);

		double dx2d = c.pos.x;
		double dz2d = c.pos.z;
		double flatDist = Math.sqrt(dx2d * dx2d + dz2d * dz2d);
		double simPosX = flatDist;
		double simPosZ = 0;

		Vector3d motion = new Vector3d();

		switch (c.id) {

			case STANDARD -> {
				Vector3d convection = getConvectionMotion(c, simPosX, simPosZ,
						0, 0, 0, coreHeight, torusWidth, rollerSize);
				Vector3d lift = getLiftMotion(c, simPosX,
						0, 0, 0, torusWidth, convectionHeight);
				double factor = Math.clamp(
						(c.pos.y - 0) / coreHeight, 0.0, 1.0);
				motion.set(
						convection.x * factor + lift.x * (1.0 - factor),
						convection.y * factor + lift.y * (1.0 - factor),
						convection.z * factor + lift.z * (1.0 - factor));
			}

			case SHOCK -> {
				double factor = Math.clamp(
						(c.pos.y - 0) / coreHeight, 0.0, 1.0);
				motion.set(
						Math.cos(c.angle) * factor,
						0,
						Math.sin(c.angle) * factor);
			}

			case RING -> {
				motion.set(getRingMotion(c, simPosX, simPosZ,
						0, 0, 0, coreHeight, torusWidth, rollerSize));
			}

			case CONDENSATION -> {
				double speed = 2e-5 * age;
				motion.set(
						(c.pos.x - 0) * speed,
						0,
						(c.pos.z - 0) * speed);
			}
		}

		double mult = c.motionMultiplier * simSpeed;
		c.vel.set(motion.x * mult, motion.y * mult, motion.z * mult);

		updateColor(c, 0, 0, 0, coreHeight, heat, age, ent);


		float alpha = (1.0f - (float) c.life / (float) c.maxLife) * getGlobalAlpha(age);
		if (c.id == CONDENSATION) alpha *= 0.25f;
		c.a = (int) (alpha * 255);

		return List.of();
	}


	private Vector3d getConvectionMotion(
			TorusCloudlet c,
			double simPosX, double simPosZ,
			double ex, double ey, double ez,
			double coreHeight, double torusWidth, double rollerSize) {

		double torusPX = ex + torusWidth;
		double torusPY = ey + coreHeight;
		double torusPZ = ez;

		double dx = torusPX - simPosX;
		double dy = torusPY - c.pos.y;
		double dz = torusPZ - simPosZ;

		double roller = rollerSize * c.rangeMod;

		double len = Math.sqrt(dx * dx + dy * dy + dz * dz);

		double dist = len / roller - 1.0;

		double func = 1.0 - Math.exp(-dist);

		double ang = func * Math.PI * 0.5;

		double rx = -dx / dist;
		double ry = -dy / dist;
		double rz = -dz / dist;

		double[] rotZ = rotateAroundZ(rx, ry, -ang);

		Vector3d motion = new Vector3d(
				torusPX + rotZ[0] - simPosX,
				torusPY + rotZ[1] - c.pos.y,
				torusPZ + rz - simPosZ
		);

		double ml = motion.length();
		if (ml > 1.0E-4) {
			motion.div(ml);
		} else {
			motion.zero();
		}

		return rotateAroundY(motion, c.angle);
	}

	private Vector3d getLiftMotion(TorusCloudlet c,
	                               double simPosX,
	                               double ex, double ey, double ez,
	                               double torusWidth, double convectionHeight) {
		double scale = Math.clamp(1.0 - (simPosX - (ex + torusWidth)), 0.0, 1.0);
		Vector3d motion = new Vector3d(
				ex - c.pos.x,
				ey + convectionHeight - c.pos.y,
				ez - c.pos.z);
		double ml = motion.length();
		if (ml > 0) motion.div(ml);
		motion.mul(scale);
		return motion;
	}

	private Vector3d getRingMotion(TorusCloudlet c,
	                               double simPosX, double simPosZ,
	                               double ex, double ey, double ez,
	                               double coreHeight, double torusWidth, double rollerSize) {
		if (simPosX > ex + torusWidth * 2.0) return new Vector3d();

		double torusPX = ex + torusWidth;
		double torusPY = ey + coreHeight * 0.5;

		double dx = torusPX - simPosX;
		double dy = torusPY - c.pos.y;
		double roller = rollerSize * c.rangeMod * 0.25;
		double len = Math.sqrt(dx * dx + dy * dy);
		double dist = len / roller - 1.0;
		double func = 1.0 - Math.pow(Math.E, -dist);
		double ang = func * Math.PI * 0.5;

		double rx = -dx / len;
		double ry = -dy / len;
		double[] rotZ = rotateAroundZ(rx, ry, ang);

		Vector3d motion = new Vector3d(
				torusPX + rotZ[0] - simPosX,
				torusPY + rotZ[1] - c.pos.y,
				0);
		double speed = 0.001;
		motion.mul(speed);
		double ml = motion.length();
		if (ml > 0) motion.div(ml);
		return rotateAroundY(motion, c.angle);
	}

	protected void updateColor(TorusCloudlet c,
	                           double ex, double ey, double ez,
	                           double coreHeight, double heat,
	                           int age, EntityNukeEffects ent) {
		if (c.id == CONDENSATION) {
			c.r = c.g = c.b = 255;
			return;
		}

		double dx = ex - c.pos.x;
		double dy = (ey + coreHeight) - c.pos.y;
		double dz = ez - c.pos.z;
		double distSq = (dx * dx + dy * dy + dz * dz) / Math.max(heat, 1.0);
		double dist = Math.max(Math.sqrt(distSq), 1.0);
		double col = 2.0 / dist;
		double greying = getGreying(age);
		if (c.id == RING) greying += 1.0;

		double r = Math.max(col * 2.0, 0.25) * greying;
		double g = Math.max(col * 1.5, 0.25) * greying;
		double b = Math.max(col * 0.5, 0.25) * greying;

		c.r = (int) Math.min(r * 255, 255);
		c.g = (int) Math.min(g * 255, 255);
		c.b = (int) Math.min(b * 255, 255);
	}

	private double[] rotateAroundZ(double x, double y, double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return new double[]{x * cos - y * sin, x * sin + y * cos};
	}

	private Vector3d rotateAroundY(Vector3d v, double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return new Vector3d(
				v.x * cos + v.z * sin,
				v.y,
				-v.x * sin + v.z * cos);
	}

	@Override
	public float getFlashSize() {
		return 150;
	}

	@Override
	public int getFlashDuration() {
		return 50;
	}

	public static class TorusCloudlet extends Cloudlet {

		public float angle;
		public float rangeMod;

		public TorusCloudlet(Vector3d pos, Vector3d vel) {
			super(pos, vel);
		}

		public static class TorusCloudletBuilder extends CloudletBuilder {
			private float angle = 0;
			private float rangeMod = 1;

			public TorusCloudletBuilder(Vector3d pos, Vector3d vel) {
				super(pos, vel);
			}

			public TorusCloudletBuilder angle(float angle) {
				this.angle = angle;
				return this;
			}

			public TorusCloudletBuilder rangeMod(float rangeMod) {
				this.rangeMod = rangeMod;
				return this;
			}

			@Override
			public Cloudlet build() {
				TorusCloudlet cloud = new TorusCloudlet(this.pos, this.vel);
				cloud.maxLife = this.maxLife;
				cloud.startingScale = this.startingScale;
				cloud.endingScale = this.endingScale;
				cloud.id = this.id;
				cloud.r = this.r;
				cloud.g = this.g;
				cloud.b = this.b;
				cloud.a = this.a;
				cloud.motionMultiplier = this.motionMultiplier;
				cloud.rangeMod = this.rangeMod;
				cloud.angle = this.angle;
				return cloud;
			}
		}
	}
}