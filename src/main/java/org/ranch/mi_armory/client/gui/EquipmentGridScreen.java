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
import org.ranch.mi_armory.modular_armor.Module;
import org.ranch.mi_armory.modular_armor.ModuleList;
import org.ranch.mi_armory.network.PacketEquipmentGridClick;

public class EquipmentGridScreen extends AbstractContainerScreen<EquipmentGridContainerMenu> {
	public static final ResourceLocation EQUIPMENT_GRID_GUI = MiArmory.location("textures/gui/container/equipment_grid.png");
	public static final ResourceLocation ENTRY_SPRITE = MiArmory.location("container/entry");
	public static final ResourceLocation ENTRY_SLOT = MiArmory.location("textures/gui/container/entry_slot.png");

	private final int TILE_SIZE = 18;
	private int gridX;
	private int gridY;

	public EquipmentGridScreen(EquipmentGridContainerMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		this.imageHeight = 175;
	}

	@Override
	protected void init() {
		super.init();
		gridX = this.leftPos + 52;
		gridY = this.topPos - 2;
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		guiGraphics.blit(EQUIPMENT_GRID_GUI, this.leftPos, this.topPos - 9, 0, 0, this.imageWidth, this.imageHeight);

		EquipmentGrid grid = menu.getEquipmentGrid();
		if (grid != null) {
			renderGrid(grid, guiGraphics, gridX, gridY, TILE_SIZE);
			if (!menu.getCarried().isEmpty() && ModuleList.hasItem(menu.getCarried().getItem())) {
				int[] mpos = mouseToGridPos(mouseX, mouseY, gridX, gridY, TILE_SIZE);
				if (grid.inBounds(mpos[0], mpos[1])) {
					boolean fits = grid.canAdd(new EquipmentGrid.Entry(mpos[0], mpos[1], menu.getCarried().copy()));
					Module m = ModuleList.getFromItem(menu.getCarried().getItem());
					guiGraphics.renderOutline(
							gridX + mpos[0] * TILE_SIZE,
							gridY + mpos[1] * TILE_SIZE,
							m.width * TILE_SIZE,
							m.height * TILE_SIZE,
							fits ? 0x88FFFFFF : 0x88FF4444);
				}
			}
		}
	}

	public void renderGrid(EquipmentGrid grid, GuiGraphics guiGraphics, int x, int y, int tileSize) {

		for (int i = 0; i < grid.width(); i++) {
			for (int j = 0; j < grid.height(); j++) {
				guiGraphics.blit(ENTRY_SLOT, x + i * tileSize, y + j * tileSize, 0, 0, tileSize, tileSize, 18, 18);
			}
		}

		for (EquipmentGrid.Entry entry : grid.modules()) {
			guiGraphics.blitSprite(ENTRY_SPRITE, x + entry.x() * tileSize, y + entry.y() * tileSize, entry.width() * tileSize, entry.height() * tileSize);
			guiGraphics.renderItem(entry.stack(), x + entry.x() * tileSize + entry.width() * tileSize / 2 - 8, y + entry.y() * tileSize + entry.height() * tileSize / 2 - 8);
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
		int[] mpos = mouseToGridPos(x, y, gridX, gridY, TILE_SIZE);
		EquipmentGrid grid = menu.getEquipmentGrid();
		if (grid != null && grid.inBounds(mpos[0], mpos[1])) {
			menu.onGridClick(mpos[0], mpos[1]);
		}
		return super.mouseClicked(x, y, p_97750_);
	}

	@Override
	public boolean mouseReleased(double x, double y, int p_97814_) {
		int[] mpos = mouseToGridPos(x, y, gridX, gridY, TILE_SIZE);
		EquipmentGrid grid = menu.getEquipmentGrid();
		if (grid != null && grid.inBounds(mpos[0], mpos[1])) {
			return true;
		}
		return super.mouseReleased(x, y, p_97814_);
	}

	@Override
	public void render(GuiGraphics p_283479_, int p_283661_, int p_281248_, float p_281886_) {
		super.render(p_283479_, p_283661_, p_281248_, p_281886_);
		this.renderTooltip(p_283479_, p_283661_, p_281248_);
	}
}
