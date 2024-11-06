package com.github.shap_po.pencilgames.common.network.packet;

import java.io.Serializable;

/**
 * Base class for all packets.
 * It's important that the type parameter of a packet type is a packet it associates with
 */
public interface Packet extends Serializable {
    PacketType<? extends Packet> getType();
}
