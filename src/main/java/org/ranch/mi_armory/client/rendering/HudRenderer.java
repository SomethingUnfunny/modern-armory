package org.ranch.mi_armory.client.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.ranch.mi_armory.MiArmoryAttachmentTypes;
import org.ranch.mi_armory.MiArmoryAttributes;
import org.ranch.mi_armory.items.ModularArmor;

import static org.ranch.mi_armory.client.MiArmoryClient.MC;

public class HudRenderer {

	private static final int barH = 2;
	private static final int barS = 1;

	private static int barI = 0;
	private static int barStartY = 0;
	private static int barX = 0;

	public static void render(GuiGraphics guiGraphics) {
		Gui gui = MC.gui;
		barI = 0;
		barX = guiGraphics.guiWidth() / 2 - 91;
		barStartY = guiGraphics.guiHeight() - gui.leftHeight - barS;

		for (EquipmentSlot slot : ModularArmor.EQUIPMENT_SLOTS_REVERSED) {
			ItemStack stack = MC.player.getItemBySlot(slot);
			if (stack.getItem() instanceof ModularArmor m) {
				renderBar(guiGraphics, (float) m.getStoredEnergy(stack) / m.getEnergyCapacity(stack), 0xFFFF0000);
			}
		}

		float shield = Minecraft.getInstance().player.getData(MiArmoryAttachmentTypes.ENERGY_SHIELD);
		double maxShield = Minecraft.getInstance().player.getAttributeValue(MiArmoryAttributes.ENERGY_SHIELD);
		if (maxShield > 0)
			renderBar(guiGraphics, (float) (shield / maxShield), 0xFF00C0DD);
	}

	public static void renderBar(GuiGraphics guiGraphics, float fill, int color) {
		fill = Math.clamp(fill, 0, 1);
		int w = (int) (fill * 81);
		barI += barH + barS;
		guiGraphics.fill(barX, barStartY - barI, barX + 81, barStartY - barI + barH, 0xFF000000);
		guiGraphics.fill(barX, barStartY - barI, barX + w, barStartY - barI + barH, color);
	}
}
