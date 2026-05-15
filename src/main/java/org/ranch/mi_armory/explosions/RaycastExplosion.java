package org.ranch.mi_armory.explosions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RaycastExplosion {

	private GSPIterator iterator;
	private Level level;
	private Vector3i origin;
	private int strength;
	private int range;

	public boolean castingComplete = false;

	private HashMap<ChunkPos, Set<float[]>> perChunk = new HashMap<>();

	public RaycastExplosion(Level world, Vector3i origin, int strength, int range) {
		this.level = world;
		this.origin = origin;
		this.strength = strength;
		this.range = range;
	}

	private int calculatePointDensity(int radius) {
		return (int) Math.PI * radius * radius * 8;
	}

	private Vector3d sphericalToCartesian(Vector2d point) {
		double x = Math.cos(point.x) * Math.sin(point.y);
		double z = Math.sin(point.x) * Math.sin(point.y);
		double y = Math.cos(point.y);
		return new Vector3d(x, y, z);
	}

	public void castPoints(int amount) {


		if (iterator == null) {
			iterator = new GSPIterator(calculatePointDensity(range));
		}

		for (int i = 0; i < amount; i++) {
			if (!iterator.hasNext()) {
				castingComplete = true;
				return;
			}

			Vector2d sPoint = iterator.next();
			Vector3d dir = sphericalToCartesian(sPoint);

			float res = strength;

			for (int j = 0; j < range; j++) {
				float x = (float) (dir.x * j + origin.x);
				float y = (float) (dir.y * j + origin.y);
				float z = (float) (dir.z * j + origin.z);

				BlockState block = level.getBlockState(new BlockPos((int) x, (int) y, (int) z));
				if (block.getFluidState().isEmpty())
					res -= block.getBlock().getExplosionResistance();

				if (res <= 0) break;
				level.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			}

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
