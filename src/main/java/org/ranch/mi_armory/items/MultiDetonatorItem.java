package org.ranch.mi_armory.items;

import aztech.modern_industrialization.MIBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiDetonatorItem extends DetonatorItem {
	public MultiDetonatorItem(Properties properties) {
		super(properties);
	}

	protected void selectNuke(BlockPos pos, ItemStack stack) {
		DetonatorSelections selections = stack.get(MiArmoryComponents.DETONATOR_SELECTIONS);
		DetonatorSelection selection = new DetonatorSelection(pos.getX(), pos.getY(), pos.getZ());
		List<DetonatorSelection> newArr;
		if (selections == null) {
			newArr = new ArrayList<>();
		} else {
			newArr =new ArrayList<>( selections.positions());
		}
		newArr.add(selection);
		stack.set(MiArmoryComponents.DETONATOR_SELECTIONS, new DetonatorSelections(newArr));
	}

	protected InteractionResultHolder<ItemStack> detonate(ItemStack stack, Level level, Player player, InteractionHand usedHand) {
		if (stack.get(MiArmoryComponents.DETONATOR_SELECTIONS) == null) {
			return InteractionResultHolder.pass(stack);
		} else {
			for (BlockPos selected : stack.get(MiArmoryComponents.DETONATOR_SELECTIONS).getBlockPosList()) {
				if (level.getBlockState(selected).getBlock() == MIBlock.NUKE.get()) {
					level.setBlock(selected, Blocks.AIR.defaultBlockState(), 3);
					createExplosion(level.getBlockState(selected), selected, player, level);
				}
			}

			stack.remove(MiArmoryComponents.DETONATOR_SELECTIONS);

			return InteractionResultHolder.success(stack);
		}
	}

	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (stack.get(MiArmoryComponents.DETONATOR_SELECTIONS) != null) {
			tooltip.add(Component.translatable("multi_detonator.tooltip").withColor(ChatFormatting.GRAY.getColor()));
			for (DetonatorSelection selection : stack.get(MiArmoryComponents.DETONATOR_SELECTIONS).positions()) {
				tooltip.add(getSelection(selection));
			}
		}
	}
}
