package com.github.shap_po.pencilgames.common.network;

import com.github.shap_po.pencilgames.common.event.type.BiConsumerEvent;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;
import com.github.shap_po.pencilgames.common.registry.SimpleRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

/**
 * Registry for packet handlers
 *
 * @param <C> the context
 */
public class PacketReceiverRegistry<C> extends SimpleRegistry<PacketType<? extends Packet>, BiConsumerEvent<? extends Packet, C>> {
    private final NetworkSide side;
    private final List<PacketReceiverRegistry<C>> subRegistries = new CopyOnWriteArrayList<>();
    private final BiConsumerEvent<Packet, C> onAnyPacket = BiConsumerEvent.create();

    /**
     * Create a new packet receiver registry
     *
     * @param side the network side that accepts packets
     */
    public PacketReceiverRegistry(NetworkSide side) {
        this.side = side;
    }

    /**
     * Add a packet type to registry. A packet type must be registered before it can be used
     *
     * @param packetType the packet type
     */
    public void registerPacketType(@NonNull PacketType<?> packetType) {
        if (this.contains(packetType)) {
            throw new IllegalArgumentException("Packet type " + packetType + " is already registered");
        }
        assertSide(packetType);

        this.add(packetType, BiConsumerEvent.create());
    }

    /**
     * Remove a packet type from registry.
     * This also removes all packet handlers of the packet type
     *
     * @param packetType the packet type
     */
    public void unregisterPacketType(@NonNull PacketType<?> packetType) {
        this.remove(packetType);
    }

    /**
     * Add a packet handler for a specific packet type
     *
     * @param packetType the packet type
     * @param handler    the handler
     * @param <P>        the packet
     */
    public <P extends Packet> void registerReceiver(@NonNull PacketType<P> packetType, BiConsumer<P, C> handler) {
        assertSide(packetType);
        this.getEvent(packetType).register(handler);
    }

    /**
     * Remove all packet handlers of a specific type
     *
     * @param packetType the packet type
     */
    public void unregisterReceivers(@NonNull PacketType<?> packetType) {
        this.remove(packetType);
    }

    /**
     * Remove a packet handler of a specific type
     *
     * @param packetType the packet type
     * @param handler    the handler
     * @param <P>        the packet
     */
    public <P extends Packet> void unregisterReceiver(@NonNull PacketType<P> packetType, BiConsumer<P, C> handler) {
        this.getEvent(packetType).unregister(handler);
    }

    /**
     * Add a packet handler for any packet type
     *
     * @param handler the handler
     */
    public void registerReceiver(BiConsumer<Packet, C> handler) {
        onAnyPacket.register(handler);
    }

    /**
     * Remove a packet handler of any type
     *
     * @param handler the handler
     */
    public void unregisterReceiver(BiConsumer<Packet, C> handler) {
        onAnyPacket.unregister(handler);
    }

    /**
     * Add a sub registry for handling packets
     *
     * @param subRegistry the sub registry
     */
    public void registerSubRegistry(PacketReceiverRegistry<C> subRegistry) {
        this.subRegistries.add(subRegistry);
    }

    /**
     * Remove a sub registry
     *
     * @param subRegistry the sub registry
     */
    public void unregisterSubRegistry(PacketReceiverRegistry<C> subRegistry) {
        this.subRegistries.remove(subRegistry);
    }

    /**
     * Receive a packet
     *
     * @param packet  the packet
     * @param context the context
     * @param <P>     the packet
     * @throws IllegalArgumentException if the packet type is unknown
     */
    public <P extends Packet> void receive(P packet, C context) throws IllegalArgumentException {
        PacketType<P> packetType = getType(packet);
        assertSide(packetType);

        if (!this.contains(packetType)) {
            throw new IllegalArgumentException("Received packet of unknown type " + packetType);
        }

        // receive the packet of a specific type
        this.getEvent(packetType).accept(packet, context);

        // receive any packet
        this.onAnyPacket.accept(packet, context);

        // pass to sub registries
        for (PacketReceiverRegistry<C> subRegistry : this.subRegistries) {
            try {
                subRegistry.receive(packet, context);
            } catch (IllegalArgumentException ignored) {
                // sub registries are not required to handle all packet types
            }
        }
    }

    /**
     * Get the event for a specific packet type
     *
     * @param packetType the packet type
     * @param <P>        the packet
     * @return the event
     * @throws NullPointerException if the event does not exist
     */
    public <P extends Packet> BiConsumerEvent<P, C> getEvent(PacketType<P> packetType) throws NullPointerException {
        @SuppressWarnings({"unchecked"})
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
