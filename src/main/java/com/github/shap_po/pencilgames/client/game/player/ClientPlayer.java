package com.github.shap_po.pencilgames.client.game.player;

import com.github.shap_po.pencilgames.common.game.player.Player;

import java.util.UUID;

public record ClientPlayer(UUID getId) implements Player {
}
