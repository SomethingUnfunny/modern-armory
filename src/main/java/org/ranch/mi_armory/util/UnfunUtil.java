package org.ranch.mi_armory.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;

public class UnfunUtil {

	public static Holder<Attribute> getAttribute(String location) {
		return getAttribute(ResourceLocation.tryParse(location));
	}

	public static Holder<Attribute> getAttribute(ResourceLocation location) {
		return BuiltInRegistries.ATTRIBUTE.getHolder(location).orElseThrow();
	}

	public static Holder<Item> getItem(String location) {
		return getItem(ResourceLocation.tryParse(location));
	}

	public static Holder<Item> getItem(ResourceLocation location) {
		return BuiltInRegistries.ITEM.getHolder(location).orElseThrow();
	}
}
