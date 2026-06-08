package org.ranch.mi_armory.modular_armor.custom_modules;

import net.minecraft.world.entity.EquipmentSlotGroup;
import org.ranch.mi_armory.modular_armor.Module;

import java.util.List;

public class NullModule extends Module {
	public NullModule() {
		super("null", 1, 1, 0, List.of(), EquipmentSlotGroup.ANY);
	}
}
