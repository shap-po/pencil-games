package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.Player;

import java.util.UUID;

/**
 * Represents a player on the server side.
 */
public class ServerPlayer implements Player {
    private final UUID id;
    private final Server2ClientConnection connectionHandler;

    public ServerPlayer(UUID id, Server2ClientConnection connectionHandler) {
        this.id = id;
        this.connectionHandler = connectionHandler;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Server2ClientConnection getConnectionHandler() {
        return connectionHandler;
    }
}
