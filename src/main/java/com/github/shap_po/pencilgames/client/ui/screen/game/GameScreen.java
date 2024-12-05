package com.github.shap_po.pencilgames.client.ui.screen.game;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.client.ui.Application;
import com.github.shap_po.pencilgames.client.ui.util.ContentPanel;
import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.server.PencilGamesServer;

public abstract class GameScreen<G extends Game<ClientGameLobby>> extends ContentPanel {
    protected final G game;

    public GameScreen(Application root, G game) {
        super(root, false);

        // most game screens depend on the game, so set it before populating
        this.game = game;
        populate();

        addLeaveButton();
    }

    public G getGame() {
        return game;
    }

    protected void addLeaveButton() {
        if (PencilGamesClient.isHost()) {
            addButtonWithConfirm(
                "End Game",
                "Are you sure you want to end the game?",
                e -> {
                    if (PencilGamesServer.serverGameLobby != null) {
                        PencilGamesServer.serverGameLobby.endGame();
                    }
                    root.setContentState(Application.ScreenState.START_GAME_MENU);
                }
            );
        } else {
            addButtonWithConfirm(
                "Disconnect",
                "Are you sure you want to disconnect from the game? You won't be able to reconnect until the game ends.",
                e -> PencilGamesClient.CLIENT_LOBBY.disconnect()
            );
        }
    }
}
