package com.github.shap_po.pencilgames.common.network;

import com.github.shap_po.pencilgames.common.event.type.ConsumerEvent;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;
import com.github.shap_po.pencilgames.common.registry.SimpleRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.Consumer;

public class PacketReceiverRegistry extends SimpleRegistry<PacketType, ConsumerEvent<? extends Packet>> {
    private final NetworkSide side;

    public PacketReceiverRegistry(NetworkSide side) {
        this.side = side;
    }

    public void registerPacketType(@NonNull PacketType packetType) {
        if (this.contains(packetType)) {
            throw new IllegalArgumentException("Id " + packetType + " already exists");
        }
        assertSide(packetType);

        this.add(packetType, ConsumerEvent.create());
    }

    public <T extends Packet> void registerReceiver(@NonNull PacketType packetType, Consumer<T> handler) {
        assertSide(packetType);
        this.<T>getEvent(packetType).register(handler);
    }

    public <T extends Packet> void receive(T packet) {
        if (!this.contains(packet.getType())) {
            throw new IllegalArgumentException("Received packet of unknown type " + packet.getType());
        }
        assertSide(packet.getType());

        this.<T>getEvent(packet.getType()).accept(packet);
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Packet> ConsumerEvent<T> getEvent(PacketType packetType) {
        ConsumerEvent<T> event = (ConsumerEvent<T>) this.get(packetType);
        Objects.requireNonNull(event, "Packet handler of type " + packetType + " does not exist");

        return event;
    }

    /**
     * Make sure the packet type matches the side. Helps with debugging
     *
     * @param packetType the packet type
     * @throws IllegalArgumentException if the side does not match
     */
    private void assertSide(PacketType packetType) throws IllegalArgumentException {
        if (!this.side.equals(packetType.side())) {
            throw new IllegalArgumentException("Packet type " + packetType + " does not match side " + this.side);
        }
    }
}
