package org.ranch.mi_armory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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
import org.ranch.mi_armory.MiArmoryAttachmentTypes;
import org.ranch.mi_armory.MiArmoryAttributes;
import org.ranch.mi_armory.MiArmoryEntities;
import org.ranch.mi_armory.client.gui.EquipmentGridScreen;
import org.ranch.mi_armory.client.rendering.SkyFlashRenderer;
import org.ranch.mi_armory.client.rendering.nuke.EntityNukeEffectsRenderer;
import org.spongepowered.asm.mixin.Unique;

@EventBusSubscriber(
		value = {Dist.CLIENT},
		modid = MiArmory.MODID
)
public class MiArmoryClient {
	public static final ResourceLocation SHIELD_BAR = MiArmory.location("textures/gui/shield_bar.png");
	public static SkyFlashRenderer skyFlashRenderer;

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
		event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, MiArmory.location("energy_shield"), (guiGraphics, partialTick) -> MiArmoryClient.renderShieldBar(guiGraphics));
	}

	public static void renderShieldBar(GuiGraphics guiGraphics) {
		float shield = Minecraft.getInstance().player.getData(MiArmoryAttachmentTypes.ENERGY_SHIELD);
		double maxShield = Minecraft.getInstance().player.getAttributeValue(MiArmoryAttributes.ENERGY_SHIELD);
		if (maxShield <= 0) return;
		Gui gui = Minecraft.getInstance().gui;
		int w = (int) ((shield / maxShield) * 79);
		int x = guiGraphics.guiWidth() / 2 - 90;
		int y = guiGraphics.guiHeight() - gui.leftHeight - 9;
		guiGraphics.blit(SHIELD_BAR, x - 1, y - 1, 0, 0, 81, 9, 81, 9);

		for (int i = 0; i < w; i++) {
			int color = i % 2 == 0 ? 0xFFFF00FF : 0xFFFF3EFF;
			guiGraphics.fill(x + i, y, x + i + 1, y + 7, color);
		}
	}
}
