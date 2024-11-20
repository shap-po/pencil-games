package com.github.shap_po.pencilgames.common.game.impl.tictactoe;

import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.game.data.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.FieldGame;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.common.util.Pair;

public class TicTacToeGame extends FieldGame<TicTacToeGame.Cell> {
    public static final Identifier GAME_ID = Identifier.of("tictactoe");
    private static final Pair<Integer, Integer> size = Pair.of(3, 3);

    public TicTacToeGame(GameLobby<?> lobby, GameField<Cell> gameField) {
        super(lobby, gameField);
    }

    public static void registerClient() {
        FieldGame.registerClient();
    }

    public static void registerServer() {
        FieldGame.registerServer();
    }

    public enum Cell {
        EMPTY(null), X("x"), O("o");

        private final String symbol;

        Cell(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }
    }

    public static GameFactory<TicTacToeGame> getFactory() {
        return new GameFactory<>(
            GAME_ID,
            (lobby) -> {
                GameField<Cell> gameField = new GameField<>(new Cell[size.left()][size.right()]);
                return new TicTacToeGame(lobby, gameField);
            },
            TicTacToeGame::registerClient,
            TicTacToeGame::registerServer
        );
    }
}
