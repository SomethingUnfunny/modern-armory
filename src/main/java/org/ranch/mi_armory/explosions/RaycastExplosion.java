package org.ranch.mi_armory.explosions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.*;
import org.ranch.mi_armory.util.GSPIterator;

import java.lang.Math;
import java.util.*;

import static org.ranch.mi_armory.util.UnfunMath.sphericalToCartesian;

public class RaycastExplosion {

	private GSPIterator iterator;
	private Level level;
	private Vector3i origin;
	private int strength;
	private int range;

	public boolean castingComplete = false;
	public boolean removingComplete = false;

	private HashMap<ChunkPos, Set<float[]>> perChunk = new HashMap<>();
	private List<ChunkPos> orderedChunks = new ArrayList<>();
	private CoordComparator comparator;

	public RaycastExplosion(Level world, Vector3i origin, int strength, int range) {
		this.level = world;
		this.origin = origin;
		this.strength = strength;
		this.range = range;
		comparator = new CoordComparator(origin);
	}

	private int calculatePointDensity(int radius) {
		return (int) Math.PI * radius * radius * 10;
	}

	public void castPoints(int amount) {

		if (iterator == null) {
			iterator = new GSPIterator(calculatePointDensity(range));
		}

		System.out.println("casting " + amount + " rays");

		for (int i = 0; i < amount; i++) {
			if (!iterator.hasNext()) {
				castingComplete = true;
				return;
			}

			Vector2d sPoint = iterator.next();
			Vector3d dir = sphericalToCartesian(sPoint);

			float res = strength;

			float[] lastpos = null;
			Set<ChunkPos> traveledChunks = new HashSet<>();

			for (int j = 0; j < range; j++) {
				float x = (float) (dir.x * j + origin.x);
				float y = (float) (dir.y * j + origin.y);
				float z = (float) (dir.z * j + origin.z);

				BlockState block = level.getBlockState(new BlockPos((int) x, (int) y, (int) z));
				if (block.getFluidState().isEmpty())
					res -= block.getBlock().getExplosionResistance();


				if (res <= 0) break;

				if (!block.isAir()) {
					lastpos = new float[]{x, y, z};
					traveledChunks.add(new ChunkPos(((int) x) >> 4, ((int) z) >> 4));
				}

				//level.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			}

			if (lastpos != null) {
				for (ChunkPos chunkPos : traveledChunks) {
					perChunk.computeIfAbsent(chunkPos, k -> new HashSet<>());
					perChunk.get(chunkPos).add(lastpos);
				}
			}
		}
		orderedChunks.clear();
		orderedChunks.addAll(perChunk.keySet());
		orderedChunks.sort(comparator);
	}

	public void processChunk() {
		if (perChunk.isEmpty()) {
			removingComplete = true;
			return;
		}

		ChunkPos current = orderedChunks.get(0);

		System.out.println(orderedChunks.size());

		Set<float[]> tips = perChunk.get(current);

		int enter = Math.min(Math.abs(origin.x - (current.x << 4)), Math.abs(origin.z - (current.z << 4))) - 16;
		enter = Math.max(enter, 0);

		if (tips != null) {
			for (float[] tip : tips) {
				Vector3f vec = new Vector3f(tip[0] - origin.x, tip[1] - origin.y, tip[2] - origin.z);

				boolean inChunk = false;

				for (int i = enter; i < vec.length(); i++) {
					int x = (int) ((vec.x / vec.length()) * i + origin.x);
					int y = (int) ((vec.y / vec.length()) * i + origin.y);
					int z = (int) ((vec.z / vec.length()) * i + origin.z);

					if (x >> 4 == current.x && z >> 4 == current.z) {
						inChunk = true;
						BlockPos blockPos = new BlockPos(x, y, z);

						level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 1);
					} else if (inChunk) {
						break;
					}
				}
			}
		}

		perChunk.remove(current);
		orderedChunks.remove(0);
	}

	public static class CoordComparator implements Comparator<ChunkPos> {

		Vector3i origin;

		public CoordComparator(Vector3i origin) {
			this.origin = origin;
		}

		public int compare(ChunkPos c1, ChunkPos c2) {
			int chunkX = origin.x >> 4;
			int chunkZ = origin.z >> 4;
			int diff1 = Math.abs(chunkX - c1.x) + Math.abs(chunkZ - c1.z);
			int diff2 = Math.abs(chunkX - c2.x) + Math.abs(chunkZ - c2.z);
			return diff1 - diff2;
		}
	}
}
