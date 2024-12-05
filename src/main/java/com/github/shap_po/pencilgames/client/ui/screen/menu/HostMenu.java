package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.Application;
import com.github.shap_po.pencilgames.client.ui.util.NumberField;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;

import javax.swing.*;
import java.io.IOException;

public class HostMenu extends MenuScreen {
    public HostMenu(Application root) {
        super(root);
    }

    @Override
    protected void populate() {
        add(new JLabel("Port:"));
        NumberField portField = new NumberField(String.valueOf(ConnectionHandler.DEFAULT_PORT), 10);
        add(portField);

        addButton("Host", e -> {
            int port = portField.getValue();

            try {
                if (PencilGamesServer.serverGameLobby != null) {
                    PencilGamesServer.serverGameLobby.disconnect();
                }
                PencilGamesServer.serverGameLobby = new ServerGameLobby(port);
                PencilGamesServer.serverGameLobby.start();
            } catch (IOException ex) {
                PencilGamesServer.LOGGER.error("Failed to create server", ex);
                return;
            }
            root.setContentState(Application.ScreenState.START_GAME_MENU);

            try {
                PencilGamesClient.CLIENT_LOBBY.connect("localhost");
            } catch (IOException ex) {
                PencilGamesClient.LOGGER.error("Failed to connect to server", ex);
                PencilGamesServer.serverGameLobby.disconnect();
            }
        });
    }
}
