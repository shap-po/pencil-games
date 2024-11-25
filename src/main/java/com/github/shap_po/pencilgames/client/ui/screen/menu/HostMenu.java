package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.util.NumberField;
import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.game.ServerGameFactoryRegistry;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class HostMenu extends MenuScreen {
    public HostMenu(GameWindow root) {
        super(root);
    }

    @Override
    protected void populate() {
        add(new JLabel("Port:"));
        NumberField portField = new NumberField(String.valueOf(ConnectionHandler.DEFAULT_PORT), 10);
        add(portField);

        JComboBox<Identifier> gameTypes = new JComboBox<>();
        for (Identifier id : ServerGameFactoryRegistry.REGISTRY.getKeys()) {
            gameTypes.addItem(id);
        }
        add(gameTypes);

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

            Identifier gameType = (Identifier) gameTypes.getSelectedItem();

            GameFactory<ServerGameLobby, Game<ServerGameLobby>> gameFactory = ServerGameFactoryRegistry.REGISTRY.get(gameType);
            Objects.requireNonNull(gameFactory, "No game factory found for " + gameType + " how did you manage to break this?");

            PencilGamesClient.LOGGER.info("Hosting {} game at {}", gameType, port);

            PencilGamesServer.serverGameLobby.setGameFactory(gameFactory);
            root.setContentState(GameWindow.ScreenState.START_GAME_MENU);

            try {
                PencilGamesClient.clientLobby.connect("localhost");
            } catch (IOException ex) {
                PencilGamesClient.LOGGER.error("Failed to connect to server", ex);
                PencilGamesServer.serverGameLobby.disconnect();
            }
        });
    }
}
