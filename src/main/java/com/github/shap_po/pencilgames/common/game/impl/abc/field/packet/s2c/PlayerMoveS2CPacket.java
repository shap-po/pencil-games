package com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

import java.util.UUID;

public record PlayerMoveS2CPacket(UUID playerID, int x, int y) implements Packet {
    public static final PacketType<PlayerMoveS2CPacket> PACKET_TYPE = PacketType.s2c("player_move");

    @Override
    public PacketType<PlayerMoveS2CPacket> getType() {
        return PACKET_TYPE;
    }
}
