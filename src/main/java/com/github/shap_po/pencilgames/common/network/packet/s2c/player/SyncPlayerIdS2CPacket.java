package com.github.shap_po.pencilgames.common.network.packet.s2c.player;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

import java.util.UUID;

public record SyncPlayerIdS2CPacket(UUID playerId) implements Packet {
    public static final PacketType<SyncPlayerIdS2CPacket> PACKET_TYPE = PacketType.s2c("sync_player_id");

    @Override
    public PacketType<SyncPlayerIdS2CPacket> getType() {
        return PACKET_TYPE;
    }
}
