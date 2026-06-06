package org.ranch.mi_armory.modular_armor;

import aztech.modern_industrialization.MIItem;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.List;

public class ModuleList {

	private static final HashMap<Item, Module> modules = new HashMap<>();

	public static void addModule(Item key, Module module) {
		modules.put(key, module);
	}

	public static HashMap<Item, Module> getModules() {
		return modules;
	}

	public static boolean hasItem(Item item) {
		return modules.containsKey(item);
	}

	public static Module getFromItem(Item item) {
		return modules.get(item);
	}

	static {
		addModule(
				Items.DIAMOND,
				new Module(
						2,
						2,
						100,
						List.of(
								new Module.AddedAttribute(
										Attributes.JUMP_STRENGTH,
										0.1
								)
						)
				)
		);
		addModule(
				Items.IRON_INGOT,
				new Module(
						1,
						2,
						100,
						List.of(
								new Module.AddedAttribute(
										Attributes.MOVEMENT_SPEED,
										0.1
								)
						)
				)
		);
	}
}
