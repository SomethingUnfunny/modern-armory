package org.ranch.mi_armory;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ranch.mi_armory.items.DetonatorSelection;
import org.ranch.mi_armory.items.DetonatorSelections;
import org.ranch.mi_armory.modular_armor.EquipmentGrid;

import java.util.function.Supplier;

public class MiArmoryComponents {
	public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MiArmory.MODID);

	public static final Supplier<DataComponentType<DetonatorSelection>> DETONATOR_SELECTION = COMPONENTS.registerComponentType("detonator_selection", (b) -> b.persistent(DetonatorSelection.CODEC));
	public static final Supplier<DataComponentType<DetonatorSelections>> DETONATOR_SELECTIONS = COMPONENTS.registerComponentType("detonator_selections", (b) -> b.persistent(DetonatorSelections.CODEC));

	public static final Supplier<DataComponentType<EquipmentGrid>> EQUIPMENT_GRID_COMPONENT = COMPONENTS.registerComponentType("equipment_grid", (b) -> b.persistent(EquipmentGrid.CODEC));

	public static void register(IEventBus modEventBus) {
		COMPONENTS.register(modEventBus);
	}
}
