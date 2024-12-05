package com.github.shap_po.pencilgames.server.game;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.network.packet.s2c.game.EndGameS2CPacket;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class ServerGame extends Game<ServerGameLobby> {
    private boolean sentEndGamePacket = false;

    public ServerGame(ServerGameLobby lobby) {
        super(lobby);
    }

    public void end(Collection<UUID> winners) {
        sentEndGamePacket = true;
        this.lobby.broadcastPacket(new EndGameS2CPacket(winners));
        this.end();
    }

    @Override
    public void end() {
        if (!sentEndGamePacket) {
            this.lobby.broadcastPacket(new EndGameS2CPacket(List.of()));
        }
        super.end();
    }
}
