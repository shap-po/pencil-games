package com.github.shap_po.pencilgames.common.game.impl.abc.field;

import com.github.shap_po.pencilgames.common.data.Settings;
import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.game.data.Game;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.util.Pair;

public abstract class FieldGame<T> extends Game {
    public static final Settings SETTINGS = new Settings()
        .addField("size", new Settings.Field<>(() -> Pair.of(3, 3)));

    protected final GameField<T> gameField;

    public FieldGame(GameLobby<?> lobby, GameField<T> gameField) {
        super(lobby);
        this.gameField = gameField;
    }

    public static void registerClient() {

    }

    public static void registerServer() {

    }
}
