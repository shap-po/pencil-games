package com.github.shap_po.pencilgames.client.network;

import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.common.network.PacketReceiverRegistry;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketType;
import com.github.shap_po.pencilgames.common.network.packet.s2c.game.StartGameS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiConsumer;

/**
 * Registry for all client packets
 */
public class ClientPackets {
    public static final PacketReceiverRegistry<ClientPacketContext> REGISTRY = new PacketReceiverRegistry<>(NetworkSide.CLIENT);

    static {
        registerPacketType(PlayerConnectS2CPacket.PACKET_TYPE);
        registerPacketType(PlayerDisconnectS2CPacket.PACKET_TYPE);
        registerPacketType(PlayerKickS2CPacket.PACKET_TYPE);
        registerPacketType(PlayerMessageS2CPacket.PACKET_TYPE);
        registerPacketType(PlayersWinS2CPacket.PACKET_TYPE);
        registerPacketType(SyncPlayerIdS2CPacket.PACKET_TYPE);
        registerPacketType(SyncPlayerListS2CPacket.PACKET_TYPE);

        registerPacketType(StartGameS2CPacket.PACKET_TYPE);
    }

    public static void registerPacketType(@NonNull PacketType<?> packetType) {
        REGISTRY.registerPacketType(packetType);
    }

    public static void unregisterPacketType(@NonNull PacketType<?> packetType) {
        REGISTRY.unregisterPacketType(packetType);
    }

    public static <P extends Packet> void registerReceiver(@NonNull PacketType<P> packetType, BiConsumer<P, ClientPacketContext> handler) {
        REGISTRY.registerReceiver(packetType, handler);
    }

    public record ClientPacketContext(ClientGameLobby lobby) {
    }
}
