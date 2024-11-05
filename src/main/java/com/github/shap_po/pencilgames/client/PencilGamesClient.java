package com.github.shap_po.pencilgames.client;

import com.github.shap_po.pencilgames.client.network.Client2ServerConnection;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.common.network.packet.c2s.PlayerMessageC2SPacket;
import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * The main entry point for the PencilGames client part.
 */
public class PencilGamesClient {
    public static Logger LOGGER = LoggerUtils.getLogger();

    public static void main(String[] args) {
        try {
            boolean host = true;

            Client2ServerConnection connectionHandler;
            if (host) {
                connectionHandler = host();
            } else {
                connectionHandler = connect("localhost");
            }

            if (connectionHandler == null) {
                return;
            }

            connect(connectionHandler);
        } catch (Exception e) {
            LOGGER.error("An error occurred", e);
        }
    }

    private static Client2ServerConnection connect(String host) {
        try {
            return new Client2ServerConnection(host);
        } catch (IOException e) {
            LOGGER.error("Failed to connect to server", e);
            return null;
        }
    }

    private static Client2ServerConnection host() {
        ServerGameLobby serverGameLobby;
        try {
            serverGameLobby = new ServerGameLobby();
        } catch (IOException e) {
            LOGGER.error("Failed to start server", e);
            return null;
        }
        serverGameLobby.start();

        return connect("localhost");
    }

    private static void connect(Client2ServerConnection connectionHandler) {
        ClientGameLobby clientGameLobby = new ClientGameLobby(connectionHandler);

        clientGameLobby.sendPacket(new PlayerMessageC2SPacket("Hello!"));

        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            while (scanner.hasNextLine() && connectionHandler.isAlive()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                if (!clientGameLobby.isAlive()) {
                    break;
                }
                clientGameLobby.sendPacket(new PlayerMessageC2SPacket(line));
            }
        }
    }
}
