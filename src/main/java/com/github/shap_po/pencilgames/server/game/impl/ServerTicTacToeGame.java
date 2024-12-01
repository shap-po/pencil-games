package com.github.shap_po.pencilgames.server.game.impl;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.game.abc.field.ServerFieldGame;
import com.github.shap_po.pencilgames.server.game.player.ServerPlayer;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import com.google.common.collect.BiMap;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ServerTicTacToeGame extends ServerFieldGame<TicTacToeGame.Cell> implements TicTacToeGame {
    private final BiMap<UUID, Cell> playerToCellMap;

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

        Cell winner = checkWin(x, y);
        if (winner != null) {
            PencilGamesServer.LOGGER.debug("Player {} won!", playerId);
            end(Set.of(playerId));
        }
    }

    private @Nullable Cell checkWin(int x, int y) {
        Cell c = gameField.get(x, y);
        if (c == Cell.EMPTY) {
            return null;
        }

        int count;

        // Check horizontal win
        count = 1;
        for (int i = x - 1; i >= 0; i--) {
            if (gameField.get(i, y) != c) break;
            count++;
        }
        for (int i = x + 1; i < gameField.getWidth(); i++) {
            if (gameField.get(i, y) != c) break;
            count++;
        }
        if (count >= WIN_LENGTH) return c;

        // Check vertical win
        count = 1;
        for (int i = y - 1; i >= 0; i--) {
            if (gameField.get(x, i) != c) break;
            count++;
        }
        for (int i = y + 1; i < gameField.getHeight(); i++) {
            if (gameField.get(x, i) != c) break;
            count++;
        }
        if (count >= WIN_LENGTH) return c;

        int i;
        // Check primary diagonal win
        count = 1;
        i = 1;
        while (x - i >= 0 && y - i >= 0) {
            if (gameField.get(x - i, y - i) != c) break;
            count++;
            i++;
        }
        i = 1;
        while (x + i < gameField.getWidth() && y + i < gameField.getHeight()) {
            if (gameField.get(x + i, y + i) != c) break;
            count++;
            i++;
        }
        if (count >= WIN_LENGTH) return c;

        // Check secondary diagonal win
        count = 1;
        i = 1;
        while (x - i >= 0 && y + i < gameField.getHeight()) {
            if (gameField.get(x - i, y + i) != c) break;
            count++;
            i++;
        }
        i = 1;
        while (x + i < gameField.getWidth() && y - i >= 0) {
            if (gameField.get(x + i, y - i) != c) break;
            count++;
            i++;
        }
        if (count >= WIN_LENGTH) return c;

        return null;
    }

    public static GameFactory<ServerGameLobby, Game<ServerGameLobby>> getFactory() {
        return new GameFactory<>(GAME_ID, ServerTicTacToeGame::new);
    }
}
