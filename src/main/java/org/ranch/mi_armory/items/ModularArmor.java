package org.ranch.mi_armory.items;

import aztech.modern_industrialization.MIComponents;
import dev.technici4n.grandpower.api.ISimpleEnergyItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.*;
import org.ranch.mi_armory.MiArmoryArmorMaterials;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ModularArmor extends ArmorItem implements ISimpleEnergyItem {
	public static final long ENERGY_CAPACITY = 1 << 16;

	public ModularArmor(Type type) {
		super(MiArmoryArmorMaterials.MODULAR, type, new Item.Properties().durability(type.getDurability(50)).component(MIComponents.ENERGY, 0L));
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
		return ENERGY_CAPACITY;
	}

	@Override
	public long getEnergyMaxInput(ItemStack stack) {
		return ENERGY_CAPACITY;
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
		return (int) Math.round(getStoredEnergy(stack) / (double) ENERGY_CAPACITY * 13);
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
