package org.ranch.mi_armory;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MiArmoryAttachmentTypes {
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MiArmory.MODID);

	public static final Supplier<AttachmentType<Float>> ENERGY_SHIELD = ATTACHMENT_TYPES.register(
			"energy_shield", () -> AttachmentType.builder(() -> 0.0f).serialize(Codec.FLOAT).build()
	);

	public static void register(IEventBus modEventBus) {
		ATTACHMENT_TYPES.register(modEventBus);
	}
}
