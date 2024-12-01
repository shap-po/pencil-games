package com.github.shap_po.pencilgames.server.game.player;

import com.github.shap_po.pencilgames.common.game.player.Player;
import com.github.shap_po.pencilgames.server.network.Server2ClientConnection;

import java.util.UUID;

/**
 * Represents a player on the server side.
 */
public record ServerPlayer(UUID getId, Server2ClientConnection connectionHandler) implements Player {
}
