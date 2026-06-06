package org.ranch.mi_armory.modular_armor;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;

public class EquipmentGrid {

	public final int width;
	public final int height;

	public final ArrayList<Entry> modules;

	public EquipmentGrid(int width, int height) {
		this.width = width;
		this.height = height;
		this.modules = new ArrayList<>();
	}

	public record Entry(int x, int y, ItemStack stack, Module module) {

	}
}
