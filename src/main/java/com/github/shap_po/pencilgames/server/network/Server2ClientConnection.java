package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import org.slf4j.Logger;

import java.net.Socket;

/**
 * Handles a connection to a client.
 */
public class Server2ClientConnection extends ConnectionHandler {
    public static Logger LOGGER = LoggerUtils.getLogger();

    public Server2ClientConnection(Socket socket) {
        super(socket, NetworkSide.SERVER);
        PencilGamesServer.LOGGER.info("Accepted connection from {}", socket.getInetAddress());
    }
}
