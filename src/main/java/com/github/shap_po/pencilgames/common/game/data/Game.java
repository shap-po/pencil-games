package com.github.shap_po.pencilgames.common.game.data;

import com.github.shap_po.pencilgames.common.game.GameLobby;

/**
 * A base class for all game types.
 */
public abstract class Game<L extends GameLobby<?>> {
    protected final L lobby;
    protected int currentPlayerIndex = 0;

    public Game(L lobby) {
        this.lobby = lobby;
    }

    /**
     * Runs when the game starts.
     *
     * @implNote this method must register all packet types and handlers used by the game
     */
    abstract public void onStart();

    /**
     * Runs when the game ends.
     *
     * @implNote this method must unregister all packet types and handlers used by the game
     */
    abstract public void onEnd();

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void nextPlayer() {
        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.lobby.getPlayers().size();
    }
}
