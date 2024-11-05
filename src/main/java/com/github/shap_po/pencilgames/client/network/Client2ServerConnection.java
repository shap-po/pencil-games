package com.github.shap_po.pencilgames.client.network;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.common.network.packet.Packet;

import java.io.IOException;
import java.net.Socket;

public class Client2ServerConnection extends ConnectionHandler {
    public Client2ServerConnection(String host, int port) throws IOException {
        super(new Socket(host, port), NetworkSide.CLIENT);
        this.onPacket.register(this::receivePacket);
        PencilGamesClient.LOGGER.info("Connected to server at {}:{}", host, port);
    }

    public Client2ServerConnection(String host) throws IOException {
        this(host, DEFAULT_PORT);
    }

    public void receivePacket(Packet packet) {
        PencilGamesClient.LOGGER.info("Received packet of type {}", packet.getType());
    }
}
