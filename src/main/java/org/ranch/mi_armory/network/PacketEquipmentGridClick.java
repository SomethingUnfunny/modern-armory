package org.ranch.mi_armory.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.ranch.mi_armory.MiArmory;
import org.ranch.mi_armory.client.MiArmoryClient;
import org.ranch.mi_armory.client.rendering.nuke.NukeExplosionType;
import org.ranch.mi_armory.modular_armor.EquipmentGridContainerMenu;

public record PacketEquipmentGridClick(int x, int y, ClickType clickType, int syncId) implements CustomPacketPayload {
	public static final Type<PacketEquipmentGridClick> TYPE = new Type<>(MiArmory.location("equipment_grid_click"));

	public static final StreamCodec<FriendlyByteBuf, PacketEquipmentGridClick> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			PacketEquipmentGridClick::x,
			ByteBufCodecs.VAR_INT,
			PacketEquipmentGridClick::y,
			NeoForgeStreamCodecs.enumCodec(ClickType.class),
			PacketEquipmentGridClick::clickType,
			ByteBufCodecs.VAR_INT,
			PacketEquipmentGridClick::syncId,
			PacketEquipmentGridClick::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void sendToServer(int x, int y, ClickType clickType, int syncId) {
		PacketDistributor.sendToServer(new PacketEquipmentGridClick(x, y, clickType, syncId));
	}

	public static void handle(PacketEquipmentGridClick payload, IPayloadContext ctx) {
		AbstractContainerMenu menu = ctx.player().containerMenu;
		if (menu instanceof EquipmentGridContainerMenu gridMenu) {
			gridMenu.handleGridClick(payload.x, payload.y, payload.clickType);
		}
		// todo maybe a low rumble depending on distance
	}

	public enum ClickType {
		ADD,
		REMOVE,
	}
}
