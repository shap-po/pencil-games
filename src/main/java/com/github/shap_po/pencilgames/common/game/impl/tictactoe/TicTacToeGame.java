package com.github.shap_po.pencilgames.common.game.impl.tictactoe;

import com.github.shap_po.pencilgames.common.game.impl.abc.field.PlayerToCellFieldGame;
import com.github.shap_po.pencilgames.common.util.Identifier;

public interface TicTacToeGame extends PlayerToCellFieldGame<TicTacToeGame.Cell> {
    Identifier GAME_ID = Identifier.of("tictactoe");

    int WIN_LENGTH = 3;
}
