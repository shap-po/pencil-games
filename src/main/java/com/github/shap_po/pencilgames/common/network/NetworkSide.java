package com.github.shap_po.pencilgames.common.network;

/**
 * Network side an action is performed on.
 * This allows both identifying {@link com.github.shap_po.pencilgames.common.network.packet.PacketType} and {@link ConnectionHandler} side.
 */
public enum NetworkSide {
    SERVER("server"),
    CLIENT("client");

    private final String name;

    NetworkSide(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
