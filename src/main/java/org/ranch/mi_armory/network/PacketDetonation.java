package org.ranch.mi_armory.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.ranch.mi_armory.MiArmory;

public record PacketDetonation(ChunkPos location, int strength) implements CustomPacketPayload {
	public static final Type<PacketDetonation> TYPE = new Type<>(MiArmory.location("detonation"));

	public static final StreamCodec<FriendlyByteBuf, PacketDetonation> STREAM_CODEC = StreamCodec.composite(
			NeoForgeStreamCodecs.CHUNK_POS,
			PacketDetonation::location,
			ByteBufCodecs.VAR_INT,
			PacketDetonation::strength,
			PacketDetonation::new
	);

	public static void sendToAllPlayers(ChunkPos location, int strength, ServerLevel dimension) {
		PacketDistributor.sendToPlayersInDimension(dimension, new PacketDetonation(location, strength));
	}

	@Override
	public @NotNull Type<PacketDetonation> type() {
		return TYPE;
	}

	public static void handle(PacketDetonation payload, IPayloadContext ctx) {
		ctx.player().displayClientMessage(Component.literal("goot"), true);
		// goot
	}
}
