package org.ranch.mi_armory.client.rendering.nuke;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.ranch.mi_armory.MiArmoryConstants;
import org.ranch.mi_armory.MiArmoryEntities;
import org.ranch.mi_armory.client.rendering.Cloudlet;
import org.ranch.mi_armory.client.rendering.nuke.handlers.NukeParticleHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityNukeEffects extends Entity {

	public NukeExplosionType type;
	public int age;
	private Random rng;

	public boolean playedShockSound = false;
	public boolean playedEMISound = false;

	public ArrayList<Cloudlet> cloudlets = new ArrayList();

	public EntityNukeEffects(EntityType<?> entityType, Level level) {
		super(entityType, level);
		age = 0;
		rng = new Random();
		noCulling = true;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return true;
	}

	@Override
	public void tick() {

		if (type == null) {
			remove(RemovalReason.DISCARDED);
			return;
		}

		super.tick();
		NukeParticleHandler handler = type.getHandler();
		if (handler == null) return;
		handler.updateCloudlets(cloudlets, this.age, this, rng);
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

		if (age >= handler.maxAge()) {
			remove(RemovalReason.DISCARDED);
		}

		age++;
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

	public static EntityNukeEffects create(Vec3 pos, Level world, float size) {
		EntityNukeEffects effects = new EntityNukeEffects(MiArmoryEntities.TOREX.get(), world);
		effects.setPos(pos);
		return effects;
	}
}
