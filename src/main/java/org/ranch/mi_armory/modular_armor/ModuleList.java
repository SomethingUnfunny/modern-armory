package org.ranch.mi_armory.modular_armor;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.items.PortableStorageUnit;
import aztech.modern_industrialization.materials.MIMaterials;
import aztech.modern_industrialization.materials.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import org.ranch.mi_armory.MiArmoryAttributes;
import org.ranch.mi_armory.modular_armor.custom_modules.BatteryModule;
import org.ranch.mi_armory.modular_armor.custom_modules.SolarPanelModule;
import org.ranch.mi_armory.util.UnfunUtil;

import java.util.HashMap;
import java.util.List;

public class ModuleList {

	private static final HashMap<String, Module> modules = new HashMap<>();

	public static void addModule(String key, Module module) {
		modules.put(key, module);
	}

	public static void addModule(ResourceLocation key, Module module) {
		modules.put(key.toString(), module);
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
		addBattery(MIMaterials.REDSTONE);
		addBattery(MIMaterials.SODIUM);
		addBattery(MIMaterials.SILICON);
		addBattery(MIMaterials.CADMIUM);
		addBattery(MIMaterials.PLUTONIUM);

		double baseMotorBoost = 0.01;
		long drawPerBaseBoost = 16;

		addModule(
				MI.id("large_motor"),
				new Module(
						"large_motor",
						2, 4,
						drawPerBaseBoost * 5,
						List.of(
								new Module.AddedAttribute(Attributes.MOVEMENT_SPEED, baseMotorBoost * 5, AttributeModifier.Operation.ADD_VALUE)
						),
						EquipmentSlotGroup.LEGS
				)
		);
		addModule(
				MI.id("large_advanced_motor"),
				new Module(
						"large_advanced_motor",
						2, 4,
						drawPerBaseBoost * 20 * 2,
						List.of(
								new Module.AddedAttribute(Attributes.MOVEMENT_SPEED, baseMotorBoost * 20, AttributeModifier.Operation.ADD_VALUE)
						),
						EquipmentSlotGroup.LEGS
				)
		);

		addModule(
				MI.id("piston"),
				new Module(
						"piston",
						1, 2,
						16,
						List.of(
								new Module.AddedAttribute(Attributes.JUMP_STRENGTH, 0.05, AttributeModifier.Operation.ADD_VALUE)
						),
						EquipmentSlotGroup.FEET
				)
		);

		addModule(
				MI.id("blastproof_alloy_large_plate"),
				new Module(
						"blastproof_plate",
						3, 3,
						0,
						List.of(
								new Module.AddedAttribute(MiArmoryAttributes.SHOCKWAVE_RESISTANCE, 0.05, AttributeModifier.Operation.ADD_VALUE)
						),
						EquipmentSlotGroup.ANY
				)
		);

		addModule(
				MI.id("silicon_plate"),
				new SolarPanelModule(
						"solar_panel",
						1, 1,
						0.25, 2
				)
		);
	}

	private static void addBattery(Material material) {
		addModule(
				"modern_industrialization:" + material.name + "_battery",
				new BatteryModule(
						material.name + "_battery",
						1,
						2,
						material,
						List.of()
				)
		);
	}
}
