package org.ranch.mi_armory.client.gui;

import aztech.modern_industrialization.MI;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.modular_armor.EquipmentGrid;
import org.ranch.mi_armory.modular_armor.EquipmentGridContainerMenu;
import org.ranch.mi_armory.network.PacketEquipmentGridClick;

public class EquipmentGridScreen extends AbstractContainerScreen<EquipmentGridContainerMenu> {
	public static final ResourceLocation EQUIPMENT_GRID_GUI = MiArmory.location("textures/gui/container/equipment_grid.png");

	private int selectedModule = -1;

	private final int TILE_SIZE = 16;
	private final int GRID_X = this.leftPos + 60;
	private final int GRID_Y = 20;

	public EquipmentGridScreen(EquipmentGridContainerMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		guiGraphics.blit(EQUIPMENT_GRID_GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

		int[] mpos = mouseToGridPos(mouseX, mouseY, GRID_X, GRID_Y, TILE_SIZE);

		EquipmentGrid grid = menu.getEquipmentGrid();
		if (grid != null) {
			renderGrid(grid, guiGraphics, GRID_X, GRID_Y, TILE_SIZE);
		}
		guiGraphics.renderOutline(GRID_X + mpos[0] * TILE_SIZE, GRID_Y + mpos[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE, 0xFFFF0000);

	}

	public void renderGrid(EquipmentGrid grid, GuiGraphics guiGraphics, int x, int y, int tileSize) {
		for (int i = 0; i < grid.width(); i++) {
			for (int j = 0; j < grid.height(); j++) {
				guiGraphics.renderOutline(x + i * tileSize, y + j * tileSize, tileSize, tileSize, 0xFFFFFFFF);
			}
		}

		for (EquipmentGrid.Entry entry : grid.modules()) {
			guiGraphics.renderOutline(x + entry.x() * tileSize, y + entry.y() * tileSize, entry.module().width() * tileSize, entry.module().height() * tileSize, 0xFF0000FF);
		}
	}

	public int[] mouseToGridPos(double x, double y, int gx, int gy, int tileSize) {
		double rx = x - gx;
		double ry = y - gy;
		int grx = (int) Math.floor(rx / tileSize);
		int gry = (int) Math.floor(ry / tileSize);
		return new int[]{grx, gry};
	}

	@Override
	public boolean mouseClicked(double x, double y, int p_97750_) {
		boolean ret = super.mouseClicked(x, y, p_97750_);
		int[] mpos = mouseToGridPos(x, y, GRID_X, GRID_Y, TILE_SIZE);
		EquipmentGrid grid = menu.getEquipmentGrid();
		if (grid != null) {
			EquipmentGrid.Entry entry = grid.getAtPos(mpos[0], mpos[1]);
			if (entry != null) {
				menu.onGridClick(mpos[0], mpos[1], PacketEquipmentGridClick.ClickType.REMOVE);
			}
		}
		return ret;
	}

	@Override
	public void render(GuiGraphics p_283479_, int p_283661_, int p_281248_, float p_281886_) {
		super.render(p_283479_, p_283661_, p_281248_, p_281886_);
	}
}
