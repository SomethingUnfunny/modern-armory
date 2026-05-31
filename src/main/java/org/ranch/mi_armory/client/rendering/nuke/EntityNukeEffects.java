package org.ranch.mi_armory.client.rendering.nuke;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryConstants;
import org.ranch.mi_armory.MiArmoryEntities;
import org.ranch.mi_armory.client.rendering.Cloudlet;
import org.ranch.mi_armory.client.rendering.nuke.handlers.NukeParticleHandler;
import org.ranch.mi_armory.explosions.EntityChunkloading;
import org.ranch.mi_armory.explosions.EntityNukeExplosion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityNukeEffects extends EntityChunkloading {

	private static final EntityDataAccessor<Long> AGE = SynchedEntityData.defineId(EntityNukeEffects.class, EntityDataSerializers.LONG);

	private final int MAX_CATCHUP = 15;

	public NukeExplosionType type;
	public int simAge;
	private Random rng;

	public boolean playedShockSound = false;
	public boolean playedEMISound = false;

	public ArrayList<Cloudlet> cloudlets = new ArrayList();

	public EntityNukeEffects(EntityType<?> entityType, Level level) {
		super(entityType, level);
		simAge = 0;
		rng = new Random();
		noCulling = true;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return true;
	}

	@Override
	public void tick() {

		if (entityData.get(AGE) > 200) playedShockSound = true;

		if (type == null) {
			type = EntityNukeExplosion.getExplosionType(level(), BlockPos.containing(position()));
		}

		loadChunk();

		super.tick();

		for (int i = 0; i < MAX_CATCHUP && !upToDate(); i++) {
			tickSimulation();
		}

		if (simAge >= type.getHandler().maxAge()) {
			remove(RemovalReason.DISCARDED);
		}

		entityData.set(AGE, entityData.get(AGE) + 1);
	}

	public boolean upToDate() {
		return simAge >= entityData.get(AGE);
	}

	private void tickSimulation() {
		if (!level().isClientSide) return;

		NukeParticleHandler handler = type.getHandler();
		handler.updateCloudlets(cloudlets, this.simAge, this, rng);
		List<Cloudlet> toAdd = new ArrayList<>();
		for (Cloudlet cloudlet : cloudlets) {
			toAdd.addAll(handler.updateCloudlet(cloudlet, this, rng));
			cloudlet.vel.mul(cloudlet.motionMultiplier);
			cloudlet.pos.add(cloudlet.vel);
			cloudlet.life += 1;
			if (cloudlet.life >= cloudlet.maxLife) cloudlet.dead = true;
		}

		cloudlets.addAll(toAdd);

		cloudlets.removeIf(cloudlet -> cloudlet.dead);
		simAge++;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(AGE, 0L);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {
		entityData.set(AGE, compoundTag.getLong("age"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {
		compoundTag.putLong("age", entityData.get(AGE));
	}

	public static EntityNukeEffects create(Vec3 pos, Level world, float size) {
		EntityNukeEffects effects = new EntityNukeEffects(MiArmoryEntities.TOREX.get(), world);
		effects.setPos(pos);
		return effects;
	}
}
