package com.github.shap_po.pencilgames.client.game.impl.tictactoe;

import com.github.shap_po.pencilgames.client.game.impl.abc.field.ClientPlayerToCellFieldGame;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;

public class ClientTicTacToeGame extends ClientPlayerToCellFieldGame<TicTacToeGame.Cell> {
    public ClientTicTacToeGame(ClientGameLobby lobby) {
        super(lobby, GameField.of(Cell.EMPTY, SIZE.left(), SIZE.right()), Cell::of, Cell.EMPTY);
    }

    public static GameFactory<ClientGameLobby, ClientTicTacToeGame> getFactory() {
        return new GameFactory<>(TicTacToeGame.GAME_ID, ClientTicTacToeGame::new);
    }
}
