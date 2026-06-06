package org.ranch.mi_armory.modular_armor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.ranch.mi_armory.items.DetonatorSelection;
import org.ranch.mi_armory.util.UnfunUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

public record Module(int width, int height, int powerDraw, List<AddedAttribute> attributes) {

	public record AddedAttribute(Holder<Attribute> attribute, double value) {
	}
}
