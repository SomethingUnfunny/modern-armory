package org.ranch.mi_armory.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.ranch.mi_armory.modular_armor.ModuleList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", shift = At.Shift.BEFORE, ordinal = 0))
	private void addModuleTooltip(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
		if (ModuleList.hasItem(((ItemStack) (Object) this).getItem())) {
			MutableComponent component = Component.translatable("mi_armory.fits_in." + ModuleList.getFromItem(((ItemStack) (Object) this).getItem()).validSlots.getSerializedName());
			list.add(Component.translatable("mi_armory.has_module").append(" ").append(component).withStyle(ChatFormatting.GRAY));
		}
	}
}
