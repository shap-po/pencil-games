package com.github.shap_po.pencilgames.server.game;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.network.packet.s2c.game.EndGameS2CPacket;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;

import java.util.Collection;
import java.util.UUID;

public abstract class ServerGame extends Game<ServerGameLobby> {
    public ServerGame(ServerGameLobby lobby) {
        super(lobby);
    }

    public void end(Collection<UUID> winners){
        this.lobby.broadcastPacket(new EndGameS2CPacket(winners));
        this.end();
    }
}
