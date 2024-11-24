package com.github.shap_po.pencilgames.client.game;

import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.screen.game.GameScreen;
import com.github.shap_po.pencilgames.common.game.Game;

public abstract class ClientGame extends Game<ClientGameLobby> {
    public ClientGame(ClientGameLobby lobby) {
        super(lobby);
    }

    abstract public GameScreen<?> getGameScreen(GameWindow root);
}
