package org.ranch.mi_armory.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.MiArmoryAttachmentTypes;

public record PacketSyncEnergyShield(float shield) implements CustomPacketPayload {
	public static final Type<PacketSyncEnergyShield> TYPE = new Type<>(MiArmory.location("sync_energy_shield"));

	public static final StreamCodec<FriendlyByteBuf, PacketSyncEnergyShield> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT,
			PacketSyncEnergyShield::shield,
			PacketSyncEnergyShield::new
	);

	public static void sendToPlayer(ServerPlayer player, float shield) {
		PacketDistributor.sendToPlayer(player, new PacketSyncEnergyShield(shield));
	}

	@Override
	public @NotNull Type<PacketSyncEnergyShield> type() {
		return TYPE;
	}

	public static void handle(PacketSyncEnergyShield payload, IPayloadContext ctx) {
		ctx.player().setData(MiArmoryAttachmentTypes.ENERGY_SHIELD, payload.shield);
	}
}
