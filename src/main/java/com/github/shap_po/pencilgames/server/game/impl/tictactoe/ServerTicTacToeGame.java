package com.github.shap_po.pencilgames.server.game.impl.tictactoe;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.game.impl.abc.field.ServerPlayerToCellFieldGame;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public class ServerTicTacToeGame extends ServerPlayerToCellFieldGame<TicTacToeGame.Cell> implements TicTacToeGame {
    public ServerTicTacToeGame(ServerGameLobby lobby) {
        super(lobby, GameField.of(Cell.EMPTY, SIZE.left(), SIZE.right()), Cell::of, Cell.EMPTY);
    }

    @Override
    public void handleMove(UUID playerId, int x, int y) {
        super.handleMove(playerId, x, y);

        Cell winner = checkWin(x, y);
        if (winner != null) {
            PencilGamesServer.LOGGER.debug("Player {} won!", playerId);
            end(Set.of(playerId));
        }
    }

    /**
     * Check for the winner of the game.
     *
     * @param x x coordinate of the last move
     * @param y y coordinate of the last move
     * @return Cell associated with the winner or null if there is no winner
     */
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
