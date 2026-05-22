package org.ranch.mi_armory;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.ranch.mi_armory.explosions.EntityNukeExplosion;
import org.ranch.mi_armory.network.PacketDetonation;
import org.ranch.mi_armory.rendering.nuke.EntityNukeEffects;
import org.ranch.mi_armory.rendering.nuke.EntityNukeEffectsRenderer;
import org.slf4j.Logger;

@Mod(MiArmory.MODID)
public class MiArmory {
	public static final String MODID = "mi_armory";
	private static final Logger LOGGER = LogUtils.getLogger();

	public MiArmory(IEventBus modEventBus, ModContainer modContainer) {
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);

		MiArmoryEntities.register(modEventBus);
		MiArmoryItems.register(modEventBus);
		MiArmoryComponents.register(modEventBus);
		MiArmorySounds.register(modEventBus);

		// Register our mod's ModConfigSpec so that FML can create and load the config file for us
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	public static ResourceLocation location(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	public static double speedOfSound(int tick) {
		return (tick * 1.5) * 1.5;
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		// Some common setup code
	}

	@EventBusSubscriber(modid = MODID)
	public static class ModEvents {
		@SubscribeEvent
		public static void register(final RegisterPayloadHandlersEvent event) {
			final PayloadRegistrar registrar = event.registrar(MODID).versioned("1");

			registrar.playToClient(PacketDetonation.TYPE, PacketDetonation.STREAM_CODEC, PacketDetonation::handle);
		}
	}

	@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			EntityRenderers.register(MiArmoryEntities.NUKE.get(), NoopRenderer::new);
			EntityRenderers.register(MiArmoryEntities.TOREX.get(), EntityNukeEffectsRenderer::new);
		}
	}

	public static void decimate(BlockPos pos, int strength, boolean visuals, Entity cause, Level level) {
		if (!level.isClientSide() && level instanceof ServerLevel sLevel) {
			EntityNukeExplosion explosion = EntityNukeExplosion.create(pos.getBottomCenter(), level, strength, cause);
			level.addFreshEntity(explosion);
			explosion.loadChunk();
		}

		if (visuals) {
			EntityNukeEffects effects = EntityNukeEffects.create(pos.getBottomCenter(), level, strength);
			level.addFreshEntity(effects);
		}
	}
}
