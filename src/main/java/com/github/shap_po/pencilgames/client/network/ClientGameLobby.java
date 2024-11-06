package com.github.shap_po.pencilgames.client.network;

import com.github.shap_po.pencilgames.common.Player;
import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.network.packet.Packet;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientGameLobby extends Thread implements GameLobby<Player> {
    private final Map<UUID, Player> players = new HashMap<>();
    private final Client2ServerConnection connectionHandler;

    public ClientGameLobby(Client2ServerConnection connectionHandler) {
        this.connectionHandler = connectionHandler;
        connectionHandler.start();

        connectionHandler.onPacket.register((packet) -> {

        });
    }

    @Override
    public Map<UUID, Player> getPlayers() {
        return players;
    }

    public <P extends Packet> void sendPacket(P packet) {
        connectionHandler.sendPacket(packet);
    }
}
