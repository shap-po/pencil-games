package com.github.shap_po.pencilgames.common.network.packet.type;

import com.github.shap_po.pencilgames.common.network.packet.Packet;

public class MessagePacket extends Packet<String> {
    public static final String ID = "message";

    public MessagePacket(String message) {
        super(message);
    }

    @Override
    public String getId() {
        return ID;
    }
}
