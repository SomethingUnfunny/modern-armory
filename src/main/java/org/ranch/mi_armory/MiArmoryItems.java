package org.ranch.mi_armory;

import aztech.modern_industrialization.MIBlock;
import dev.technici4n.grandpower.api.ISimpleEnergyItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ranch.mi_armory.items.BlastproofArmor;
import org.ranch.mi_armory.items.DetonatorItem;
import org.ranch.mi_armory.items.ModularArmor;
import org.ranch.mi_armory.items.MultiDetonatorItem;

public class MiArmoryItems {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MiArmory.MODID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MiArmory.MODID);

	public static final DeferredItem<Item> DETONATOR = ITEMS.registerItem("detonator", DetonatorItem::new, new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> MULTI_DETONATOR = ITEMS.registerItem("multi_detonator", MultiDetonatorItem::new, new Item.Properties().stacksTo(1));

	public static final DeferredItem<BlastproofArmor> BLASTPROOF_HELMET = ITEMS.register("blastproof_helmet", () -> new BlastproofArmor(ArmorItem.Type.HELMET));
	public static final DeferredItem<BlastproofArmor> BLASTPROOF_CHESTPLATE = ITEMS.register("blastproof_chestplate", () -> new BlastproofArmor(ArmorItem.Type.CHESTPLATE));
	public static final DeferredItem<BlastproofArmor> BLASTPROOF_LEGGINGS = ITEMS.register("blastproof_leggings", () -> new BlastproofArmor(ArmorItem.Type.LEGGINGS));
	public static final DeferredItem<BlastproofArmor> BLASTPROOF_BOOTS = ITEMS.register("blastproof_boots", () -> new BlastproofArmor(ArmorItem.Type.BOOTS));

	public static final DeferredItem<ModularArmor> MODULAR_TIER_1_HELMET = ITEMS.register("modular_1_helmet", () -> new ModularArmor(ArmorItem.Type.HELMET, MiArmoryArmorMaterials.MODULAR_TIER_1, 3));
	public static final DeferredItem<ModularArmor> MODULAR_TIER_1_CHESTPLATE = ITEMS.register("modular_1_chestplate", () -> new ModularArmor(ArmorItem.Type.CHESTPLATE, MiArmoryArmorMaterials.MODULAR_TIER_1, 3));
	public static final DeferredItem<ModularArmor> MODULAR_TIER_1_LEGGINGS = ITEMS.register("modular_1_leggings", () -> new ModularArmor(ArmorItem.Type.LEGGINGS, MiArmoryArmorMaterials.MODULAR_TIER_1, 3));
	public static final DeferredItem<ModularArmor> MODULAR_TIER_1_BOOTS = ITEMS.register("modular_1_boots", () -> new ModularArmor(ArmorItem.Type.BOOTS, MiArmoryArmorMaterials.MODULAR_TIER_1, 3));

	public static final DeferredItem<ModularArmor> MODULAR_TIER_2_HELMET = ITEMS.register("modular_2_helmet", () -> new ModularArmor(ArmorItem.Type.HELMET, MiArmoryArmorMaterials.MODULAR_TIER_2, 5));
	public static final DeferredItem<ModularArmor> MODULAR_TIER_2_CHESTPLATE = ITEMS.register("modular_2_chestplate", () -> new ModularArmor(ArmorItem.Type.CHESTPLATE, MiArmoryArmorMaterials.MODULAR_TIER_2, 5));
	public static final DeferredItem<ModularArmor> MODULAR_TIER_2_LEGGINGS = ITEMS.register("modular_2_leggings", () -> new ModularArmor(ArmorItem.Type.LEGGINGS, MiArmoryArmorMaterials.MODULAR_TIER_2, 5));
	public static final DeferredItem<ModularArmor> MODULAR_TIER_2_BOOTS = ITEMS.register("modular_2_boots", () -> new ModularArmor(ArmorItem.Type.BOOTS, MiArmoryArmorMaterials.MODULAR_TIER_2, 5));


	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_MODE_TAB = CREATIVE_MODE_TABS.register("mi_armory_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.mi_armory")).withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> DETONATOR.get().getDefaultInstance()).displayItems((parameters, output) -> {
		output.accept(DETONATOR.get());
		output.accept(MULTI_DETONATOR.get());
		output.accept(MIBlock.NUKE.asItem());
		output.accept(BLASTPROOF_HELMET.asItem());
		output.accept(BLASTPROOF_CHESTPLATE.asItem());
		output.accept(BLASTPROOF_LEGGINGS.asItem());
		output.accept(BLASTPROOF_BOOTS.asItem());
		output.accept(MODULAR_TIER_1_HELMET.asItem());
		output.accept(MODULAR_TIER_1_CHESTPLATE.asItem());
		output.accept(MODULAR_TIER_1_LEGGINGS.asItem());
		output.accept(MODULAR_TIER_1_BOOTS.asItem());
		output.accept(MODULAR_TIER_2_HELMET.asItem());
		output.accept(MODULAR_TIER_2_CHESTPLATE.asItem());
		output.accept(MODULAR_TIER_2_LEGGINGS.asItem());
		output.accept(MODULAR_TIER_2_BOOTS.asItem());
	}).build());

	public static void register(IEventBus modEventBus) {
		ITEMS.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
	}

	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		ISimpleEnergyItem.registerStorage(event, MODULAR_TIER_1_HELMET.get());
		ISimpleEnergyItem.registerStorage(event, MODULAR_TIER_1_CHESTPLATE.get());
		ISimpleEnergyItem.registerStorage(event, MODULAR_TIER_1_LEGGINGS.get());
		ISimpleEnergyItem.registerStorage(event, MODULAR_TIER_1_BOOTS.get());

		ISimpleEnergyItem.registerStorage(event, MODULAR_TIER_2_HELMET.get());
		ISimpleEnergyItem.registerStorage(event, MODULAR_TIER_2_CHESTPLATE.get());
		ISimpleEnergyItem.registerStorage(event, MODULAR_TIER_2_LEGGINGS.get());
		ISimpleEnergyItem.registerStorage(event, MODULAR_TIER_2_BOOTS.get());
	}
}
