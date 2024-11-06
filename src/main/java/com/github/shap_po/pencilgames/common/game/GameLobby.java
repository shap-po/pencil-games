package com.github.shap_po.pencilgames.common.game;

import com.github.shap_po.pencilgames.common.Player;

import java.util.Map;
import java.util.UUID;

public interface GameLobby<P extends Player> {
    Map<UUID, P> getPlayers();

    default boolean addPlayer(UUID id, P player) {
        if (getPlayers().containsKey(id)) {
            return false;
        }
        getPlayers().put(id, player);
        return true;
    }

    default void removePlayer(UUID id) {
        getPlayers().remove(id);
    }
}
