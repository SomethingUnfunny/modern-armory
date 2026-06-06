package org.ranch.mi_armory.modular_armor;

import aztech.modern_industrialization.items.PortableStorageUnit;
import aztech.modern_industrialization.materials.MIMaterials;
import aztech.modern_industrialization.materials.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import org.ranch.mi_armory.modular_armor.custom_modules.BatteryModule;
import org.ranch.mi_armory.util.UnfunUtil;

import java.util.HashMap;
import java.util.List;

public class ModuleList {

	private static final HashMap<String, Module> modules = new HashMap<>();

	public static void addModule(String key, Module module) {
		modules.put(key, module);
	}

	public static HashMap<String, Module> getModules() {
		return modules;
	}

	public static boolean hasItem(String item) {
		return modules.containsKey(item);
	}

	public static boolean hasItem(Item item) {
		return hasItem(BuiltInRegistries.ITEM.getKey(item).toString());
	}

	public static Module getFromItem(String item) {
		return modules.get(item);
	}

	public static Module getFromItem(Item item) {
		return getFromItem(BuiltInRegistries.ITEM.getKey(item).toString());
	}

	static {
		addModule(
				"minecraft:diamond",
				new Module(
						"diamond",
						2,
						2,
						100,
						0,
						List.of(
								new Module.AddedAttribute(
										Attributes.JUMP_STRENGTH,
										0.1,
										AttributeModifier.Operation.ADD_VALUE
								)
						)
				)
		);
		addModule(
				"modern_industrialization:redstone_battery",
				new BatteryModule(
						"redstone_battery",
						1,
						2,
						MIMaterials.REDSTONE,
						List.of()
				)
		);
	}
}
