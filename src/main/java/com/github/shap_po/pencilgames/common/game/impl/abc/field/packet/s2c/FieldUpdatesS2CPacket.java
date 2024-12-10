package com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c;

import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;
import com.github.shap_po.pencilgames.common.util.Position;

import java.util.Map;

public record FieldUpdatesS2CPacket(Map<Position, ?> updates) implements Packet {
    public static final PacketType<FieldUpdatesS2CPacket> PACKET_TYPE = PacketType.s2c("field_updates");

    @SuppressWarnings({"unchecked"})
    public <C> Map<Position, C> getUpdates() {
        return (Map<Position, C>) updates;
    }

    @Override
    public PacketType<FieldUpdatesS2CPacket> getType() {
        return PACKET_TYPE;
    }
}
