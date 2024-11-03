package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.server.PencilGamesServer;

import java.net.Socket;

/**
 * Handles a connection to a client.
 */
public class Server2ClientConnection extends ConnectionHandler {
    public Server2ClientConnection(Socket socket) {
        super(socket, true);
//        this.onPacketReceived.register(this::receivePacket);

        PencilGamesServer.LOGGER.info("Accepted connection from {}", socket.getInetAddress());
    }

    @Override
    public void close() {
        super.close();
        PencilGamesServer.LOGGER.info("Closing connection to {}", socket.getInetAddress());
    }
}
