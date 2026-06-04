package org.ranch.mi_armory;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ranch.mi_armory.items.DetonatorSelection;
import org.ranch.mi_armory.items.DetonatorSelections;

import java.util.function.Supplier;

public class MiArmoryAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MiArmory.MODID);

	public static final Holder<Attribute> SHOCKWAVE_RESISTANCE = ATTRIBUTES.register("shockwave_resistance", () -> new PercentageAttribute(
			"attributes.mi_armory.shockwave_resistance",
			0,
			0,
			1
	).setSentiment(Attribute.Sentiment.POSITIVE));

	public static void register(IEventBus modEventBus) {
		ATTRIBUTES.register(modEventBus);
	}
}
