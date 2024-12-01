package com.github.shap_po.pencilgames.server.game.impl;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.game.abc.field.ServerFieldGame;
import com.github.shap_po.pencilgames.server.game.player.ServerPlayer;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerTicTacToeGame extends ServerFieldGame<TicTacToeGame.Cell> implements TicTacToeGame {
    private final Map<UUID, Cell> playerToCellMap;

    public ServerTicTacToeGame(ServerGameLobby lobby) {
        super(lobby, GameField.of(Cell.EMPTY, size.left(), size.right()));

        List<UUID> players = lobby.getPlayerManager().getPlayerOrder();
        this.playerToCellMap = TicTacToeGame.createPlayerToCellMap(players);
    }

    @Override
    public GameField<Cell> getGameField() {
        return null;
    }

    @Override
    public Cell playerToCell(UUID playerId) {
        return playerToCellMap.getOrDefault(playerId, Cell.EMPTY);
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
        return super.validateMove(player, x, y) && gameField.get(x, y) == Cell.EMPTY;
    }

    @Override
    public void handleMove(UUID playerId, int x, int y) {
        nextPlayer();

        Cell c = playerToCell(playerId);

        PencilGamesServer.LOGGER.debug("Player {} moved to ({}, {}) with cell {}", playerId, x, y, c);

        gameField.set(x, y, c);
    }

    public static GameFactory<ServerGameLobby, Game<ServerGameLobby>> getFactory() {
        return new GameFactory<>(GAME_ID, ServerTicTacToeGame::new);
    }
}
