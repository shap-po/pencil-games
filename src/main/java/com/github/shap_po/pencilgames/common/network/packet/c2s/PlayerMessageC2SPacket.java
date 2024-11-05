package com.github.shap_po.pencilgames.common.network.packet.c2s;


import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

public record PlayerMessageC2SPacket(String message) implements Packet {
    public static final PacketType PACKET_TYPE = PacketType.c2s("player_message");

    @Override
    public PacketType getType() {
        return PACKET_TYPE;
    }
}
