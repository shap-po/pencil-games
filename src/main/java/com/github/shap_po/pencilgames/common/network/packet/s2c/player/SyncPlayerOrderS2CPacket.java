package com.github.shap_po.pencilgames.common.network.packet.s2c.player;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

import java.util.Collection;
import java.util.UUID;

public record SyncPlayerOrderS2CPacket(Collection<UUID> playersOrder) implements Packet {
    public static PacketType<SyncPlayerOrderS2CPacket> PACKET_TYPE = PacketType.s2c("sync_player_order");

    @Override
    public PacketType<SyncPlayerOrderS2CPacket> getType() {
        return PACKET_TYPE;
    }
}
