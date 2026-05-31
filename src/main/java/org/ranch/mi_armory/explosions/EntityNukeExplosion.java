package org.ranch.mi_armory.explosions;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3i;
import org.ranch.mi_armory.MiArmoryConstants;
import org.ranch.mi_armory.MiArmoryEntities;
import org.ranch.mi_armory.client.rendering.nuke.NukeExplosionType;
import org.ranch.mi_armory.network.PacketDetonation;

public class EntityNukeExplosion extends EntityChunkloading {
	public NukeExplosionType type;
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

		if (type == null) {
			type = EntityNukeExplosion.getExplosionType(level(), BlockPos.containing(position()));
		}

		if (explosion == null) {
			this.explosion = new RaycastExplosion(this.level(), new Vector3i(this.getBlockX(), this.getBlockY(), this.getBlockZ()), strength, range);
		}

		if (!this.level().isClientSide() && !sentPacket) {
			PacketDetonation.sendToAllPlayers(BlockPos.containing(position()), strength, (ServerLevel) level(), type);
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

	public static NukeExplosionType getExplosionType(Level world, BlockPos pos) {

		if (pos.getY() > MiArmoryConstants.HIGH_ALTITUDE) {
			return NukeExplosionType.EXOATMOSPHERIC;
		}

		int worldHeight = world.getChunk(pos).getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX() & 15, pos.getZ() & 15);
		int oceanFloorHeight = world.getChunk(pos).getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX() & 15, pos.getZ() & 15);
		int altitude = pos.getY() - worldHeight;

		if (worldHeight > oceanFloorHeight && world.isWaterAt(pos)) {
			return NukeExplosionType.UNDERWATER;
		}

		if (altitude < MiArmoryConstants.UNDERGROUND) {
			return NukeExplosionType.UNDERGROUND;
		}

		if (altitude > MiArmoryConstants.UNDERGROUND && altitude < -5) {
			return NukeExplosionType.CRATERING;
		}

		if (altitude > MiArmoryConstants.NO_STEM) {
			return NukeExplosionType.ATMOSPHERIC;
		} else {
			return NukeExplosionType.ATMOSPHERIC_STEM;
		}

	}

	public static EntityNukeExplosion create(Vec3 pos, Level world, int r, Entity cause) {
		EntityNukeExplosion mk5 = new EntityNukeExplosion(MiArmoryEntities.NUKE.get(), world);
		mk5.strength = r * 2;
		mk5.cause = cause;
		mk5.speed = (int) (1000000.0 / r);
		mk5.setPos(pos);
		mk5.range = r;
		return mk5;
	}
}
