package org.ranch.mi_armory;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ranch.mi_armory.explosions.EntityNukeExplosion;

import java.util.function.Supplier;

public class MiArmoryEntities {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MiArmory.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<Entity>> NUKE = ENTITY_TYPES.register(
			"nuke", () -> EntityType.Builder.of(EntityNukeExplosion::new, MobCategory.MISC).sized(1.0F, 1.0F).build("nuke")
	);

	public static void register(IEventBus modEventBus) {
		ENTITY_TYPES.register(modEventBus);
	}
}
