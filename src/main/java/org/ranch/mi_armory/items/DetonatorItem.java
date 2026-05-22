package org.ranch.mi_armory.items;

import aztech.modern_industrialization.MIBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
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

import java.util.List;

public class DetonatorItem extends Item {
	public DetonatorItem(Properties properties) {
		super(properties);
	}

	protected void selectNuke(BlockPos pos, ItemStack stack) {
		stack.set(MiArmoryComponents.DETONATOR_SELECTION, new DetonatorSelection(pos.getX(), pos.getY(), pos.getZ()));
	}

	protected InteractionResultHolder<ItemStack> detonate(ItemStack stack, Level level, Player player, InteractionHand usedHand) {
		if (stack.get(MiArmoryComponents.DETONATOR_SELECTION) == null) {
			return InteractionResultHolder.pass(stack);
		} else {
			BlockPos selected = stack.get(MiArmoryComponents.DETONATOR_SELECTION).getBlockPos();
			if (level.getBlockState(selected).getBlock() == MIBlock.NUKE.get()) {
				level.setBlock(selected, Blocks.AIR.defaultBlockState(), 11);
				MiArmory.decimate(selected, 140, false, player, level);
				stack.remove(MiArmoryComponents.DETONATOR_SELECTION);
			}

			return InteractionResultHolder.success(stack);
		}
	}

	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		Player player = context.getPlayer();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		Block block = world.getBlockState(pos).getBlock();
		if (!world.isClientSide && block == MIBlock.NUKE.get() && player.isShiftKeyDown()) {
			selectNuke(pos, stack);
			player.displayClientMessage(Component.translatable("detonator.message"), true);
			return InteractionResult.SUCCESS;
		}

		if (!player.isShiftKeyDown()) {
			return detonate(stack, world, player, context.getHand()).getResult();
		}

		return InteractionResult.PASS;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		if (!player.isShiftKeyDown()) {
			return detonate(player.getItemInHand(usedHand), level, player, usedHand);
		} else {
			return InteractionResultHolder.pass(player.getItemInHand(usedHand));
		}
	}

	protected Component getSelection(DetonatorSelection selection) {
		BlockPos selected = selection.getBlockPos();
		return Component.literal(selected.toShortString()).withColor(ChatFormatting.BLUE.getColor());
	}

	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (stack.get(MiArmoryComponents.DETONATOR_SELECTION) != null) {
			tooltip.add(Component.translatable("detonator.tooltip").withColor(ChatFormatting.GRAY.getColor()));
			tooltip.add(getSelection(stack.get(MiArmoryComponents.DETONATOR_SELECTION)));
		}
	}
}
