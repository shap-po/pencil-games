package com.github.shap_po.pencilgames.common.network;

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
