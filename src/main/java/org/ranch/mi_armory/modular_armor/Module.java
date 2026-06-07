package org.ranch.mi_armory.modular_armor;

import net.minecraft.core.Holder;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.ranch.mi_armory.MiArmory;

import java.util.ArrayList;
import java.util.List;

public class Module {

	public final String id;
	public final int width;
	public final int height;
	private final long basePowerDraw;
	private List<AddedAttribute> attributes;
	public final EquipmentSlotGroup validSlots;

	public Module(String id, int width, int height, long powerDraw, List<AddedAttribute> attributes) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.basePowerDraw = powerDraw;
		this.attributes = attributes;
		this.validSlots = EquipmentSlotGroup.ANY;
	}

	public Module(String id, int width, int height, long powerDraw, List<AddedAttribute> attributes, EquipmentSlotGroup validSlots) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.basePowerDraw = powerDraw;
		this.attributes = attributes;
		this.validSlots = validSlots;
	}

	public long addedEU() {
		return 0;
	}

	public long powerDraw() {
		return basePowerDraw;
	}

	public long powerGen(Level level, Player player) {
		return 0;
	}

	public boolean canInsert(EquipmentSlot slot) {
		return validSlots.test(slot);
	}

	// todo merge same attributes so they dont fill up the tooltip
	public ItemAttributeModifiers.Builder addAttributes(ItemAttributeModifiers.Builder builder, ItemStack stack, ArmorItem.Type type, int i) {
		EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot(type.getSlot());
		for (AddedAttribute attribute : attributes) {
			builder.add(
					attribute.attribute,
					new AttributeModifier(
							MiArmory.location("modular_armor." + type.getName() + "." + id + "." + i),
							attribute.value,
							attribute.operation
					),
					equipmentslotgroup
			);
		}
		return builder;
	}

	public record AddedAttribute(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {

	}
}
