package com.github.shap_po.pencilgames.client.game;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.client.network.ClientPackets;
import com.github.shap_po.pencilgames.client.ui.Application;
import com.github.shap_po.pencilgames.client.ui.screen.game.GameScreen;
import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.network.packet.s2c.game.EndGameS2CPacket;

public abstract class ClientGame extends Game<ClientGameLobby> {
    public ClientGame(ClientGameLobby lobby) {
        super(lobby);
    }

    abstract public GameScreen<?> getGameScreen(Application root);

    @Override
    public void onStart() {
        ClientPackets.registerPacketType(EndGameS2CPacket.PACKET_TYPE);
        ClientPackets.registerReceiver(EndGameS2CPacket.PACKET_TYPE, (packet, context) -> {
            PencilGamesClient.LOGGER.info("Game ended, winners: {}", packet.winners());
            end();
        });
    }

    @Override
    public void onEnd() {
        ClientPackets.unregisterPacketType(EndGameS2CPacket.PACKET_TYPE);
    }

    public boolean isMyTurn() {
        return isPlayerTurn(lobby.getLocalPlayerId());
    }
}
