package com.github.shap_po.pencilgames.common.network.packet.s2c.player;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

import java.util.UUID;

public record PlayerDisconnectS2CPacket(UUID playerId) implements Packet {
    public static final PacketType PACKET_TYPE = PacketType.s2c("player_disconnect");

    @Override
    public PacketType getType() {
        return PACKET_TYPE;
    }
}
