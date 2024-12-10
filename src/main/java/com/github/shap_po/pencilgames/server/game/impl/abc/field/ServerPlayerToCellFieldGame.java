package com.github.shap_po.pencilgames.server.game.impl.abc.field;

import com.github.shap_po.pencilgames.common.game.impl.abc.field.PlayerToCellFieldGame;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.game.player.ServerPlayer;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import com.google.common.collect.BiMap;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public abstract class ServerPlayerToCellFieldGame<C> extends ServerFieldGame<C> implements PlayerToCellFieldGame<C> {
    private final BiMap<UUID, C> playerToCellMap;
    private final C emptyCell;

    public ServerPlayerToCellFieldGame(ServerGameLobby lobby, GameField<C> gameField, boolean broadcastMoves, Function<Integer, C> cellOf, C emptyCell) {
        super(lobby, gameField, broadcastMoves);

        this.emptyCell = emptyCell;

        List<UUID> players = lobby.getPlayerManager().getPlayerOrder();
        this.playerToCellMap = PlayerToCellFieldGame.createPlayerToCellMap(players, cellOf);
    }

    public ServerPlayerToCellFieldGame(ServerGameLobby lobby, GameField<C> gameField, Function<Integer, C> cellOf, C emptyCell) {
        this(lobby, gameField, true, cellOf, emptyCell);
    }

    @Override
    public @Nullable C playerToCell(UUID playerId) {
        return playerToCellMap.getOrDefault(playerId, null);
    }

    @Override
    public @Nullable UUID cellToPlayer(C cell) {
        return playerToCellMap.inverse().getOrDefault(cell, null);
    }

    @Override
    public GameField<C> getGameField() {
        return gameField;
    }

    @Override
    public void handleMove(UUID playerId, int x, int y) {
        nextPlayer();

        C cell = playerToCell(playerId);
        gameField.set(x, y, cell);
        PencilGamesServer.LOGGER.debug("Player {} moved to ({}, {}) with cell {}", playerId, x, y, cell);
    }

    /**
     * Checks if the move is valid: is on the field, is the player's turn, and the cell is empty.
     *
     * @param player the player performing the move
     * @param x      x coordinate
     * @param y      y coordinate
     * @return true if the move is valid
     */
    @Override
    public boolean validateMove(ServerPlayer player, int x, int y) {
        return super.validateMove(player, x, y) && emptyCell.equals(gameField.get(x, y));
    }
}
