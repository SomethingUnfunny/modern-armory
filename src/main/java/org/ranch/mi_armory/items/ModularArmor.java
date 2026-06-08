package org.ranch.mi_armory.items;

import aztech.modern_industrialization.MIComponents;
import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.MITooltips;
import dev.technici4n.grandpower.api.ISimpleEnergyItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryArmorMaterials;
import org.ranch.mi_armory.MiArmoryComponents;
import org.ranch.mi_armory.modular_armor.EquipmentGrid;

import java.util.ArrayList;
import java.util.List;

public class ModularArmor extends ArmorItem implements ISimpleEnergyItem {
	public static final long BASE_ENERGY_CAPACITY = 2 << 14;
	public static final EquipmentSlot[] EQUIPMENT_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
	public static final EquipmentSlot[] EQUIPMENT_SLOTS_REVERSED = new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};

	public ModularArmor(Type type, Holder<ArmorMaterial> material, int grid_size) {
		super(material, type,
				new Item.Properties()
						.stacksTo(1)
						.component(MIComponents.ENERGY, 0L)
						.component(
								MiArmoryComponents.EQUIPMENT_GRID_COMPONENT,
								new EquipmentGrid(grid_size, grid_size, new ArrayList<>())
						)
		);
	}

	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
		ItemAttributeModifiers.Builder attributesBuilder = ItemAttributeModifiers.builder();

		addVanillaAttributes(attributesBuilder);

		EquipmentGrid grid = EquipmentGrid.getGridData(stack);
		if (grid != null) {
			int i = 0;
			for (EquipmentGrid.Entry entry : grid.modules()) {
				if (getStoredEnergy(stack) < entry.module().maxPowerDraw()) continue; // power is taken in inventoryTick
				entry.module().addAttributes(attributesBuilder, stack, type, i++);
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
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean p_41408_) {
		super.inventoryTick(stack, level, entity, slotId, p_41408_);
		this.setStoredEnergy(stack, Math.min(getEnergyCapacity(stack), getStoredEnergy(stack)));
		if (slotId > 35 && slotId < 40 && entity instanceof Player p) {
			armorTick(stack, level, p, slotId, p_41408_);
		}
	}

	private void armorTick(ItemStack stack, Level level, Player player, int slotId, boolean p_41408_) {
		if (!level.isClientSide()) {
			distributeEnergy(player);
		}

		EquipmentGrid grid = EquipmentGrid.getGridData(stack);
		if (grid != null) {
			for (EquipmentGrid.Entry entry : grid.modules()) {
				this.tryUseEnergy(stack, entry.module().powerDraw(player, level));
				this.setStoredEnergy(stack, Math.min(getEnergyCapacity(stack), getStoredEnergy(stack) + entry.module().powerGen(level, player)));
			}
		}
	}

	private void distributeEnergy(Player p) { // god smite this bullshit
		long totalMax = 0;
		long total = 0;
		int pieces = 0;
		EquipmentSlot first = null;
		for (EquipmentSlot slot : EQUIPMENT_SLOTS) { // collect total energy and amount of pieces
			ItemStack sStack = p.getItemBySlot(slot);
			if (isArmor(sStack)) {
				total += getStoredEnergy(sStack);
				totalMax += getEnergyCapacity(sStack);
				pieces++;
				if (first == null) {
					first = slot;
				}
			}
		}

		if ((total > 0 || pieces > 0) && first == type.getSlot()) {
			double filled = (double) total / totalMax;
			long newTotal = 0;

			long off = 0;
			for (EquipmentSlot slot : EQUIPMENT_SLOTS) { // see if already kinda balanced
				ItemStack sStack = p.getItemBySlot(slot);
				if (isArmor(sStack)) {
					long newValue = (long) (getEnergyCapacity(sStack) * filled);
					off += Math.abs(newValue - getStoredEnergy(sStack));
				}
			}

			if (off < 8) return;

			// modification starts from here

			for (EquipmentSlot slot : EQUIPMENT_SLOTS) { // set every piece to the % of the total
				ItemStack sStack = p.getItemBySlot(slot);
				if (isArmor(sStack)) {
					long newValue = (long) (getEnergyCapacity(sStack) * filled);
					setStoredEnergy(sStack, newValue);
					newTotal += newValue;
				}
			}

			long error = newTotal - total;
			MiArmory.LOGGER.info(String.valueOf(error));
			if (error != 0) {
				for (EquipmentSlot slot : EQUIPMENT_SLOTS) { // correct for error
					ItemStack sStack = p.getItemBySlot(slot);
					if (isArmor(sStack)) {
						long canAdd = getEnergyCapacity(sStack) - getStoredEnergy(sStack);
						long toAdd = Math.clamp(error, -getStoredEnergy(sStack), canAdd);
						long newValue = getStoredEnergy(sStack) + toAdd;
						setStoredEnergy(sStack, newValue);
						error -= toAdd;
					}
				}
			}
		}
	}

	public boolean takeEnergy(ItemStack stack, long amount) {
		return this.tryUseEnergy(stack, amount);
	}

	public boolean isArmor(ItemStack stack) {
		return stack.getItem() instanceof ModularArmor;
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
		return Math.min((int) Math.round(getStoredEnergy(stack) / (double) getEnergyCapacity(stack) * 13), 13);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		Long energy = stack.get(getEnergyComponent());
		if (energy == null) {
			energy = 0L;
		}

		MutableComponent energyFormatted = new MITooltips.Line(MIText.EnergyStored).arg(new MITooltips.NumberWithMax(energy, getEnergyCapacity(stack)), MITooltips.EU_MAXED_PARSER).build();
		String percentFormatted = String.valueOf((int) Math.floor(((float) energy / getEnergyCapacity(stack)) * 100));
		MutableComponent percentComponent = Component.literal(" ("+percentFormatted+"%)").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(16768637)));
		tooltipComponents.add(energyFormatted.append(percentComponent));
		tooltipComponents.add(Component.translatable("mi_armory.has_grid").withStyle(ChatFormatting.GRAY));
	}
}
