package com.github.shap_po.pencilgames.client.network;

import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.PlayerConnectS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.PlayerDisconnectS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.SyncPlayerListS2CPacket;
import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A representation of a game lobby on the client side.
 * <p>
 * The connection handler of a lobby can be updated to a new one by calling
 * {@link #connect(String, int)}, {@link #connect(String)}, or {@link #disconnect()}
 */
public class ClientGameLobby implements GameLobby<ClientPlayer> {
    public static final Logger LOGGER = LoggerUtils.getLogger();
    private final Map<UUID, ClientPlayer> players = new HashMap<>();
    private Client2ServerConnection connectionHandler;

    /**
     * Creates a new game lobby and registers receivers for base packets
     */
    public ClientGameLobby() {
        ClientPackets.REGISTRY.registerReceiver(SyncPlayerListS2CPacket.PACKET_TYPE, (packet, context) -> {
            LOGGER.info("Syncing player list: {}", packet.playerIds());
            players.clear();
            for (UUID playerId : packet.playerIds()) {
                addPlayer(playerId, new ClientPlayer(playerId));
            }
        });
        ClientPackets.REGISTRY.registerReceiver(PlayerConnectS2CPacket.PACKET_TYPE, (packet, context) -> {
            LOGGER.info("Player {} connected", packet.playerId());
            addPlayer(packet.playerId(), new ClientPlayer(packet.playerId()));
        });
        ClientPackets.REGISTRY.registerReceiver(PlayerDisconnectS2CPacket.PACKET_TYPE, (packet, context) -> {
            LOGGER.info("Player {} disconnected", packet.playerId());
            removePlayer(packet.playerId());
        });
    }

    /**
     * Connects to a server.
     * If a connection is already established, old connection will be closed.
     *
     * @param host host's IP address
     * @param port port
     * @throws IOException if connection fails
     */
    public void connect(String host, int port) throws IOException {
        // disconnect old connection
        if (connectionHandler != null) {
            disconnect();
        }

        // create a new connection
        connectionHandler = new Client2ServerConnection(host, port);

        // subscribe to events
        connectionHandler.onPacket.register((packet) -> {
            try {
                ClientPackets.REGISTRY.receive(packet, new ClientPackets.ClientPacketContext(this));
            } catch (IllegalArgumentException e) {
                LOGGER.error("Failed to receive packet", e);
            }
        });
        connectionHandler.onDisconnect.register(() -> {
            connectionHandler = null;
            disconnect();
        });

        // start the connection
        connectionHandler.start();

        LOGGER.info("Connected to server at {}:{}", host, port);
    }

    /**
     * Connects to a server with a default port.
     * If a connection is already established, old connection will be closed.
     *
     * @param host host's IP address
     * @throws IOException if connection fails
     * @see #connect(String, int)
     */
    public void connect(String host) throws IOException {
        this.connect(host, ConnectionHandler.DEFAULT_PORT);
    }

    @Override
    public Map<UUID, ClientPlayer> getPlayers() {
        return players;
    }

    public void sendPacket(Packet packet) {
        connectionHandler.sendPacket(packet);
    }

    public boolean isConnected() {
        return connectionHandler != null && connectionHandler.isAlive();
    }

    public void disconnect() {
        if (isConnected()) {
            connectionHandler.close();
        }
        LOGGER.info("Disconnected from server");
        players.clear();
    }
}
