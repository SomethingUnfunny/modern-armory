package org.ranch.mi_armory;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MiArmorySounds {

	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MiArmory.MODID);
	;
	public static final DeferredHolder<SoundEvent, SoundEvent> NUCLEAR_EXPLOSION = SOUND_EVENTS.register("nuclear_explosion", () -> SoundEvent.createFixedRangeEvent(MiArmory.location("nuclear_explosion"), 16.0F));
	public static final DeferredHolder<SoundEvent, SoundEvent> EMI = SOUND_EVENTS.register("emi", () -> SoundEvent.createFixedRangeEvent(MiArmory.location("emi"), 16.0F));

	public static void register(IEventBus modEventBus) {
		SOUND_EVENTS.register(modEventBus);
	}
}
