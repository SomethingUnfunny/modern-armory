package org.ranch.mi_armory.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.client.MiArmoryClient;
import org.ranch.mi_armory.client.rendering.nuke.NukeExplosionType;

public record PacketDetonation(BlockPos location, int strength,
                               NukeExplosionType explosionType) implements CustomPacketPayload {
	public static final Type<PacketDetonation> TYPE = new Type<>(MiArmory.location("detonation"));

	public static final StreamCodec<FriendlyByteBuf, PacketDetonation> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			PacketDetonation::location,
			ByteBufCodecs.VAR_INT,
			PacketDetonation::strength,
			NeoForgeStreamCodecs.enumCodec(NukeExplosionType.class),
			PacketDetonation::explosionType,
			PacketDetonation::new
	);

	public static void sendToAllPlayers(BlockPos location, int strength, ServerLevel dimension, NukeExplosionType explosionType) {
		PacketDistributor.sendToPlayersInDimension(dimension, new PacketDetonation(location, strength, explosionType));
	}

	@Override
	public @NotNull Type<PacketDetonation> type() {
		return TYPE;
	}

	public static void handle(PacketDetonation payload, IPayloadContext ctx) {
		MiArmoryClient.skyFlashRenderer.addFlash(payload, ctx);
		// todo maybe a low rumble depending on distance
	}
}
