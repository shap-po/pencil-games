package com.github.shap_po.pencilgames.client;

import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.common.network.packet.c2s.PlayerMessageC2SPacket;
import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Scanner;

/**
 * The main entry point for the PencilGames client part.
 */
public class PencilGamesClient {
    public static Logger LOGGER = LoggerUtils.getLogger();
    public static final GameWindow gameWindow = new GameWindow();
    public static final ClientGameLobby clientLobby = new ClientGameLobby();
    private static final Scanner scanner = new Scanner(System.in); //TODO: remove

    public static void main(String[] args) {
        boolean isHost = args.length != 0;

        clientLobby.start();
        gameWindow.setVisible(true);
//        run(isHost);
    }

    private static void run(boolean host) {
        try {
            if (host) {
                try {
                    if (PencilGamesServer.serverGameLobby != null) {
                        PencilGamesServer.serverGameLobby.disconnect();
                    }

                    PencilGamesServer.serverGameLobby = new ServerGameLobby();
                    PencilGamesServer.serverGameLobby.start();
                } catch (IOException e) {
                    LOGGER.error("Failed to start server", e);
                }
            }

            clientLobby.connect("localhost");
            consoleLoop();
        } catch (Exception e) {
            LOGGER.error("An error occurred", e);
        }


        for (int i = 0; i < 3; i++) { // for debugging
            try {
                clientLobby.connect("localhost");
                consoleLoop();
            } catch (IOException e) {
                LOGGER.error("Failed to connect to server", e);
            }
        }
    }

    /**
     * Console loop for testing purposes.
     * Will be removed and replaced with a UI down the line
     */
    private static void consoleLoop() {
        while (scanner.hasNextLine() && clientLobby.isConnected()) {
            String line = scanner.nextLine();

            if (line.isEmpty()) {
                continue;
            }
            if (!clientLobby.isConnected()) {
                break;
            }
            clientLobby.sendPacket(new PlayerMessageC2SPacket(line));

            if (line.equals("stop")) {
                clientLobby.disconnect();
                break;
            }
        }
    }
}
