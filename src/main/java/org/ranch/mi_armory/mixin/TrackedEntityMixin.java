package org.ranch.mi_armory.mixin;

import net.minecraft.world.entity.Entity;
import org.ranch.mi_armory.MiArmoryEntities;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
		targets = {"net.minecraft.server.level.ChunkMap$TrackedEntity"}
)
public class TrackedEntityMixin {
	@Final
	@Shadow
	Entity entity;

	@Redirect(
			method = {"updatePlayer"},
			at = @At(
					value = "INVOKE",
					target = "Ljava/lang/Math;min(II)I"
			)
	)
	public int min(int a, int b) {
		return this.entity.getType() == MiArmoryEntities.TOREX.get() ? 999 : Math.min(a, b);
	}
}
