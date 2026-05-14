package org.ranch.mi_armory.explosions;

import net.minecraft.world.level.Level;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.Iterator;

public class RaycastExplosion {

	private GSPIterator iterator;
	private Level level;
	private Vector3i origin;
	private int strength;
	private int speed;
	private int range;

	public RaycastExplosion(Level world, Vector3i origin, int strength, int speed, int range) {
		this.level = world;
		this.origin = origin;
		this.strength = strength;
		this.speed = speed;
		this.iterator = new GSPIterator(strength * strength * 7);
	}

	public Vector3d sphericalToCartesian(Vector2d point) {
		double x = Math.cos(point.x) * Math.sin(point.y);
		double z = Math.sin(point.x) * Math.sin(point.y);
		double y = Math.cos(point.y);
		return new Vector3d(x, y, z);
	}

	public void castPoints(int amount) {
		for (int i = 0; i < amount; i++) {
			if (!iterator.hasNext()) break;

			Vector2d sPoint = iterator.next();
			Vector3d point = sphericalToCartesian(sPoint);

			// todo cast ray thru every block and save end pos to hash set of every chunk it passed thru
		}
	}

	public void processChunk() {
		// todo go thru chunk pos hash set and delete every block for every end pos ray thing
	}

	public static class GSPIterator implements Iterator<Vector2d> {

		private int i;
		private int n;

		public GSPIterator(int n) {
			this.n = n;
			this.i = 0;
		}

		@Override
		public boolean hasNext() {
			return i < n;
		}

		@Override
		public Vector2d next() {
			double theta = Math.PI * (1 + Math.sqrt(5)) * i;
			double phi = Math.acos(1 - (double) (2 * i) / n);
			return new Vector2d(theta, phi);
		}
	}
}
