package com.github.shap_po.pencilgames.client;

import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import org.slf4j.Logger;

/**
 * The main entry point for the PencilGames client part.
 */
public class PencilGamesClient {
    public static final Logger LOGGER = LoggerUtils.getLogger();
    public static final GameWindow gameWindow = new GameWindow();
    public static final ClientGameLobby clientLobby = new ClientGameLobby();

    public static void main(String[] args) {
        // TODO: Parse arguments

        clientLobby.start();
        gameWindow.setVisible(true);
        LOGGER.info("Started client");
    }
}
