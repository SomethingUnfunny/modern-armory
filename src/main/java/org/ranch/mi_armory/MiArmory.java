package org.ranch.mi_armory;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.ranch.mi_armory.explosions.EntityNukeExplosion;
import org.ranch.mi_armory.rendering.EntityNukeEffects;
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

		NeoForge.EVENT_BUS.register(this);

		// Register our mod's ModConfigSpec so that FML can create and load the config file for us
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		// Some common setup code
	}

	@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			EntityRenderers.register(MiArmoryEntities.NUKE.get(), NoopRenderer::new);
			EntityRenderers.register(MiArmoryEntities.TOREX.get(), EntityNukeEffectsRenderer::new);
		}
	}

	public static void decimate(BlockPos pos, int strength, boolean visuals, Entity cause, ServerLevel level) {
		EntityNukeExplosion explosion = EntityNukeExplosion.create(pos.getBottomCenter(), level, strength, cause);
		level.addFreshEntity(explosion);
		if (visuals) {
			EntityNukeEffects effects = EntityNukeEffects.create(pos.getBottomCenter(), level, strength);
			level.addFreshEntity(effects);
		}
	}
}
