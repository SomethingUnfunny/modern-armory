package org.ranch.mi_armory.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.client.rendering.SkyFlashRenderer;

@EventBusSubscriber(
		value = {Dist.CLIENT},
		modid = MiArmory.MODID
)
public class MiArmoryClient {
	public static SkyFlashRenderer skyFlashRenderer;

	@SubscribeEvent
	private static void init(FMLConstructModEvent event) {
		skyFlashRenderer = new SkyFlashRenderer();
	}
}
