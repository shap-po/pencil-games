package com.github.shap_po.pencilgames.common.network.packet;

import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.common.network.PacketReceiverRegistry;
import com.github.shap_po.pencilgames.common.util.Identifier;

/**
 * The type of {@link Packet}.
 *
 * @param side network side that accepts the packet
 * @param id   unique identifier
 * @param <P>  the packet. Although it is not used in this class, it helps receiver to know what kind of packet it is
 * @see PacketReceiverRegistry
 */
public record PacketType<P extends Packet>(NetworkSide side, Identifier id) {
    @Override
    public String toString() {
        return this.side + "/" + this.id;
    }

    public static <T extends Packet> PacketType<T> s2c(Identifier id) {
        return new PacketType<>(NetworkSide.CLIENT, id);
    }

    public static <T extends Packet> PacketType<T> s2c(String id) {
        return new PacketType<>(NetworkSide.CLIENT, Identifier.of(id));
    }

    public static <T extends Packet> PacketType<T> c2s(Identifier id) {
        return new PacketType<>(NetworkSide.SERVER, id);
    }

    public static <T extends Packet> PacketType<T> c2s(String id) {
        return new PacketType<>(NetworkSide.SERVER, Identifier.of(id));
    }
}
