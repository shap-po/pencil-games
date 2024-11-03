package com.github.shap_po.pencilgames.common.network.packet;

import com.github.shap_po.pencilgames.common.network.packet.type.MessagePacket;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {
    private static final Map<String, PacketHandler> packetHandlers = new HashMap<>();

    static {
        registerPacketHandler(MessagePacket.ID, (packet, context) -> {
            MessagePacket messagePacket = (MessagePacket) packet;
            System.out.println("Received message: " + messagePacket.getPayload());
        });
    }

    public static void registerPacketHandler(String type, PacketHandler handler) {
        packetHandlers.put(type, handler);
    }

    public static void handlePacket(Packet<?> packet, PacketHandler.Context context) {
        PacketHandler handler = packetHandlers.get(packet.getId());
        if (handler != null) {
            try {
                handler.handle(packet, context);
            } catch (ClassCastException ignored) {
            }
        }
    }
}
