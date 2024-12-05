package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.ui.Application;
import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.game.ServerGameFactoryRegistry;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;

import javax.swing.*;
import java.util.Objects;

public class StartGameMenu extends MenuScreen {
    public StartGameMenu(Application root) {
        super(root, false);
    }

    @Override
    protected void populate() {
        JComboBox<Identifier> gameTypes = new JComboBox<>();
        for (Identifier id : ServerGameFactoryRegistry.REGISTRY.getKeys()) {
            gameTypes.addItem(id);
        }
        add(gameTypes);


        addButton("Start Game", e -> {
            Identifier gameType = (Identifier) gameTypes.getSelectedItem();

            GameFactory<ServerGameLobby, Game<ServerGameLobby>> gameFactory = ServerGameFactoryRegistry.REGISTRY.get(gameType);
            Objects.requireNonNull(gameFactory, "No game factory found for " + gameType + " how did you manage to break this?");
            Objects.requireNonNull(PencilGamesServer.serverGameLobby, "Server lobby is not created.");

            PencilGamesServer.serverGameLobby.startGame(gameFactory);
        });

        addButtonWithConfirm(
            "Disconnect",
            "Are you sure you want to close the server?",
            e -> {
                Objects.requireNonNull(PencilGamesServer.serverGameLobby, "Server lobby is not created.");
                PencilGamesServer.serverGameLobby.disconnect();
            }
        );
    }
}
