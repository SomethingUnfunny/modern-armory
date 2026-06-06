package org.ranch.mi_armory.items;

import aztech.modern_industrialization.MIComponents;
import dev.technici4n.grandpower.api.ISimpleEnergyItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.ranch.mi_armory.MiArmoryArmorMaterials;
import org.ranch.mi_armory.MiArmoryComponents;
import org.ranch.mi_armory.modular_armor.EquipmentGrid;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ModularArmor extends ArmorItem implements ISimpleEnergyItem {
	public static final long BASE_ENERGY_CAPACITY = 0;

	public ModularArmor(Type type) {
		super(MiArmoryArmorMaterials.MODULAR, type,
				new Item.Properties()
						.durability(type.getDurability(50))
						.component(MIComponents.ENERGY, 0L)
						.component(
								MiArmoryComponents.EQUIPMENT_GRID_COMPONENT,
								new EquipmentGrid(4, 4, new ArrayList<>())
						)
		);
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
		ItemAttributeModifiers.Builder attributesBuilder = ItemAttributeModifiers.builder();

		addVanillaAttributes(attributesBuilder);

		EquipmentGrid grid = EquipmentGrid.getGridData(stack);
		if (grid != null) {
			for (EquipmentGrid.Entry entry : grid.modules()) {
				entry.module().addAttributes(attributesBuilder, stack, type);
			}
		}

		return attributesBuilder.build();
	}

	private ItemAttributeModifiers.Builder addVanillaAttributes(ItemAttributeModifiers.Builder attributesBuilder) {
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

		return attributesBuilder;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
		super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
		this.setStoredEnergy(stack, Math.min(getEnergyCapacity(stack), getStoredEnergy(stack)));
	}

	@Override
	public boolean isDamageable(ItemStack stack) {
		return false;
	}

	@Override
	public DataComponentType<Long> getEnergyComponent() {
		return MIComponents.ENERGY.get();
	}

	@Override
	public long getEnergyCapacity(ItemStack stack) {
		long added = 0;

		EquipmentGrid grid = EquipmentGrid.getGridData(stack);
		if (grid != null) {
			for (EquipmentGrid.Entry entry : grid.modules()) {
				added += entry.module().addedEU();
			}
		}
		return BASE_ENERGY_CAPACITY + added;
	}

	@Override
	public long getEnergyMaxInput(ItemStack stack) {
		return getEnergyCapacity(stack);
	}

	@Override
	public long getEnergyMaxOutput(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return (int) Math.round(getStoredEnergy(stack) / (double) getEnergyCapacity(stack) * 13);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		Long energy = stack.get(getEnergyComponent());
		if (energy == null) {
			energy = 0L;
		}
		NumberFormat formatter = new DecimalFormat("#0");
		String energyFormatted = formatter.format(energy);
		String totalFormatted = formatter.format(getEnergyCapacity(stack));
		String percentFormatted = formatter.format(((float) energy / getEnergyCapacity(stack)) * 100);
		Component energyComponent = Component.literal(energyFormatted + " / " + totalFormatted + " EU " + "(" + percentFormatted + "%)").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16768637)).withItalic(false));
		tooltipComponents.add(Component.literal("Energy Stored: ").withStyle(ChatFormatting.GRAY).append(energyComponent));
	}
}
