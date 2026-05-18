package org.ranch.mi_armory.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public record DetonatorSelections(List<DetonatorSelection> positions) {
	public static final Codec<DetonatorSelections> CODEC = RecordCodecBuilder.create(
			(instance) -> instance.group(
					DetonatorSelection.CODEC.listOf().fieldOf("targets").forGetter(DetonatorSelections::positions))
					.apply(instance, DetonatorSelections::new)
	);

	public List<BlockPos> getBlockPosList() {
		List<BlockPos> blockPosList = new ArrayList<>();
		positions.forEach(pos -> blockPosList.add(pos.getBlockPos()));
		return blockPosList;
	}
}
