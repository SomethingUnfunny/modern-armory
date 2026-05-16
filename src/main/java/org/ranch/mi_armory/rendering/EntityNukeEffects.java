package org.ranch.mi_armory.rendering;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.ranch.mi_armory.MiArmoryConstants;

import java.util.ArrayList;

public class EntityNukeEffects extends Entity {

	public ExplosionType type;

	public ArrayList<Cloudlet> cloudlets = new ArrayList();

	public EntityNukeEffects(EntityType<?> entityType, Level level) {
		super(entityType, level);
		type = getExplosionType(level, getOnPos());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {

	}

	private ExplosionType getExplosionType(Level world, BlockPos pos) {

		if (pos.getY() > MiArmoryConstants.HIGH_ALTITUDE) {
			return ExplosionType.EXOATMOSPHERIC;
		}

		int worldHeight = world.getChunk(pos).getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX() & 15, pos.getZ() & 15);
		int oceanFloorHeight = world.getChunk(pos).getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX() & 15, pos.getZ() & 15);
		int altitude = pos.getY() - worldHeight;

		if (worldHeight > oceanFloorHeight && world.isWaterAt(pos)) {
			return ExplosionType.UNDERWATER;
		}

		if (altitude < MiArmoryConstants.UNDERGROUND) {
			return ExplosionType.UNDERGROUND;
		}

		if (altitude > MiArmoryConstants.UNDERGROUND && altitude < -5) {
			return ExplosionType.CRATERING;
		}

		if (pos.getY() - worldHeight > MiArmoryConstants.NO_STEM) {
			return ExplosionType.ATMOSPHERIC;
		} else {
			return ExplosionType.ATMOSPHERIC_STEM;
		}

	}

	public enum ExplosionType {
		EXOATMOSPHERIC,
		ATMOSPHERIC,
		ATMOSPHERIC_STEM,
		CRATERING,
		UNDERWATER,
		UNDERGROUND
	}
}
