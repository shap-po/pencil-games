package com.github.shap_po.pencilgames.common.network.packet;

import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.common.util.Identifier;

public record PacketType(NetworkSide side, Identifier id) {
    @Override
    public String toString() {
        return this.side + "/" + this.id;
    }

    public static PacketType s2c(Identifier id) {
        return new PacketType(NetworkSide.CLIENT, id);
    }

    public static PacketType s2c(String id) {
        return new PacketType(NetworkSide.CLIENT, Identifier.of(id));
    }

    public static PacketType c2s(Identifier id) {
        return new PacketType(NetworkSide.SERVER, id);
    }

    public static PacketType c2s(String id) {
        return new PacketType(NetworkSide.SERVER, Identifier.of(id));
    }
}
