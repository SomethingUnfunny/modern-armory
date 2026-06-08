package org.ranch.mi_armory.modular_armor.custom_modules;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.function.TriFunction;
import org.ranch.mi_armory.modular_armor.Module;

import java.util.List;

public class SpeedModule extends Module {

	public SpeedModule(String id, int width, int height, long powerDraw, double speed) {
		super(id, width, height, powerDraw,
				(max, player, level) -> {
					Vec3 vel = player.getDeltaMovement();
					vel = vel.multiply(1, 0, 1);
					return vel.length() > 0.1 ? max : 0;
				},
				List.of(
						new AddedAttribute(Attributes.MOVEMENT_SPEED, speed, AttributeModifier.Operation.ADD_VALUE),
						new AddedAttribute(Attributes.MOVEMENT_EFFICIENCY, speed, AttributeModifier.Operation.ADD_VALUE)
				), EquipmentSlotGroup.LEGS);
	}
}
