package org.ranch.mi_armory.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.ranch.mi_armory.client.MiArmoryClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Shadow
	private ClientLevel level;

	@Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", shift = At.Shift.AFTER, ordinal = 2))
	private void injected(CallbackInfo ci, @Local PoseStack poseStack, @Local(argsOnly = true) Camera camera) {
		MiArmoryClient.skyFlashRenderer.renderFlashes(poseStack, level, camera);
	}
}
