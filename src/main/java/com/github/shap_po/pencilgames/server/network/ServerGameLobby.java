package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.event.type.BiConsumerEvent;
import com.github.shap_po.pencilgames.common.event.type.ConsumerEvent;
import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.PlayerConnectS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.PlayerDisconnectS2CPacket;
import com.github.shap_po.pencilgames.common.network.packet.s2c.player.SyncPlayerListS2CPacket;
import com.github.shap_po.pencilgames.server.PencilGamesServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

    public ServerGameLobby(int port) throws IOException {
        super("ServerGameLobby");
        serverSocket = new ServerSocket(port);

        onPlayerConnect.register((player -> {
            // add player and check if successful
            boolean result = addPlayer(player.id(), player);

            if (result) {
                // send the player list to the new player
                Collection<UUID> playerIds = new LinkedList<>(players.keySet());
                player.connectionHandler()
                    .sendPacket(new SyncPlayerListS2CPacket(playerIds));

                // notify other players of the new player
                broadcastPacket(new PlayerConnectS2CPacket(player.id()));
            }
        }));
        onPlayerDisconnect.register((player -> {
            players.remove(player.id());
            broadcastPacket(new PlayerDisconnectS2CPacket(player.id()));
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


    private void broadcastPacket(Packet packet) {
        for (ServerPlayer player : getPlayers().values()) {
            player.connectionHandler().sendPacket(packet);
        }
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
