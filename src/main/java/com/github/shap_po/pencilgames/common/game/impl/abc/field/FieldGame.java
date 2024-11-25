package com.github.shap_po.pencilgames.common.game.impl.abc.field;

import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;

import java.util.UUID;

public interface FieldGame<C> {
    GameField<C> getGameField();

    C handleMove(UUID playerID, int x, int y);
}
