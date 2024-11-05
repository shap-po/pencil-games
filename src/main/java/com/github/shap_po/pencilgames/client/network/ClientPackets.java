package com.github.shap_po.pencilgames.client.network;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.common.network.PacketReceiverRegistry;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.PlayerConnectS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.PlayerDisconnectS2CPacket;

public class ClientPackets {
    public static final PacketReceiverRegistry<ClientPacketContext> REGISTRY = new PacketReceiverRegistry<>(NetworkSide.CLIENT);

    static {
        REGISTRY.registerPacketType(PlayerConnectS2CPacket.PACKET_TYPE);
        REGISTRY.registerPacketType(PlayerDisconnectS2CPacket.PACKET_TYPE);

        // TODO: implement actual packet handlers
        REGISTRY.registerReceiver(PlayerConnectS2CPacket.PACKET_TYPE, (p, c) -> {
            PencilGamesClient.LOGGER.info("Player connected: {}", p.playerId());
        });
        REGISTRY.registerReceiver(PlayerDisconnectS2CPacket.PACKET_TYPE, (p, c) -> {
            PencilGamesClient.LOGGER.info("Player disconnected: {}", p.playerId());
        });
    }

    public record ClientPacketContext() {
    }
}
