package com.github.shap_po.pencilgames.common.game.type;

import com.github.shap_po.pencilgames.common.game.GameLobby;

import java.util.UUID;

/**
 * Abstract class representing a game type.
 *
 * @implSpec Classes extending this class should have a static void register() method.
 * This method must register all the necessary packets to the packet type registry.
 */
public abstract class GameType {
    protected final GameLobby<?> lobby;

    public GameType(GameLobby<?> lobby) {
        this.lobby = lobby;
    }

    abstract public void start();

    public abstract void addPlayer(UUID playerId) throws IllegalStateException;
}
