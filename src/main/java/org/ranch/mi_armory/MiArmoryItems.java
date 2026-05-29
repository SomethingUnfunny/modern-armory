package org.ranch.mi_armory;

import aztech.modern_industrialization.MIBlock;
import aztech.modern_industrialization.MIItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ranch.mi_armory.items.DetonatorItem;
import org.ranch.mi_armory.items.MultiDetonatorItem;

public class MiArmoryItems {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MiArmory.MODID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MiArmory.MODID);

	public static final DeferredItem<Item> DETONATOR = ITEMS.registerItem("detonator", DetonatorItem::new, new Item.Properties().stacksTo(1));
	public static final DeferredItem<Item> MULTI_DETONATOR = ITEMS.registerItem("multi_detonator", MultiDetonatorItem::new, new Item.Properties().stacksTo(1));

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_MODE_TAB = CREATIVE_MODE_TABS.register("mi_armory_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.mi_armory")).withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> DETONATOR.get().getDefaultInstance()).displayItems((parameters, output) -> {
		output.accept(DETONATOR.get());
		output.accept(MULTI_DETONATOR.get());
		output.accept(MIBlock.NUKE.asItem());
	}).build());

	public static void register(IEventBus modEventBus) {
		ITEMS.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
	}
}
