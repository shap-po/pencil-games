package com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;

public record InvalidMoveS2CPacket(int x, int y, Object lastState) implements Packet {
    public static final PacketType<InvalidMoveS2CPacket> PACKET_TYPE = PacketType.s2c("invalid_move");

    @SuppressWarnings("unchecked")
    public <C> C getLastState() {
        return (C) lastState;
    }

    @Override
    public PacketType<InvalidMoveS2CPacket> getType() {
        return PACKET_TYPE;
    }
}
