package com.github.shap_po.pencilgames.common.game.impl.tictactoe;

import com.github.shap_po.pencilgames.common.game.impl.abc.field.FieldGame;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.common.util.Pair;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.List;
import java.util.UUID;

public interface TicTacToeGame extends FieldGame<TicTacToeGame.Cell> {
    Identifier GAME_ID = Identifier.of("tictactoe");
    int MIN_PLAYERS = 2;
    int MAX_PLAYERS = Cell.values().length - 1;
    int WIN_LENGTH = 3;

    Pair<Integer, Integer> size = Pair.of(3, 3);

    Cell playerToCell(UUID playerId);

    static BiMap<UUID, Cell> createPlayerToCellMap(List<UUID> players) {
        BiMap<UUID, Cell> playerToCellMap = HashBiMap.create(players.size());

        for (int i = 0; i < players.size(); i++) {
            playerToCellMap.put(players.get(i), Cell.of(i + 1));
        }

        return playerToCellMap;
    }

    enum Cell {
        EMPTY(" "),
        X("x"), O("o"),
        A("a"), B("b"), C("c"), D("d"), E("e"), F("f");

        private final String symbol;

        Cell(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public static Cell of(int i) {
            return Cell.values()[i];
        }
    }
}
