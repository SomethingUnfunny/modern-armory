package org.ranch.mi_armory.explosions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;

public abstract class EntityChunkloading extends Entity {
	private ChunkPos loadedChunk;

	public EntityChunkloading(EntityType<?> type, Level world) {
		super(type, world);
	}

	public void loadChunk(int x, int z) {
		if (!this.level().isClientSide) {
			ChunkPos chunkPos = new ChunkPos(x, z);
			if (this.loadedChunk == null || !this.loadedChunk.equals(chunkPos)) {
				this.loadedChunk = chunkPos;
				this.forceChunk();
			}
		}

	}

	public void clearChunkLoader() {
		if (!this.level().isClientSide && this.loadedChunk != null) {
			ServerLevel world = (ServerLevel)this.level();
			world.getChunkSource().removeRegionTicket(TicketType.FORCED, this.loadedChunk, 3, this.loadedChunk);
			this.loadedChunk = null;
		}

	}

	private void forceChunk() {
		ServerLevel world = (ServerLevel)this.level();
		world.getChunkSource().addRegionTicket(TicketType.FORCED, loadedChunk, 3, this.loadedChunk);
	}

	public void remove(Entity.RemovalReason reason) {
		this.clearChunkLoader();
		super.remove(reason);
	}
}
