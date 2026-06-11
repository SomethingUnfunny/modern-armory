package org.ranch.mi_armory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryEntities;
import org.ranch.mi_armory.client.gui.EquipmentGridScreen;
import org.ranch.mi_armory.client.rendering.HudRenderer;
import org.ranch.mi_armory.client.rendering.SkyFlashRenderer;
import org.ranch.mi_armory.client.rendering.nuke.EntityNukeEffectsRenderer;

@EventBusSubscriber(
		value = {Dist.CLIENT},
		modid = MiArmory.MODID
)
public class MiArmoryClient {
	public static final ResourceLocation SHIELD_BAR = MiArmory.location("textures/gui/shield_bar.png");
	public static final ResourceLocation ENERGY_BAR = MiArmory.location("textures/gui/energy_bar.png");
	public static SkyFlashRenderer skyFlashRenderer;
	public static final Minecraft MC = Minecraft.getInstance();

	@SubscribeEvent
	private static void init(FMLConstructModEvent event) {
		skyFlashRenderer = new SkyFlashRenderer();
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		EntityRenderers.register(MiArmoryEntities.NUKE.get(), NoopRenderer::new);
		EntityRenderers.register(MiArmoryEntities.TOREX.get(), EntityNukeEffectsRenderer::new);
	}

	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(MiArmory.EQUIPMENT_GRID_MENU.get(), EquipmentGridScreen::new);
	}

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, MiArmory.location("armor_hud"), (guiGraphics, partialTick) -> MiArmoryClient.renderHud(guiGraphics));
	}

	public static void renderHud(GuiGraphics guiGraphics) {
		HudRenderer.render(guiGraphics);

	}
}
