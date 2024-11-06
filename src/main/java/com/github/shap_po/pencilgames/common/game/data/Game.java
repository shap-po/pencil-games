package com.github.shap_po.pencilgames.common.game.data;

import com.github.shap_po.pencilgames.common.game.GameLobby;

/**
 * A base class for all game types.
 *
 * @implSpec Classes extending this class should have a static void registerClient and registerServer methods
 * that will register all the necessary packets and their handlers.
 */
public abstract class Game {
    protected final GameLobby<?> lobby;

    public Game(GameLobby<?> lobby) {
        this.lobby = lobby;
    }
}
