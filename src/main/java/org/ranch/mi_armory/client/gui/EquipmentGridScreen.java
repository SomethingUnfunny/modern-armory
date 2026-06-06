package org.ranch.mi_armory.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.ranch.mi_armory.modular_armor.EquipmentGridContainerMenu;

public class EquipmentGridScreen extends AbstractContainerScreen<EquipmentGridContainerMenu> {
	public EquipmentGridScreen(EquipmentGridContainerMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

	}
}
