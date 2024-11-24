package com.github.shap_po.pencilgames.common.game.impl.tictactoe;

import com.github.shap_po.pencilgames.common.game.impl.abc.field.FieldGame;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.common.util.Pair;

import java.util.UUID;

public interface TicTacToeGame extends FieldGame<TicTacToeGame.Cell> {
    Identifier GAME_ID = Identifier.of("tictactoe");
    int MIN_PLAYERS = 2;
    int MAX_PLAYERS = Cell.values().length - 1;

    Pair<Integer, Integer> size = Pair.of(3, 3);

    Cell playerToCell(UUID playerId);

    enum Cell {
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
}
