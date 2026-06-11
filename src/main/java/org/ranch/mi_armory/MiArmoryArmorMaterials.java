package org.ranch.mi_armory;

import aztech.modern_industrialization.materials.MIMaterials;
import aztech.modern_industrialization.materials.part.MIParts;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class MiArmoryArmorMaterials {
	public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, MiArmory.MODID);

	public static final Holder<ArmorMaterial> BLASTPROOF =
			ARMOR_MATERIALS.register("blastproof_armor", () -> new ArmorMaterial(
					Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
						map.put(ArmorItem.Type.BOOTS, 3);
						map.put(ArmorItem.Type.LEGGINGS, 6);
						map.put(ArmorItem.Type.CHESTPLATE, 8);
						map.put(ArmorItem.Type.HELMET, 3);
						map.put(ArmorItem.Type.BODY, 11);
					}),
					10,
					SoundEvents.ARMOR_EQUIP_GENERIC,
					() -> Ingredient.of(),
					List.of(
							new ArmorMaterial.Layer(
									MiArmory.location("blastproof")
							)
					),
					5,
					0.20f
			));

	public static final Holder<ArmorMaterial> MODULAR_TIER_1 =
			ARMOR_MATERIALS.register("modular_1", () -> new ArmorMaterial(
					Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
						map.put(ArmorItem.Type.BOOTS, 2);
						map.put(ArmorItem.Type.LEGGINGS, 5);
						map.put(ArmorItem.Type.CHESTPLATE, 6);
						map.put(ArmorItem.Type.HELMET, 2);
						map.put(ArmorItem.Type.BODY, 5);
					}),
					0,
					SoundEvents.ARMOR_EQUIP_GENERIC,
					() -> Ingredient.of(MIMaterials.CARBON.getPart(MIParts.PLATE).asItem()),
					List.of(
							new ArmorMaterial.Layer(
									MiArmory.location("modular_1")
							)
					),
					5,
					0.0f
			));

	public static final Holder<ArmorMaterial> MODULAR_TIER_2 =
			ARMOR_MATERIALS.register("modular_2", () -> new ArmorMaterial(
					Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
						map.put(ArmorItem.Type.BOOTS, 3);
						map.put(ArmorItem.Type.LEGGINGS, 6);
						map.put(ArmorItem.Type.CHESTPLATE, 8);
						map.put(ArmorItem.Type.HELMET, 3);
						map.put(ArmorItem.Type.BODY, 11);
					}),
					0,
					SoundEvents.ARMOR_EQUIP_GENERIC,
					() -> Ingredient.of(MIMaterials.IRIDIUM.getPart(MIParts.INGOT).asItem()),
					List.of(
							new ArmorMaterial.Layer(
									MiArmory.location("modular_2")
							)
					),
					5,
					0.0f
			));

	public static void register(IEventBus modEventBus) {
		ARMOR_MATERIALS.register(modEventBus);
	}
}
