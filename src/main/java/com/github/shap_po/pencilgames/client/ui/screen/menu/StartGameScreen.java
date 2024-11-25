package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.server.PencilGamesServer;

public class StartGameScreen extends MenuScreen {
    public StartGameScreen(GameWindow root) {
        super(root);
    }

    @Override
    protected void populate() {
        addButton("Start Game", e -> {
            PencilGamesServer.serverGameLobby.startGame();
        });
    }
}
