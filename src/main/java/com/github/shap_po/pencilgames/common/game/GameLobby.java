package com.github.shap_po.pencilgames.common.game;

import com.github.shap_po.pencilgames.common.Player;

import java.util.Map;
import java.util.UUID;

public interface GameLobby <P extends Player>{
    Map<UUID, P> getPlayers();

    default void addPlayer(P player, UUID id) {
        getPlayers().put(id, player);
    }

    default void removePlayer(UUID id) {
        getPlayers().remove(id);
    }
}
