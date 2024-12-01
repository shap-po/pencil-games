package com.github.shap_po.pencilgames.common.game;

import com.github.shap_po.pencilgames.common.game.player.Player;

import java.util.UUID;

/**
 * A base class for all game types.
 */
public abstract class Game<L extends GameLobby<?>> {
    protected final L lobby;
    protected int playerTurn = 0; // TODO: handle player disconnect

    public Game(L lobby) {
        this.lobby = lobby;
    }

    public L getLobby() {
        return this.lobby;
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

    public void start() {
        this.onStart();
    }

    public void end() {
        this.onEnd();
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void nextPlayer() {
        this.playerTurn = (this.playerTurn + 1) % this.lobby.getPlayerManager().getPlayerCount();
    }

    public boolean isPlayerTurn(UUID playerId) {
        Integer playerIndex = this.lobby.getPlayerManager().getPlayerIndex(playerId);
        if (playerIndex == null) return false;
        return this.playerTurn == playerIndex;
    }

    public boolean isPlayerTurn(Player player) {
        return isPlayerTurn(player.getId());
    }
}
