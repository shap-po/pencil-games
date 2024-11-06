package com.github.shap_po.pencilgames.common.network.packet.s2c.game;

import com.github.shap_po.pencilgames.common.data.Settings;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;
import com.github.shap_po.pencilgames.common.util.Identifier;

public record StartGameS2CPacket(Identifier gameFactoryId, Settings.Instance settings) implements Packet {
    public static final PacketType<StartGameS2CPacket> PACKET_TYPE = PacketType.s2c("start_game");

    @Override
    public PacketType<StartGameS2CPacket> getType() {
        return PACKET_TYPE;
    }
}
