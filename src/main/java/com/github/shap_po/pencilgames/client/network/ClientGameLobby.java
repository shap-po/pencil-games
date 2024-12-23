package com.github.shap_po.pencilgames.client.network;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.game.ClientGame;
import com.github.shap_po.pencilgames.client.game.ClientGameFactoryRegistry;
import com.github.shap_po.pencilgames.client.game.player.ClientPlayer;
import com.github.shap_po.pencilgames.common.event.type.RunnableEvent;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.game.player.PlayerManager;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.s2c.game.StartGameS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.*;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.UUID;

/**
 * A representation of a game lobby on the client side.
 * <p>
 * The connection handler of a lobby can be updated to a new one by calling
 * {@link #connect(String, Integer)}, {@link #connect(String)}, or {@link #disconnect()}
 */
public class ClientGameLobby extends Thread implements GameLobby<ClientPlayer> {
    public static final Logger LOGGER = LoggerUtils.getLogger();

    public final PlayerManager<ClientPlayer> playerManager = new PlayerManager<>();
    public final RunnableEvent onConnect = RunnableEvent.create();
    public final RunnableEvent onDisconnect = RunnableEvent.create();

    private @Nullable Client2ServerConnection connectionHandler;
    private @Nullable UUID localPlayerId; // can be null before syncing with the server
    private @Nullable ClientGame currentGame;

    /**
     * Creates a new game lobby and registers receivers for base packets
     */
    public ClientGameLobby() {
        ClientPackets.REGISTRY.registerReceiver(PlayerConnectS2CPacket.PACKET_TYPE, (packet, context) -> {
            LOGGER.info("Player {} connected", packet.playerId());
            playerManager.addPlayer(new ClientPlayer(packet.playerId()));
        });
        ClientPackets.REGISTRY.registerReceiver(PlayerDisconnectS2CPacket.PACKET_TYPE, (packet, context) -> {
            LOGGER.info("Player {} disconnected", packet.playerId());
            playerManager.removePlayer(packet.playerId());
        });
        ClientPackets.REGISTRY.registerReceiver(PlayerMessageS2CPacket.PACKET_TYPE, (packet, context) -> {
            ClientPlayer player = playerManager.getPlayer(packet.playerId());
            if (player != null) {
                LOGGER.info("Received message from {}: {}", player, packet.message());
            }
        });
        ClientPackets.REGISTRY.registerReceiver(SyncPlayerIdS2CPacket.PACKET_TYPE, (packet, context) -> {
            if (localPlayerId != null) {
                LOGGER.warn("Received player id sync packed when the id is already set");
                return;
            }

            LOGGER.info("Syncing player id: {}", packet.playerId());
            localPlayerId = packet.playerId();
        });
        ClientPackets.REGISTRY.registerReceiver(SyncPlayerListS2CPacket.PACKET_TYPE, (packet, context) -> {
            LOGGER.info("Syncing player list: {}", packet.playerIds());
            playerManager.clear();
            for (UUID playerId : packet.playerIds()) {
                playerManager.addPlayer(playerId, new ClientPlayer(playerId));
            }
        });
        ClientPackets.REGISTRY.registerReceiver(SyncPlayerOrderS2CPacket.PACKET_TYPE, (packet, context) -> {
            LOGGER.info("Syncing player order: {}", packet.playersOrder());
            playerManager.setPlayerOrder(packet.playersOrder());
        });
        ClientPackets.REGISTRY.registerReceiver(StartGameS2CPacket.PACKET_TYPE, (packet, context) -> {
            Identifier gameId = packet.gameFactoryId();
            GameFactory<ClientGameLobby, ? extends ClientGame> gameFactory = ClientGameFactoryRegistry.REGISTRY.get(gameId);

            if (gameFactory == null) {
                LOGGER.warn("Server started an unknown game: {}", gameId);
                disconnect();
                return;
            }

            currentGame = gameFactory.apply(this);
            currentGame.start();

            LOGGER.info("Server started game: {}", gameId);
            PencilGamesClient.APPLICATION.setGameScreen(currentGame.getGameScreen(PencilGamesClient.APPLICATION));
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
    public void connect(String host, @Nullable Integer port) throws IOException {
        // disconnect old connection
        if (connectionHandler != null) {
            LOGGER.info("Disconnecting from the old server");
            disconnect();
        }

        port = port == null ? ConnectionHandler.DEFAULT_PORT : port;

        // create a new connection
        connectionHandler = new Client2ServerConnection(host, port);

        // subscribe to events
        connectionHandler.onPacket.register((packet) -> {
            try {
                ClientPackets.REGISTRY.receive(packet, new ClientPackets.ClientPacketContext(this));
            } catch (IllegalArgumentException e) {
                LOGGER.error("Failed to receive packet: {}", e.getMessage());
            }
        });
        connectionHandler.onDisconnect.register(() -> {
            connectionHandler = null;
            disconnect();
            onDisconnect.run();
        });
        connectionHandler.onConnect.register(onConnect);

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
     */
    public void connect(String host) throws IOException {
        this.connect(host, null);
    }

    @Override
    public PlayerManager<ClientPlayer> getPlayerManager() {
        return playerManager;
    }

    public @Nullable UUID getLocalPlayerId() {
        return localPlayerId;
    }

    public void sendPacket(Packet packet) {
        if (!isConnected()) {
            return;
        }
        assert connectionHandler != null;
        connectionHandler.sendPacket(packet);
    }

    public boolean isConnected() {
        return connectionHandler != null && connectionHandler.isAlive();
    }

    public void disconnect() {
        if (isConnected()) {
            assert connectionHandler != null;
            connectionHandler.close();
        }
        LOGGER.info("Disconnected from server");

        playerManager.clear();
        localPlayerId = null;
        if (currentGame != null) {
            currentGame.end();
        }
        currentGame = null;
    }
}
