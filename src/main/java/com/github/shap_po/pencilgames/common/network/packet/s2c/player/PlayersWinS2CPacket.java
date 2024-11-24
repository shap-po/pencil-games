package com.github.shap_po.pencilgames.common.network.packet.s2c.player;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

import java.util.Set;
import java.util.UUID;

public record PlayersWinS2CPacket(Set<UUID> players) implements Packet {
    public static final PacketType<PlayersWinS2CPacket> PACKET_TYPE = PacketType.s2c("players_win");

    @Override
    public PacketType<PlayersWinS2CPacket> getType() {
        return PACKET_TYPE;
    }
}
