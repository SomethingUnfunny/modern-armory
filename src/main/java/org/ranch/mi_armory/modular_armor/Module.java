package org.ranch.mi_armory.modular_armor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;

import net.minecraft.world.entity.ai.attributes.Attribute;
import java.util.List;

public record Module(int width, int height, int powerDraw, List<AddedAttribute> attributes) {

	public record AddedAttribute(Holder<Attribute> attribute, double value) {

	}
}
