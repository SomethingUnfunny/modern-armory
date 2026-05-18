package org.ranch.mi_armory.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public record DetonatorSelection(int x, int y, int z) {
	public static final Codec<DetonatorSelection> CODEC = RecordCodecBuilder.create(
			(instance) -> instance.group(
							Codec.INT.fieldOf("x").forGetter(DetonatorSelection::x),
							Codec.INT.fieldOf("y").forGetter(DetonatorSelection::y),
							Codec.INT.fieldOf("z").forGetter(DetonatorSelection::z))
					.apply(instance, DetonatorSelection::new)
	);

	public BlockPos getBlockPos() {
		return new BlockPos(this.x, this.y, this.z);
	}
}