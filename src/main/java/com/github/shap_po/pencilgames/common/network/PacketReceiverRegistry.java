package com.github.shap_po.pencilgames.common.network;

import com.github.shap_po.pencilgames.common.event.type.BiConsumerEvent;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;
import com.github.shap_po.pencilgames.common.registry.SimpleRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Registry for packet handlers
 *
 * @param <C> the context
 */
public class PacketReceiverRegistry<C> extends SimpleRegistry<PacketType<? extends Packet>, BiConsumerEvent<? extends Packet, C>> {
    private final NetworkSide side;

    public PacketReceiverRegistry(NetworkSide side) {
        this.side = side;
    }

    public  void registerPacketType(@NonNull PacketType<?> packetType) {
        if (this.contains(packetType)) {
            throw new IllegalArgumentException("Id " + packetType + " already exists");
        }
        assertSide(packetType);

        this.add(packetType, BiConsumerEvent.create());
    }

    public <P extends Packet> void registerReceiver(@NonNull PacketType<P> packetType, BiConsumer<P, C> handler) {
        assertSide(packetType);
        this.getEvent(packetType).register(handler);
    }

    public <P extends Packet> void register(@NonNull PacketType<P> packetType, @NonNull BiConsumer<P, C> handler) {
        registerPacketType(packetType);
        registerReceiver(packetType, handler);
    }

    public <P extends Packet> void receive(P packet, C context) {
        PacketType<P> packetType = getType(packet);
        if (!this.contains(packetType)) {
            throw new IllegalArgumentException("Received packet of unknown type " + packetType);
        }
        assertSide(packetType);

        this.getEvent(packetType).accept(packet, context);
    }

    @SuppressWarnings({"unchecked"})
    public <P extends Packet> BiConsumerEvent<P, C> getEvent(PacketType<P> packetType) {
        BiConsumerEvent<P, C> event = (BiConsumerEvent<P, C>) this.get(packetType);
        Objects.requireNonNull(event, "Packet handler of type " + packetType + " does not exist");

        return event;
    }

    @SuppressWarnings({"unchecked"})
    private <P extends Packet> PacketType<P> getType(Packet packet) {
        return (PacketType<P>) packet.getType();
    }

    /**
     * Make sure the packet type matches the side. Helps with debugging
     *
     * @param packetType the packet type
     * @throws IllegalArgumentException if the side does not match
     */
    private <P extends Packet> void assertSide(PacketType<P> packetType) throws IllegalArgumentException {
        if (!this.side.equals(packetType.side())) {
            throw new IllegalArgumentException("Packet type " + packetType + " does not match side " + this.side);
        }
    }
}
