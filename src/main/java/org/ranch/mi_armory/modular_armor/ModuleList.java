package org.ranch.mi_armory.modular_armor;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.materials.MIMaterials;
import aztech.modern_industrialization.materials.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryAttributes;
import org.ranch.mi_armory.items.ModularArmor;
import org.ranch.mi_armory.modular_armor.custom_modules.BatteryModule;
import org.ranch.mi_armory.modular_armor.custom_modules.ShieldModule;
import org.ranch.mi_armory.modular_armor.custom_modules.SolarPanelModule;
import org.ranch.mi_armory.modular_armor.custom_modules.SpeedModule;

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

		addDrill(MIMaterials.COPPER, 0.1);
		addDrill(MIMaterials.BRONZE, 0.2);
		addDrill(MIMaterials.GOLD, 0.4);
		addDrill(MIMaterials.STEEL, 0.8);
		addDrill(MIMaterials.ALUMINUM, 1.6);
		addDrill(MIMaterials.STAINLESS_STEEL, 3.2);
		addDrill(MIMaterials.TITANIUM, 6.4);

		double baseMotorBoost = 0.015;
		long drawPerBaseBoost = 16;

		addModule(
				MI.id("gravichestplate"),
				new Module(
						"gravichestplate",
						3, 4,
						1024,
						(m, p, l) -> p.getAbilities().flying ? m : 0,
						List.of(
								new Module.AddedAttribute(
										NeoForgeMod.CREATIVE_FLIGHT, 1, AttributeModifier.Operation.ADD_VALUE
								)
						),
						EquipmentSlotGroup.CHEST
				)
		);

		addModule(
				ResourceLocation.withDefaultNamespace("elytra"),
				new Module(
						"elytra",
						2, 3,
						0,
						List.of(),
						EquipmentSlotGroup.CHEST
				)
		);

		addModule(
				MI.id("singularity"),
				new Module(
						"singularity",
						1, 1,
						0,
						List.of(),
						EquipmentSlotGroup.ANY
				) {
					@Override
					public long powerGen(Level level, Player player, ItemStack stack) {
						return ((ModularArmor) stack.getItem()).getEnergyCapacity(stack);
					}
				}
		);

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
						25f,
						0.04f
				)
		);

		addModule(
				MiArmory.location("shield_module_2"),
				new ShieldModule(
						"shield_2",
						2, 2,
						50f,
						0.08f
				)
		);

		addModule(
				MiArmory.location("solar_panel"),
				new SolarPanelModule(
						"solar_panel",
						1, 1,
						1, 1
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

	private static void addDrill(Material material, double speed) {
		addModule(
				MI.id(material.name + "_drill"),
				new Module(
						material.name + "_drill",
						1, 1,
						0,
						List.of(
								new Module.AddedAttribute(Attributes.MINING_EFFICIENCY, speed, AttributeModifier.Operation.ADD_VALUE)
						),
						EquipmentSlotGroup.CHEST
				)
		);
	}
}
