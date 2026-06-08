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
import net.minecraft.world.item.Items;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryAttributes;
import org.ranch.mi_armory.modular_armor.custom_modules.BatteryModule;
import org.ranch.mi_armory.modular_armor.custom_modules.ShieldModule;
import org.ranch.mi_armory.modular_armor.custom_modules.SolarPanelModule;
import org.ranch.mi_armory.modular_armor.custom_modules.SpeedModule;
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

		double baseMotorBoost = 0.015;
		long drawPerBaseBoost = 16;

		addModule(
				MiArmory.location("exoskeleton"),
				new SpeedModule(
						"exoskeleton",
						2, 4,
						drawPerBaseBoost * 8,
						baseMotorBoost * 8
				)
		);

		addModule(
				MI.id("motor"),
				new SpeedModule(
						"motor",
						2, 1,
						drawPerBaseBoost,
						baseMotorBoost
				)
		);

		addModule(
				MI.id("iron_tank"),
				new Module(
						"iron_tank",
						1, 1,
						0,
						List.of(
								new Module.AddedAttribute(Attributes.OXYGEN_BONUS, 1, AttributeModifier.Operation.ADD_VALUE)
						),
						EquipmentSlotGroup.HEAD
				)
		);

		addModule(
				MI.id("piston"),
				new Module(
						"piston",
						1, 2,
						16,
						List.of(
								new Module.AddedAttribute(Attributes.JUMP_STRENGTH, 0.06, AttributeModifier.Operation.ADD_VALUE)
						),
						EquipmentSlotGroup.FEET
				)
		);

		addModule(
				MI.id("blastproof_alloy_large_plate"),
				new Module(
						"blastproof_plate",
						2, 2,
						0,
						List.of(
								new Module.AddedAttribute(MiArmoryAttributes.SHOCKWAVE_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_VALUE)
						),
						EquipmentSlotGroup.ANY
				)
		);

		addModule(
				MiArmory.location("shield_module"),
				new ShieldModule(
						"shield_1",
						2, 2,
						10f,
						0.03f
				)
		);

		addModule(
				MiArmory.location("shield_module_2"),
				new ShieldModule(
						"shield_2",
						2, 2,
						20f,
						0.06f
				)
		);

		addModule(
				MiArmory.location("solar_panel"),
				new SolarPanelModule(
						"solar_panel",
						1, 1,
						0.5, 1
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
