package org.ranch.mi_armory.explosions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3i;
import org.ranch.mi_armory.MiArmoryEntities;

import java.util.ArrayList;

public class EntityNukeExplosion extends EntityChunkloading {
	public int strength;
	public int speed;
	public int range;
	RaycastExplosion explosion;
	private Entity cause;

	public EntityNukeExplosion(EntityType<?> type, Level world) {
		super(type, world);
	}

	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	public void tick() {
		super.tick();
		if (explosion == null) {
			this.explosion = new RaycastExplosion(this.level(), new Vector3i(this.getBlockX(), this.getBlockY(), this.getBlockZ()), 100, 100);
		}

		loadChunk(this.getBlockX() >> 4, this.getBlockZ() >> 4);

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

	public static EntityNukeExplosion create(Level world, int r, double x, double y, double z, Entity cause) {

		EntityNukeExplosion mk5 = new EntityNukeExplosion(MiArmoryEntities.NUKE.get(), world);
		mk5.strength = r;
		mk5.cause = cause;
		mk5.speed = (int)Math.ceil((double)100000.0F / (double)r);
		mk5.setPos(x, y, z);
		mk5.range = r / 2;
		return mk5;
	}
}
