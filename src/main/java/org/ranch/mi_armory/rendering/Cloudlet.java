package org.ranch.mi_armory.rendering;

import org.joml.Vector3d;

public class Cloudlet {
	public Vector3d pos;
	public Vector3d vel;
	public int life;
	public int maxLife;
	public float startingScale;
	public float endingScale;
	public float motionMultiplier;

	public int id;

	public int r;
	public int g;
	public int b;
	public int a;

	public int alphaFade;

	public boolean dead;

	public Cloudlet(Vector3d pos, Vector3d vel) {
		this.pos = pos;
		this.vel = vel;
		this.life = 0;
		this.startingScale = 1;
		this.endingScale = 1;
		this.id = -1;
		r = g = b = 255;
		a = 100;
		dead = false;
		maxLife = Integer.MAX_VALUE;
	}

	public double lifeRatio() {
		return life / (double) maxLife;
	}

	public float getScale() {
		return (float) (startingScale + ((endingScale - startingScale) * lifeRatio()));
	}

	public static CloudletBuilder of(Vector3d pos, Vector3d vel) {
		return new CloudletBuilder(pos, vel);
	}

	public static class CloudletBuilder {
		protected final Vector3d pos;
		protected final Vector3d vel;
		protected int maxLife = Integer.MAX_VALUE;
		protected float startingScale = 1;
		protected float endingScale = 1;
		protected float motionMultiplier = 1;

		protected int id = -1;

		protected int r = 255;
		protected int g = 255;
		protected int b = 255;
		protected int a = 100;

		protected int alphaFade = 0;

		public CloudletBuilder(Vector3d pos, Vector3d vel) {
			this.pos = pos;
			this.vel = vel;
		}

		public CloudletBuilder maxLife(int maxLife) {
			this.maxLife = maxLife;
			return this;
		}

		public CloudletBuilder scale(float scale) {
			this.startingScale = scale;
			this.endingScale = scale;
			return this;
		}

		public CloudletBuilder scale(float start, float end) {
			this.startingScale = start;
			this.endingScale = end;
			return this;
		}

		public CloudletBuilder id(int id) {
			this.id = id;
			return this;
		}

		public CloudletBuilder color(int r, int g, int b, int a) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
			return this;
		}

		public CloudletBuilder alphaFade(int alphaFade) {
			this.alphaFade = alphaFade;
			return this;
		}

		public CloudletBuilder motionMultiplier(float motionMultiplier) {
			this.motionMultiplier = motionMultiplier;
			return this;
		}

		public Cloudlet build() {
			Cloudlet cloud = new Cloudlet(pos, vel);
			cloud.maxLife = maxLife;
			cloud.startingScale = startingScale;
			cloud.endingScale = endingScale;
			cloud.id = id;
			cloud.r = r;
			cloud.g = g;
			cloud.b = b;
			cloud.a = a;
			cloud.alphaFade = alphaFade;
			cloud.motionMultiplier = motionMultiplier;
			return cloud;
		}
	}
}
