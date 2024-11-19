package com.github.shap_po.pencilgames.common.network.packet.s2c.player;


import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

import java.util.UUID;

public record PlayerMessageS2CPacket(UUID playerId, String message) implements Packet {
    public static final PacketType<PlayerMessageS2CPacket> PACKET_TYPE = PacketType.s2c("player_message");

    @Override
    public PacketType<PlayerMessageS2CPacket> getType() {
        return PACKET_TYPE;
    }
}
