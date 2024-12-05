package com.github.shap_po.pencilgames.common.game.impl.abc.field;

import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;

import java.util.UUID;

/**
 * Base interface for field games.
 *
 * @param <C> cell type
 */
public interface FieldGame<C> {
    /**
     * Gets the game field.
     *
     * @return the game field
     */
    GameField<C> getGameField();

    /**
     * Handles player's move on the field.
     *
     * @param playerId player ID
     * @param x        x coordinate
     * @param y        y coordinate
     */
    void handleMove(UUID playerId, int x, int y);
}
