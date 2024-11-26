package com.github.shap_po.pencilgames.common.game;

import com.github.shap_po.pencilgames.common.Player;

import java.util.*;

public interface GameLobby<P extends Player> {
    /**
     * Get all players in the lobby as a map.
     * The linked map is used to preserve insertion order.
     *
     * @return UUID to a player linked map
     */
    LinkedHashMap<UUID, P> getPlayers();

    /**
     * Get all players in the lobby as a list.
     *
     * @return list of players
     */
    default List<P> getPlayerList() {
        return getPlayers().values().stream().toList();
    }

    /**
     * Adds a player to the game lobby.
     *
     * @param id     UUID of the player
     * @param player the player
     * @return true if the player was successfully added
     */
    default boolean addPlayer(UUID id, P player) {
        if (getPlayers().containsKey(id)) {
            return false;
        }
        getPlayers().put(id, player);
        return true;
    }

    /**
     * Removes a player from the game lobby.
     *
     * @param id UUID of the player
     */
    default void removePlayer(UUID id) {
        getPlayers().remove(id);
    }

    /**
     * Randomly rearranges the players in the game lobby.
     * This method should be called before the game starts.
     */
    default void shufflePlayers() {
        List<UUID> keys = new ArrayList<>(getPlayers().keySet().stream().toList());
        Collections.shuffle(keys);

        setPlayerOrder(keys);
    }

    /**
     * Sets the order of players in the game lobby.
     * Useful for syncing player order between clients and the server.
     *
     * @param order player UUIDs in the desired order
     * @see com.github.shap_po.pencilgames.common.network.packet.s2c.player.SyncPlayerOrderS2CPacket
     */
    default void setPlayerOrder(List<UUID> order) {
        LinkedHashMap<UUID, P> newMap = new LinkedHashMap<>();

        for (UUID uuid : order) {
            newMap.put(uuid, getPlayers().get(uuid));
        }

        getPlayers().clear();
        getPlayers().putAll(newMap);
    }
}
