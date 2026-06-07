package org.ranch.mi_armory.modular_armor.custom_modules;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.ranch.mi_armory.modular_armor.Module;

import java.util.List;

public class SolarPanelModule extends Module {

	private final double multiplier;
	private final int tickDelay;

	public SolarPanelModule(String id, int width, int height, double multiplier, int tickDelay) {
		super(id, width, height, 0, List.of(), EquipmentSlotGroup.HEAD);
		this.multiplier = multiplier;
		this.tickDelay = tickDelay;
	}

	@Override
	public long powerGen(Level level, Player player) {
		int sunlight = level.getBrightness(LightLayer.SKY, BlockPos.containing(player.getEyePosition()));
		if (level.getGameTime() % tickDelay == 0) {
			return (long) (sunlight * Math.max(11 - level.getSkyDarken(), 0) * multiplier);
		} else {
			return 0;
		}
	}
}
