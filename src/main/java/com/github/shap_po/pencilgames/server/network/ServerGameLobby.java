package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.event.type.BiConsumerEvent;
import com.github.shap_po.pencilgames.common.event.type.ConsumerEvent;
import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.s2c.game.StartGameS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.PlayerConnectS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.PlayerDisconnectS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.SyncPlayerIdS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.SyncPlayerListS2CPacket;
import com.github.shap_po.pencilgames.server.PencilGamesServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * Represents a game lobby on the server side.
 * Listens to incoming connections, manages players and redirects game logic.
 */
public class ServerGameLobby extends Thread implements GameLobby<ServerPlayer> {
    public final ConsumerEvent<ServerPlayer> onPlayerConnect = ConsumerEvent.create();
    public final ConsumerEvent<ServerPlayer> onPlayerDisconnect = ConsumerEvent.create();
    public final BiConsumerEvent<ServerPlayer, Packet> onPlayerPacket = BiConsumerEvent.create();

    private final ServerSocket serverSocket;

    private final Map<UUID, ServerPlayer> players = new HashMap<>();
    private GameFactory<ServerGameLobby, Game<ServerGameLobby>> gameFactory;
    private Game<ServerGameLobby> currentGame;

    public ServerGameLobby(int port) throws IOException {
        super("ServerGameLobby");
        serverSocket = new ServerSocket(port);

        onPlayerConnect.register((player -> {
            // add player and check if successful
            boolean result = addPlayer(player.getId(), player);

            if (!result) {
                PencilGamesServer.LOGGER.info("Duplication of player id {} detected", player.getId());
                player.connectionHandler().close();
                return;
            }

            // sync player's id so they know their own
            player.connectionHandler().sendPacket(new SyncPlayerIdS2CPacket(player.getId()));

            // send the player list to the new player
            Collection<UUID> playerIds = new LinkedList<>(players.keySet());
            player.connectionHandler()
                .sendPacket(new SyncPlayerListS2CPacket(playerIds));

            // notify other players of the new player
            broadcastPacket(new PlayerConnectS2CPacket(player.getId()));

        }));
        onPlayerDisconnect.register((player -> {
            players.remove(player.getId());
            broadcastPacket(new PlayerDisconnectS2CPacket(player.getId()));
        }));
        onPlayerPacket.register((player, packet) -> {
            try {
                ServerPackets.REGISTRY.receive(packet, new ServerPackets.ServerPacketContext(this, player));
            } catch (IllegalArgumentException e) {
                PencilGamesServer.LOGGER.error("Failed to receive packet", e);
            }
        });

        PencilGamesServer.LOGGER.info("Server started on port {}", port);
    }

    public ServerGameLobby() throws IOException {
        this(ConnectionHandler.DEFAULT_PORT);
    }

    @Override
    public Map<UUID, ServerPlayer> getPlayers() {
        return players;
    }

    @Override
    public void run() {
        while (serverSocket.isBound() && !serverSocket.isClosed()) {
            try {
                acceptConnection();
            } catch (SocketException e) {
                PencilGamesServer.LOGGER.info("Server socket closed");
            } catch (IOException e) {
                PencilGamesServer.LOGGER.error("Error while accepting connection", e);
            }
        }
        disconnect();
    }

    private void acceptConnection() throws IOException {
        Socket connection = serverSocket.accept();

        UUID playerId = UUID.randomUUID();
        Server2ClientConnection thread = new Server2ClientConnection(connection);
        ServerPlayer player = new ServerPlayer(playerId, thread);

        thread.onConnect.register(() -> onPlayerConnect.accept(player));
        thread.onDisconnect.register(() -> onPlayerDisconnect.accept(player));
        thread.onPacket.register((packet) -> onPlayerPacket.accept(player, packet));

        thread.start();
    }

    public void setGameFactory(GameFactory<ServerGameLobby, Game<ServerGameLobby>> gameFactory) {
        this.gameFactory = gameFactory;
    }

    public void broadcastPacket(Packet packet) {
        for (ServerPlayer player : getPlayers().values()) {
            player.connectionHandler().sendPacket(packet);
        }
    }

    public void broadcastPacket(Packet packet, UUID except) {
        for (ServerPlayer player : getPlayers().values()) {
            if (player.getId().equals(except)) {
                continue;
            }
            player.connectionHandler().sendPacket(packet);
        }
    }

    public void startGame() {
        if (currentGame != null) {
            currentGame.end();
        }

        currentGame = gameFactory.apply(this);
        currentGame.start();
        broadcastPacket(new StartGameS2CPacket(gameFactory.getId()));
    }

    public void disconnect() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            PencilGamesServer.LOGGER.error("Failed to close server socket", e);
        } finally {
            this.interrupt();
        }
    }
}
