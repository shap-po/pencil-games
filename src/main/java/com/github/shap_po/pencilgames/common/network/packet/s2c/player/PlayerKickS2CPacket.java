package com.github.shap_po.pencilgames.common.network.packet.s2c.player;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

import java.util.UUID;

public record PlayerKickS2CPacket(UUID playerId) implements Packet {
    public static final PacketType<PlayerKickS2CPacket> PACKET_TYPE = PacketType.s2c("player_kick");

    @Override
    public PacketType<PlayerKickS2CPacket> getType() {
        return PACKET_TYPE;
    }
}
