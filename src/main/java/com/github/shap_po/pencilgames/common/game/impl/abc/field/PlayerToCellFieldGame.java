package com.github.shap_po.pencilgames.common.game.impl.abc.field;

import com.github.shap_po.pencilgames.common.util.Pair;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Base interface for field games where each player has a cell variant associated with them.
 *
 * @param <C> cell type
 */
public interface PlayerToCellFieldGame<C> extends FieldGame<C> {
    int MIN_PLAYERS = 2;
    int MAX_PLAYERS = Cell.values().length - 1;

    Pair<Integer, Integer> SIZE = Pair.of(3, 3);

    /**
     * Gets the cell variant associated with the player.
     *
     * @param playerId player ID
     * @return the cell
     */
    C playerToCell(UUID playerId);

    /**
     * Creates a bi-map from player ID to cell variant.
     *
     * @param players list of player IDs
     * @param cellOf  cell
     * @param <C>     cell type
     * @return the map
     */
    static <C> BiMap<UUID, C> createPlayerToCellMap(List<UUID> players, Function<Integer, C> cellOf) {
        BiMap<UUID, C> playerToCellMap = HashBiMap.create(players.size());

        for (int i = 0; i < players.size(); i++) {
            playerToCellMap.put(players.get(i), cellOf.apply(i + 1));
        }

        return playerToCellMap;
    }

    /**
     * Simple implementation of the cell type.
     * Can be used by game implementations or be overridden if needed.
     */
    enum Cell {
        EMPTY(" "),
        X("x"), O("o"),
        A("a"), B("b"), C("c"), D("d"), E("e"), F("f");

        private final String symbol;

        Cell(String symbol) {
            this.symbol = symbol;
        }

        public static Cell of(int i) {
            return Cell.values()[i];
        }

        @Override
        public String toString() {
            return symbol;
        }
    }
}
