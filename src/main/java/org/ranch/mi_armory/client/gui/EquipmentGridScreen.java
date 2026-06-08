package org.ranch.mi_armory.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.modular_armor.EquipmentGrid;
import org.ranch.mi_armory.modular_armor.EquipmentGridContainerMenu;
import org.ranch.mi_armory.modular_armor.Module;
import org.ranch.mi_armory.modular_armor.ModuleList;
import org.ranch.mi_armory.modular_armor.custom_modules.NullModule;

public class EquipmentGridScreen extends AbstractContainerScreen<EquipmentGridContainerMenu> {
	public static final ResourceLocation EQUIPMENT_GRID_GUI = MiArmory.location("textures/gui/container/equipment_grid.png");
	public static final ResourceLocation ENTRY_SPRITE = MiArmory.location("container/entry");
	public static final ResourceLocation ENTRY_SLOT = MiArmory.location("textures/gui/container/entry_slot.png");

	private final int TILE_SIZE = 18;
	private final int GRID_GUI_SIZE = 4;
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
			guiGraphics.enableScissor(gridX, gridY, gridX + TILE_SIZE * GRID_GUI_SIZE, gridY + TILE_SIZE * GRID_GUI_SIZE);
			PoseStack pose = guiGraphics.pose();
			pose.pushPose();
			pose.translate(gridX, gridY, 0);
			pose.scale((float) GRID_GUI_SIZE / grid.width(), (float) GRID_GUI_SIZE / grid.height(), 1);
			renderGrid(grid, guiGraphics, 0, 0, TILE_SIZE);
			if (!menu.getCarried().isEmpty() && ModuleList.hasItem(menu.getCarried().getItem())) {
				int[] mpos = mouseToGridPos(mouseX, mouseY, gridX, gridY, TILE_SIZE, ModuleList.getFromItem(menu.getCarried().getItem()).width, ModuleList.getFromItem(menu.getCarried().getItem()).height, grid);
				if (grid.inBounds(mpos[0], mpos[1])) {
					boolean fits = grid.canAdd(new EquipmentGrid.Entry(mpos[0], mpos[1], menu.getCarried().copy()));
					Module m = ModuleList.getFromItem(menu.getCarried().getItem());
					guiGraphics.renderOutline(
							mpos[0] * TILE_SIZE,
							mpos[1] * TILE_SIZE,
							m.width * TILE_SIZE,
							m.height * TILE_SIZE,
							fits ? 0x88FFFFFF : 0x88FF4444);
				}
			}
			pose.popPose();
			guiGraphics.disableScissor();
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

	public int[] mouseToGridPos(double x, double y, int gx, int gy, int tileSize, int entryW, int entryH, EquipmentGrid grid) {
		float scaledTile = tileSize * (float) GRID_GUI_SIZE / grid.width();
		x -= entryW * scaledTile / 2.0 - scaledTile / 2.0;
		y -= entryH * scaledTile / 2.0 - scaledTile / 2.0;
		double rx = (x - gx) * (float) grid.width() / GRID_GUI_SIZE;
		double ry = (y - gy) * (float) grid.height() / GRID_GUI_SIZE;
		int grx = (int) Math.floor(rx / tileSize);
		int gry = (int) Math.floor(ry / tileSize);
		return new int[]{grx, gry};
	}

	@Override
	public boolean mouseClicked(double x, double y, int p_97750_) {
		Module m = ModuleList.getFromItem(menu.getCarried().getItem());
		if (m == null) {
			m = new NullModule();
		}
		EquipmentGrid grid = menu.getEquipmentGrid();
		if (grid != null) {
			int[] mpos = mouseToGridPos(x, y, gridX, gridY, TILE_SIZE, m.width, m.height, grid);
			if (grid.inBounds(mpos[0], mpos[1]))
				menu.onGridClick(mpos[0], mpos[1]);
		}

		return super.mouseClicked(x, y, p_97750_);
	}

	@Override
	public boolean mouseReleased(double x, double y, int p_97814_) {
		Module m = ModuleList.getFromItem(menu.getCarried().getItem());
		if (m == null) {
			m = new NullModule();
		}
		EquipmentGrid grid = menu.getEquipmentGrid();
		if (grid != null) {
			int[] mpos = mouseToGridPos(x, y, gridX, gridY, TILE_SIZE, m.width, m.height, grid);
			if (grid.inBounds(mpos[0], mpos[1]))
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
