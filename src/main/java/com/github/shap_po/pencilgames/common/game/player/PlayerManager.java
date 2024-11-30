package com.github.shap_po.pencilgames.common.game.player;

import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;

/**
 * Holds all players in a game lobby.
 *
 * @param <P> player type
 */
public class PlayerManager<P extends Player> {
    private final Logger LOGGER = LoggerUtils.getParentLogger();

    private final Map<UUID, P> players = new HashMap<>();
    private final List<UUID> playerOrder = new ArrayList<>();

    /**
     * Get a player by their UUID.
     *
     * @param id player UUID
     * @return player object. null if not found
     */
    public @Nullable P getPlayer(UUID id) {
        return players.getOrDefault(id, null);
    }

    /**
     * Get a player by their index.
     *
     * @param index player index
     * @return player object. null if index is out of bounds or player not found
     */
    public @Nullable P getPlayer(int index) {
        return players.get(getPlayerId(index));
    }

    /**
     * Get a player UUID by their index.
     *
     * @param index player index
     * @return player UUID. null if index is out of bounds
     */
    public @Nullable UUID getPlayerId(int index) {
        if (index >= playerOrder.size()) return null;
        return playerOrder.get(index);
    }

    /**
     * Get a player index by their UUID.
     *
     * @param id player UUID
     * @return player index. null if player not found
     */
    public @Nullable Integer getPlayerIndex(UUID id) {
        if (!players.containsKey(id)) return null;
        return playerOrder.indexOf(id);
    }

    /**
     * Add a player to the player manager.
     *
     * @param id     player UUID
     * @param player player object
     */
    public void addPlayer(UUID id, P player) {
        players.put(id, player);
        playerOrder.add(id);
    }

    /**
     * Add a player to the player manager.
     *
     * @param player player object
     */
    public void addPlayer(P player) {
        addPlayer(player.getId(), player);
    }

    /**
     * Add a collection of players to the player manager.
     *
     * @param players collection of player objects
     */
    public void addPlayers(Collection<P> players) {
        players.forEach(this::addPlayer);
    }

    /**
     * Remove a player from the player manager.
     *
     * @param id player UUID
     */
    public void removePlayer(UUID id) {
        if (!players.containsKey(id)) return;
        players.remove(id);
        playerOrder.remove(id);
    }

    /**
     * Remove a player from the player manager.
     *
     * @param player player object
     */
    public void removePlayer(P player) {
        removePlayer(player.getId());
    }

    /**
     * Check if a player is in the player manager.
     *
     * @param id player UUID
     * @return true if the player is in the player manager
     */
    public boolean hasPlayer(UUID id) {
        return players.containsKey(id);
    }

    /**
     * Check if a player is in the player manager.
     *
     * @param player player object
     * @return true if the player is in the player manager
     */
    public boolean hasPlayer(P player) {
        return hasPlayer(player.getId());
    }

    /**
     * Randomly rearrange the players in the player manager.
     */
    public void shufflePlayers() {
        Collections.shuffle(playerOrder);
    }

    /**
     * Set the order of players in the player manager.
     *
     * @param order player UUIDs in the desired order
     */
    public void setPlayerOrder(List<UUID> order) {
        if (order.size() != players.size()) {
            LOGGER.error("Failed to set player order. Order size does not match number of players. Old order: {}, new order: {}", playerOrder, order);
            return;
        }
        playerOrder.clear();
        playerOrder.addAll(order);
    }

    /**
     * Get the order of players in the player manager.
     *
     * @return player UUIDs in the order
     */
    public List<UUID> getPlayerOrder() {
        return new ArrayList<>(playerOrder); // do not modify the original
    }

    /**
     * Get a set of players in the player manager.
     * Use {@link #getPlayerList} if player order is important
     *
     * @return set of player objects
     */
    public Set<P> getPlayersSet() {
        return new HashSet<>(players.values());
    }

    /**
     * Get an ordered list of players in the player manager.
     * Use {@link #getPlayersSet} if player order is not important
     *
     * @return list of player objects
     */
    public List<P> getPlayerList() {
        return playerOrder.stream().map(players::get).toList();
    }

    /**
     * Get all players in the player manager as a map.
     *
     * @return UUID to a player object map
     */
    public Map<UUID, P> getPlayers() {
        return new HashMap<>(players); // do not modify the original
    }

    /**
     * Get the number of players in the player manager.
     *
     * @return number of players
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Generate a new player UUID
     *
     * @return new player UUID
     */
    public UUID newPlayerId() {
        UUID id = UUID.randomUUID();
        // should never happen but just in case
        while (hasPlayer(id)) id = UUID.randomUUID();
        return id;
    }

    /**
     * Clear the player manager
     */
    public void clear() {
        players.clear();
        playerOrder.clear();
    }
}
