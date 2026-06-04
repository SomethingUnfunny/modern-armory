package org.ranch.mi_armory.explosions;

import aztech.modern_industrialization.machines.helper.SteamHelper;
import aztech.modern_industrialization.materials.property.MaterialHardness;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3i;
import org.lwjgl.system.MathUtil;
import org.ranch.mi_armory.*;
import org.ranch.mi_armory.client.rendering.nuke.NukeExplosionType;
import org.ranch.mi_armory.network.PacketDetonation;

import java.util.ArrayList;
import java.util.HashSet;

public class EntityNukeExplosion extends EntityChunkloading {
	public NukeExplosionType type;
	public int strength;
	public int speed;
	public int range;
	RaycastExplosion explosion;
	private Entity cause;

	private final HashSet<Integer> processedShock = new HashSet<>();
	private final HashSet<Integer> processedFlash = new HashSet<>();

	private boolean sentPacket = false;

	private boolean damageDone = false;

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
		}

		if (!damageDone) {
			processEnts(level(), range * 4);
			if (MiArmory.speedOfSound(tickCount) > range * 4) damageDone = true;
		}

		if (explosion.removingComplete && damageDone) {
			this.remove(RemovalReason.DISCARDED);
		}
	}

	private void processEnts(Level world, double radius) {
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		AABB aabb = (new AABB(x, y, z, x, y, z)).inflate(radius);

		for(Entity entity : world.getEntities(null, aabb)) {
			double dist = entity.position().distanceTo(position());
			if (dist > radius) continue;

			double damage = getDamage(dist, radius, strength * 8);

			if ((type == NukeExplosionType.ATMOSPHERIC || type == NukeExplosionType.ATMOSPHERIC_STEM) && !processedFlash.contains(entity.getId())) {
				float exposure = Explosion.getSeenPercent(new Vec3(x, y + 0.5, z), entity);
				DamageSource flashSource = world.damageSources().source(MiArmoryDamageTypes.NUCLEAR_FLASH, cause);
				entity.hurt(flashSource, (float) (damage * exposure));
				processedFlash.add(entity.getId());
			}

			if (dist < MiArmory.speedOfSound(tickCount) && !processedShock.contains(entity.getId())) {
				DamageSource blastSource = world.damageSources().source(MiArmoryDamageTypes.NUCLEAR_SHOCKWAVE, cause);
				double resDamage = damage;
				if (entity instanceof LivingEntity l && l.getAttribute(MiArmoryAttributes.SHOCKWAVE_RESISTANCE) != null) {
					double resistance = l.getAttributeValue(MiArmoryAttributes.SHOCKWAVE_RESISTANCE);
					resDamage *= 1 - resistance;
				}
				entity.hurt(blastSource, (float)resDamage);
				Vec3 pushDir = entity.position().subtract(position()).normalize();
				pushDir = pushDir.normalize().scale(3.0F);
				pushDir = new Vec3(pushDir.x, 1.0F, pushDir.z);
				entity.push(pushDir);
				processedShock.add(entity.getId());
			}
		}
	}

	public double getDamage(double dist, double radius, double damage) {
		double n = (dist / radius) * 10;
		double i = 1/(n*n);
		return i * damage;
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
