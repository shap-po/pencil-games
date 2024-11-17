package com.github.shap_po.pencilgames.client.network;

import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.NetworkSide;

import java.io.IOException;
import java.net.Socket;

/**
 * Extension of {@link ConnectionHandler} for a connection to the server.
 */
public class Client2ServerConnection extends ConnectionHandler {
    public Client2ServerConnection(String host, int port) throws IOException {
        super(new Socket(host, port), NetworkSide.CLIENT);
    }
}
