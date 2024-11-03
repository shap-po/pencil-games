package com.github.shap_po.pencilgames.common.game.type;

import com.github.shap_po.pencilgames.common.game.GameLobby;

import java.util.UUID;

public abstract class GameType {
    protected final GameLobby<?> lobby;

    public GameType(GameLobby<?> lobby) {
        this.lobby = lobby;
    }

    abstract public void start();

    public abstract void addPlayer(UUID playerId) throws IllegalStateException;
}
