package com.github.shap_po.pencilgames.common.game;

import com.github.shap_po.pencilgames.common.game.player.Player;
import com.github.shap_po.pencilgames.common.game.player.PlayerManager;

public interface GameLobby<P extends Player> {
    PlayerManager<P> getPlayerManager();
}
