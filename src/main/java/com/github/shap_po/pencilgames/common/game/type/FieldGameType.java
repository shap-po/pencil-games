package com.github.shap_po.pencilgames.common.game.type;

import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.game.data.Field;

public abstract class FieldGameType<T> extends GameType {
    protected final Field<T> field;

    public FieldGameType(GameLobby<?> lobby, Field<T> field) {
        super(lobby);
        this.field = field;
    }
}
