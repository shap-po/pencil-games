package com.github.shap_po.pencilgames.client.game.impl.go;

import com.github.shap_po.pencilgames.client.game.impl.abc.field.ClientPlayerToCellFieldGame;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.go.GoGame;

public class ClientGoGame extends ClientPlayerToCellFieldGame<GoGame.Cell> implements GoGame {
    public ClientGoGame(ClientGameLobby lobby) {
        super(lobby, GameField.of(GoGame.Cell.EMPTY, GoGame.SIZE.getLeft(), GoGame.SIZE.getRight()), GoGame.Cell::of, GoGame.Cell.EMPTY);
    }

    public static GameFactory<ClientGameLobby, ClientGoGame> getFactory() {
        return new GameFactory<>(GoGame.GAME_ID, ClientGoGame::new);
    }
}
