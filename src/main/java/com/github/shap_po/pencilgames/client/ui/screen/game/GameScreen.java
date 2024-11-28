package com.github.shap_po.pencilgames.client.ui.screen.game;

import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.util.ContentPanel;
import com.github.shap_po.pencilgames.common.game.Game;

public abstract class GameScreen<G extends Game<ClientGameLobby>> extends ContentPanel {
    protected final G game;

    public GameScreen(GameWindow root, G game) {
        super(root, false);

        // most game screens depend on the game, so set it before populating
        this.game = game;
        populate();
    }

    public G getGame() {
        return game;
    }
}
