package com.github.shap_po.pencilgames.common.game.impl.go;

import com.github.shap_po.pencilgames.common.game.impl.abc.field.PlayerToCellFieldGame;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.common.util.Pair;

public interface GoGame extends PlayerToCellFieldGame<GoGame.Cell> {
    Identifier GAME_ID = Identifier.of("go");
    Pair<Integer, Integer> SIZE = Pair.of(9, 9);
    int MOVE_COUNT = 12;
}
