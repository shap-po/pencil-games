package com.github.shap_po.pencilgames.client.ui.screen.game;

import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.util.MenuPanel;
import com.github.shap_po.pencilgames.common.game.Game;

public class GameScreen<G extends Game<ClientGameLobby>> extends MenuPanel {
    private final G game;

    public GameScreen(GameWindow root, G game) {
        super(root);
        this.game = game;
    }

    public G getGame() {
        return game;
    }
}
