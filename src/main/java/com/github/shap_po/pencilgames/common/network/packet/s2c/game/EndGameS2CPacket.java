package com.github.shap_po.pencilgames.common.network.packet.s2c.game;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public record EndGameS2CPacket(Collection<UUID> winners) implements Packet {
    public static final PacketType<EndGameS2CPacket> PACKET_TYPE = PacketType.s2c("end_game");

    @Override
    public PacketType<EndGameS2CPacket> getType() {
        return PACKET_TYPE;
    }
}
