package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.common.network.PacketReceiverRegistry;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;
import com.github.shap_po.pencilgames.common.network.packet.c2s.PlayerMessageC2SPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.PlayerMessageS2CPacket;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.game.player.ServerPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiConsumer;

/**
 * Packet registry for the server.
 */
public class ServerPackets {
    public static final PacketReceiverRegistry<ServerPacketContext> REGISTRY = new PacketReceiverRegistry<>(NetworkSide.SERVER);

    static {
        // Register base C2S packet types
        registerPacketType(PlayerMessageC2SPacket.PACKET_TYPE);

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

    /**
     * Context provided to packet handlers on the server side with a packet.
     *
     * @param lobby  server lobby
     * @param player player that sent the packet
     */
    public record ServerPacketContext(ServerGameLobby lobby, ServerPlayer player) {
    }
}
