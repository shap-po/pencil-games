package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.game.ClientGame;
import com.github.shap_po.pencilgames.client.game.ClientGameFactoryRegistry;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.screen.game.GameScreen;
import com.github.shap_po.pencilgames.client.ui.util.MenuPanel;
import com.github.shap_po.pencilgames.client.ui.util.NumberField;
import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.server.game.ServerGameFactoryRegistry;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;

import javax.swing.*;
import java.util.Objects;

public class HostMenu extends MenuPanel {
    public HostMenu(GameWindow root) {
        super(root);

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
            Identifier gameType = (Identifier) gameTypes.getSelectedItem();

            // TODO: start server, set up game
            Game<ServerGameLobby> serverGame = Objects.requireNonNull(ServerGameFactoryRegistry.REGISTRY.get(gameType)).apply(null);
            ClientGame clientGame = Objects.requireNonNull(ClientGameFactoryRegistry.REGISTRY.get(gameType)).apply(PencilGamesClient.clientLobby);
            PencilGamesClient.LOGGER.info("Hosting {} game at {}", gameType, port);

            GameScreen<?> gameScreen = clientGame.getGameScreen(root);
            root.setGameScreen(gameScreen);
        });

        addBackButton();
    }
}
