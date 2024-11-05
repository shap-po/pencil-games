package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.server.PencilGamesServer;

import java.net.Socket;

/**
 * Handles a connection to a client.
 */
public class Server2ClientConnection extends ConnectionHandler {
    public Server2ClientConnection(Socket socket) {
        super(socket, NetworkSide.SERVER);
        PencilGamesServer.LOGGER.info("Accepted connection from {}", socket.getInetAddress());
    }
}
