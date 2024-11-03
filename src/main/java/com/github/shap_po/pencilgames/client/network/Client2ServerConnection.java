package com.github.shap_po.pencilgames.client.network;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.PacketTypes;

import java.io.IOException;
import java.net.Socket;

public class Client2ServerConnection extends ConnectionHandler {
    public Client2ServerConnection(String host, int port) throws IOException {
        super(new Socket(host, port), false);
        this.onPacketReceived.register(this::receivePacket);
        PencilGamesClient.LOGGER.info("Connected to server at {}:{}", host, port);
    }

    public Client2ServerConnection(String host) throws IOException {
        this(host, DEFAULT_PORT);
    }

    public void receivePacket(Packet<?> packet) {
        PencilGamesClient.LOGGER.info("Received packet of type {} from {} with payload: {}", packet.getId(), socket.getInetAddress(), packet.getPayload());
        switch (packet.getId()) {
            case PacketTypes.GAME_END -> {
                PencilGamesClient.LOGGER.info("Server requested to end the game");
                close();
            }
            default -> {
            }
        }
    }
}
