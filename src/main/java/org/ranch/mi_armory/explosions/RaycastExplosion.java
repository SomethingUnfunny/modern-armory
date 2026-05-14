package org.ranch.mi_armory.explosions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
	private int shell;
	private int maxShells;

	public boolean castingComplete = false;

	public RaycastExplosion(Level world, Vector3i origin, int strength, int speed, int range) {
		this.level = world;
		this.origin = origin;
		this.strength = strength;
		this.speed = speed;
		this.range = range;
		shell = 0;
		maxShells = 2;
	}

	private int calculatePointDensity(int radius) {
		return (int) (Math.PI * radius * radius);
	}

	private Vector3d sphericalToCartesian(Vector2d point) {
		double x = Math.cos(point.x) * Math.sin(point.y);
		double z = Math.sin(point.x) * Math.sin(point.y);
		double y = Math.cos(point.y);
		return new Vector3d(x, y, z);
	}

	public void castPoints(int amount) {

		if (iterator == null) {
			if (shell == maxShells) {
				castingComplete = true;
				return;
			}
			iterator = new GSPIterator(calculatePointDensity(range));
		}

		System.out.println("casting " + amount + " points");

		for (int i = 0; i < amount; i++) {
			System.out.println(i);
			if (!iterator.hasNext()) {
				iterator = null;
				shell++;
				return;
			}

			Vector2d sPoint = iterator.next();
			Vector3d vec = sphericalToCartesian(sPoint).mul(range * ((shell + 1D) / maxShells));
			Vector3d point = vec.add(origin.x, origin.y, origin.z);

			level.setBlock(BlockPos.containing(point.x, point.y, point.z), Blocks.RED_WOOL.defaultBlockState(), 3);

			// todo cast ray thru every block and save end pos to hash set of every chunk it passed thru
		}
	}

	public void processChunk() {
		// todo go thru chunk pos hash set and delete every block for every end pos ray thing
	}

	public static class GSPIterator implements Iterator<Vector2d> {

		private int i;
		private final int n;

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
			i++;
			return new Vector2d(theta, phi);
		}
	}
}
