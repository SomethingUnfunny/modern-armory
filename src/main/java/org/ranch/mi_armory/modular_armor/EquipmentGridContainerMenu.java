package org.ranch.mi_armory.modular_armor;

import aztech.modern_industrialization.MIRegistries;
import aztech.modern_industrialization.blocks.forgehammer.ForgeHammerScreenHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryBlocks;
import org.ranch.mi_armory.network.PacketEquipmentGridClick;

import java.util.Map;

public class EquipmentGridContainerMenu extends AbstractContainerMenu {

	public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings");
	public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots");
	private static final Map<EquipmentSlot, ResourceLocation> TEXTURE_EMPTY_SLOTS = Map.of(
			EquipmentSlot.FEET,
			EMPTY_ARMOR_SLOT_BOOTS,
			EquipmentSlot.LEGS,
			EMPTY_ARMOR_SLOT_LEGGINGS,
			EquipmentSlot.CHEST,
			EMPTY_ARMOR_SLOT_CHESTPLATE,
			EquipmentSlot.HEAD,
			EMPTY_ARMOR_SLOT_HELMET
	);
	private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};

	private final ContainerLevelAccess access;

	public final Slot armor;

	public EquipmentGridContainerMenu(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, ContainerLevelAccess.NULL);
	}

	public EquipmentGridContainerMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
		super(MiArmory.EQUIPMENT_GRID_MENU.get(), containerId);
		this.access = access;

		armor = new Slot(new SimpleContainer(1) {
			@Override
			public void setChanged() {
				super.setChanged();
				slotsChanged(this);
			}
		}, 0, 30, 26);

		this.addSlot(armor);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}

		for (int i = 0; i < 4; i++) {
			EquipmentSlot equipmentslot = SLOT_IDS[i];
			ResourceLocation resourcelocation = TEXTURE_EMPTY_SLOTS.get(equipmentslot);
			this.addSlot(new ArmorSlot(playerInventory, playerInventory.player, equipmentslot, i + 36, 8, 53 - i * 18, resourcelocation));
		}
	}

	public EquipmentGrid getEquipmentGrid() {
		return EquipmentGrid.getGridData(armor.getItem());
	}

	public void setEquipmentGrid(EquipmentGrid grid) {
		EquipmentGrid.setGridData(armor.getItem(), grid);
	}

	public void onGridClick(int x, int y, PacketEquipmentGridClick.ClickType clickType) {
		PacketEquipmentGridClick.sendToServer(x, y, clickType, containerId);
		//handleGridClick(x, y, clickType); // idunnoooooo
	}

	public void handleGridClick(int x, int y, PacketEquipmentGridClick.ClickType clickType) {
		EquipmentGrid grid = getEquipmentGrid();
		if (grid != null) {
			EquipmentGrid.Entry entry = grid.getAtPos(x, y);
			if (entry != null) { // remove from grid
				if (this.getCarried().isEmpty() || (this.getCarried().getItem() == entry.stack().getItem() && this.getCarried().getCount() < this.getCarried().getMaxStackSize())) {
					setEquipmentGrid(grid.remove(entry));
					if (getCarried().isEmpty()) {
						this.setCarried(entry.stack());
					} else {
						ItemStack entryStack = entry.stack().copy();
						ItemStack carriedStack = getCarried().copy();
						carriedStack.setCount(carriedStack.getCount() + entryStack.getCount()); // +1 allthetime?
						this.setCarried(carriedStack);
					}
				}
			} else if (!getCarried().isEmpty() && ModuleList.hasItem(getCarried().getItem())) { // add to grid
				ItemStack carriedStack = getCarried().copy();
				EquipmentGrid.Entry toAdd = new EquipmentGrid.Entry(x, y, carriedStack.copyWithCount(1));
				if (grid.canAdd(toAdd)) {
					setEquipmentGrid(grid.add(toAdd));
					carriedStack.setCount(carriedStack.getCount() - 1);
					if (carriedStack.isEmpty()) {
						setCarried(ItemStack.EMPTY);
					} else {
						setCarried(carriedStack);
					}
				}
			}
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return AbstractContainerMenu.stillValid(this.access, player, MiArmoryBlocks.EQUIPMENT_GRID_BLOCK.get());
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		access.execute((world, blockPos) -> {
			this.clearContainer(player, armor.container);
		});
	}
}
