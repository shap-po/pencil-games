package com.github.shap_po.pencilgames.common.network.packet.type;

import com.github.shap_po.pencilgames.common.network.packet.Packet;

import java.util.UUID;

public class PlayerLeavePacket extends Packet<UUID> {
    public static final String ID = "player_leave";

    public PlayerLeavePacket(UUID uuid) {
        super(uuid);
    }

    @Override
    public String getId() {
        return ID;
    }
}
