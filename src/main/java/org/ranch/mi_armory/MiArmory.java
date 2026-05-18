package org.ranch.mi_armory;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
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
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
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
}
