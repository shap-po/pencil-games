package com.github.shap_po.pencilgames.server.game;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;

public abstract class ServerGame extends Game<ServerGameLobby> {
    public ServerGame(ServerGameLobby lobby) {
        super(lobby);
    }
}
