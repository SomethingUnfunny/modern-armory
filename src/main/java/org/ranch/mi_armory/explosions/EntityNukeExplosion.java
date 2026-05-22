package org.ranch.mi_armory.explosions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3i;
import org.ranch.mi_armory.MiArmoryEntities;
import org.ranch.mi_armory.network.PacketDetonation;

import java.util.ArrayList;

public class EntityNukeExplosion extends EntityChunkloading {
	public int strength;
	public int speed;
	public int range;
	RaycastExplosion explosion;
	private Entity cause;

	private boolean sentPacket = false;

	public EntityNukeExplosion(EntityType<?> type, Level world) {
		super(type, world);
	}

	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	public void tick() {
		super.tick();
		if (explosion == null) {
			this.explosion = new RaycastExplosion(this.level(), new Vector3i(this.getBlockX(), this.getBlockY(), this.getBlockZ()), strength, range);
		}

		if (!this.level().isClientSide() && !sentPacket) {
			PacketDetonation.sendToAllPlayers(new ChunkPos(getBlockX() >> 4, getBlockZ() >> 4), strength, (ServerLevel) level());
			sentPacket = true;
		}

		loadChunk();

		if (!explosion.castingComplete) {
			explosion.castPoints(10000);
		} else if (!explosion.removingComplete) {
			explosion.processChunk();
		} else {
			this.remove(RemovalReason.DISCARDED);
		}
	}

	protected void readAdditionalSaveData(CompoundTag compoundTag) {
	}

	protected void addAdditionalSaveData(CompoundTag compoundTag) {
	}

	public static EntityNukeExplosion create(Vec3 pos, Level world, int r, Entity cause) {
		EntityNukeExplosion mk5 = new EntityNukeExplosion(MiArmoryEntities.NUKE.get(), world);
		mk5.strength = r * 2;
		mk5.cause = cause;
		mk5.speed = (int)(1000000.0 / r);
		mk5.setPos(pos);
		mk5.range = r;
		return mk5;
	}
}
