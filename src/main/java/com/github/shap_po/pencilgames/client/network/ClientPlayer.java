package com.github.shap_po.pencilgames.client.network;

import com.github.shap_po.pencilgames.common.Player;

import java.util.UUID;

public record ClientPlayer(UUID id) implements Player {
}
