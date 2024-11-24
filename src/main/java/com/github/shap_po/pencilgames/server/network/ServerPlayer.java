package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.Player;

import java.util.UUID;

/**
 * Represents a player on the server side.
 */
public record ServerPlayer(UUID getId, Server2ClientConnection connectionHandler) implements Player {
}
