package org.ranch.mi_armory;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ranch.mi_armory.blocks.EquipmentGridBlock;

public class MiArmoryBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MiArmory.MODID);

	public static final DeferredBlock<EquipmentGridBlock> EQUIPMENT_GRID_BLOCK = BLOCKS.register(
			"equipment_grid_table",
			registryName -> new EquipmentGridBlock(BlockBehaviour.Properties.of()
					.destroyTime(2.0f)
					.explosionResistance(2.0f)
					.sound(SoundType.METAL)
			));

	public static void register(IEventBus modEventBus) {
		BLOCKS.register(modEventBus);
	}
}
