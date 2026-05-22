package org.ranch.mi_armory.explosions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public abstract class EntityChunkloading extends Entity {
	private ChunkPos loadedChunk;

	public EntityChunkloading(EntityType<?> type, Level world) {
		super(type, world);
	}

	public void loadChunk() {
		loadChunk(getBlockX() >> 4, getBlockZ() >> 4);
	}

	public void loadChunk(int x, int z) {
		if (!this.level().isClientSide) {
			ChunkPos chunkPos = new ChunkPos(x, z);

			if (this.loadedChunk == null) this.loadedChunk = chunkPos;

			if (!this.loadedChunk.equals(chunkPos)) {
				clearChunkLoader();
				this.loadedChunk = chunkPos;
			}

			this.forceChunk();
		}
	}

	public void clearChunkLoader() {
		if (!this.level().isClientSide && this.loadedChunk != null) {
			ServerLevel world = (ServerLevel) this.level();
			world.setChunkForced(loadedChunk.x, loadedChunk.z, false);
			this.loadedChunk = null;
		}
	}

	private void forceChunk() {
		ServerLevel world = (ServerLevel) this.level();
		world.setChunkForced(loadedChunk.x, loadedChunk.z, true);
	}

	public void remove(Entity.RemovalReason reason) {
		this.clearChunkLoader();
		super.remove(reason);
	}
}
