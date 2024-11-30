package com.github.shap_po.pencilgames.common.network.packet.s2c.player;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

import java.util.List;
import java.util.UUID;

public record SyncPlayerListS2CPacket(List<UUID> playerIds) implements Packet {
    public static final PacketType<SyncPlayerListS2CPacket> PACKET_TYPE = PacketType.s2c("sync_player_list");

    @Override
    public PacketType<SyncPlayerListS2CPacket> getType() {
        return PACKET_TYPE;
    }
}

