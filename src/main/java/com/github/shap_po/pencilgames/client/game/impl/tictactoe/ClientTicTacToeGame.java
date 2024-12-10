package com.github.shap_po.pencilgames.client.game.impl.tictactoe;

import com.github.shap_po.pencilgames.client.game.impl.abc.field.ClientPlayerToCellFieldGame;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;

public class ClientTicTacToeGame extends ClientPlayerToCellFieldGame<TicTacToeGame.Cell> implements TicTacToeGame {
    public ClientTicTacToeGame(ClientGameLobby lobby) {
        super(lobby, GameField.of(TicTacToeGame.Cell.EMPTY, TicTacToeGame.SIZE.getLeft(), TicTacToeGame.SIZE.getRight()), TicTacToeGame.Cell::of, TicTacToeGame.Cell.EMPTY);
    }

    public static GameFactory<ClientGameLobby, ClientTicTacToeGame> getFactory() {
        return new GameFactory<>(TicTacToeGame.GAME_ID, ClientTicTacToeGame::new);
    }
}
