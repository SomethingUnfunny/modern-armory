package org.ranch.mi_armory.items;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryArmorMaterials;
import org.ranch.mi_armory.MiArmoryAttributes;

public class BlastproofArmor extends ArmorItem {
	public BlastproofArmor(Type type) {
		super(MiArmoryArmorMaterials.BLASTPROOF, type, new Item.Properties().durability(type.getDurability(50)));
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
		ItemAttributeModifiers.Builder attributesBuilder = ItemAttributeModifiers.builder();
		EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot(type.getSlot());

		ResourceLocation vanillaEffectLocation = ResourceLocation.withDefaultNamespace("armor." + type.getName());
		attributesBuilder.add(
				Attributes.ARMOR,
				new AttributeModifier(
						vanillaEffectLocation,
						material.value().getDefense(type),
						AttributeModifier.Operation.ADD_VALUE
				),
				equipmentslotgroup
		);
		attributesBuilder.add(
				Attributes.ARMOR_TOUGHNESS,
				new AttributeModifier(
						vanillaEffectLocation,
						material.value().toughness(),
						AttributeModifier.Operation.ADD_VALUE
				),
				equipmentslotgroup
		);

		float f1 = material.value().knockbackResistance();
		if (f1 > 0.0F) {
			attributesBuilder.add(
					Attributes.KNOCKBACK_RESISTANCE,
					new AttributeModifier(
							vanillaEffectLocation,
							f1,
							AttributeModifier.Operation.ADD_VALUE
					),
					equipmentslotgroup
			);
		}

		attributesBuilder.add(
				Attributes.STEP_HEIGHT,
				new AttributeModifier(
						MiArmory.location("blastproof." + type.getName()),
						0.5 / 4,
						AttributeModifier.Operation.ADD_VALUE
				),
				equipmentslotgroup
		);

		attributesBuilder.add(
				MiArmoryAttributes.SHOCKWAVE_RESISTANCE,
				new AttributeModifier(
						MiArmory.location("blastproof." + type.getName()),
						0.90 / 4,
						AttributeModifier.Operation.ADD_VALUE
				),
				equipmentslotgroup
		);

		debuff(attributesBuilder, Attributes.MOVEMENT_SPEED, -0.015);
		debuff(attributesBuilder, Attributes.ATTACK_DAMAGE, -1);
		debuff(attributesBuilder, Attributes.JUMP_STRENGTH, -0.04);

		return attributesBuilder.build();
	}

	private void debuff(ItemAttributeModifiers.Builder builder, Holder<Attribute> attribute, double amount) {
		builder.add(
				attribute,
				new AttributeModifier(
						MiArmory.location("blastproof." + type.getName()),
						amount,
						AttributeModifier.Operation.ADD_VALUE
				),
				EquipmentSlotGroup.bySlot(type.getSlot())
		);
	}
}
