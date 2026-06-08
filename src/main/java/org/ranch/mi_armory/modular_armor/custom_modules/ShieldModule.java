package org.ranch.mi_armory.modular_armor.custom_modules;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.ranch.mi_armory.MiArmoryAttributes;
import org.ranch.mi_armory.modular_armor.Module;

import java.util.List;

public class ShieldModule extends Module {

	private final float chargeRate;

	public ShieldModule(String id, int width, int height, double shield, float chargeRate) {
		super(id, width, height, 0, List.of(new AddedAttribute(MiArmoryAttributes.ENERGY_SHIELD, shield, AttributeModifier.Operation.ADD_VALUE)), EquipmentSlotGroup.ANY);
		this.chargeRate = chargeRate;
	}

	public float getChargeRate() {
		return chargeRate;
	}
}
