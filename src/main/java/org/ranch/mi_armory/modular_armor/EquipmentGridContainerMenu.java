package org.ranch.mi_armory.modular_armor;

import aztech.modern_industrialization.MIRegistries;
import aztech.modern_industrialization.blocks.forgehammer.ForgeHammerScreenHandler;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryBlocks;

public class EquipmentGridContainerMenu extends AbstractContainerMenu {

	private final ContainerLevelAccess access;

	public final Slot armor;

	public EquipmentGridContainerMenu(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, ContainerLevelAccess.NULL);
	}

	public EquipmentGridContainerMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
		super(MiArmory.EQUIPMENT_GRID_MENU.get(), containerId);
		this.access = access;


		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}

		for (int i = 0; i < 4; i++) {
			this.addSlot(new Slot(playerInventory, i + 36, 8, 60 - i * 18));
		}

		armor = new Slot(new SimpleContainer(1) {
			@Override
			public void setChanged() {
				super.setChanged();
				slotsChanged(this);
			}
		}, 0, 34, 33);

		this.addSlot(armor);
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
