package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.common.network.PacketReceiverRegistry;
import com.github.shap_po.pencilgames.common.network.packet.c2s.PlayerMessageC2SPacket;
import com.github.shap_po.pencilgames.server.PencilGamesServer;

public class ServerPackets {
    public static final PacketReceiverRegistry<ServerPacketContext> REGISTRY = new PacketReceiverRegistry<>(NetworkSide.SERVER);

    static {
        REGISTRY.registerPacketType(PlayerMessageC2SPacket.PACKET_TYPE);

        // TODO: implement actual packet handlers
        REGISTRY.registerReceiver(PlayerMessageC2SPacket.PACKET_TYPE, (packet, context) -> {
            PencilGamesServer.LOGGER.info("Received message from {}: {}", context.player.getId(), packet.message());
        });
    }

    public record ServerPacketContext(ServerGameLobby lobby, ServerPlayer player) {
    }
}
