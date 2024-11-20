package com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.c2s;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

public record PlayerMoveC2SPacket(int x, int y) implements Packet {
    public static final PacketType<PlayerMoveC2SPacket> PACKET_TYPE = PacketType.c2s("player_move");

    @Override
    public PacketType<PlayerMoveC2SPacket> getType() {
        return PACKET_TYPE;
    }
}
