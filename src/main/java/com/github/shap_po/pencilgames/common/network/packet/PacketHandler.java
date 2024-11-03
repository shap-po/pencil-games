package com.github.shap_po.pencilgames.common.network.packet;

@FunctionalInterface
public interface PacketHandler {
    /**
     * @throws ClassCastException if the packet is not of the expected type
     */
    void handle(Packet<?> packet, Context context) throws ClassCastException;

    record Context() {
    }
}
