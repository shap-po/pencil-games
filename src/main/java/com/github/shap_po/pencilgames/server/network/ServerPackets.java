package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.common.network.PacketReceiverRegistry;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;
import com.github.shap_po.pencilgames.common.network.packet.c2s.PlayerMessageC2SPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.PlayerMessageS2CPacket;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiConsumer;

public class ServerPackets {
    public static final PacketReceiverRegistry<ServerPacketContext> REGISTRY = new PacketReceiverRegistry<>(NetworkSide.SERVER);

    static {
        registerPacketType(PlayerMessageC2SPacket.PACKET_TYPE);

        // TODO: implement actual packet handlers

        // Broadcast player messages to all players
        registerReceiver(PlayerMessageC2SPacket.PACKET_TYPE, (packet, context) -> {
            PencilGamesServer.LOGGER.info("Received message from {}: {}", context.player.getId(), packet.message());
            context.lobby.broadcastPacket(new PlayerMessageS2CPacket(context.player.getId(), packet.message()));
        });
    }

    public static void registerPacketType(@NonNull PacketType<?> packetType) {
        REGISTRY.registerPacketType(packetType);
    }

    public static void unregisterPacketType(@NonNull PacketType<?> packetType) {
        REGISTRY.unregisterPacketType(packetType);
    }

    public static <P extends Packet> void registerReceiver(@NonNull PacketType<P> packetType, BiConsumer<P, ServerPacketContext> handler) {
        REGISTRY.registerReceiver(packetType, handler);
    }

    public record ServerPacketContext(ServerGameLobby lobby, ServerPlayer player) {
    }
}
