package com.github.shap_po.pencilgames.common.network.packet;

import java.io.Serializable;

public interface Packet extends Serializable {
    PacketType<? extends Packet> getType();
}
