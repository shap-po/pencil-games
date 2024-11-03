package com.github.shap_po.pencilgames.server.network;

import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.network.packet.type.PlayerJoinPacket;
import com.github.shap_po.pencilgames.common.network.packet.type.PlayerLeavePacket;
import com.github.shap_po.pencilgames.server.PencilGamesServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a game lobby on the server side.
 * Listens to incoming connections, manages players and redirects game logic.
 */
public class ServerGameLobby extends Thread implements GameLobby<ServerPlayer> {
    private final ServerSocket serverSocket;
    private final Map<UUID, ServerPlayer> players = new HashMap<>();

    public ServerGameLobby(int port) throws IOException {
        serverSocket = new ServerSocket(port);
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
        thread.onClose.register((e) -> onPlayerLeave(playerId));
        thread.start();

        onPlayerJoin(new ServerPlayer(playerId, thread));
    }


    private void onPlayerJoin(ServerPlayer player) {
        players.put(player.getId(), player);
        broadcastPacket(new PlayerJoinPacket(player.getId()));
    }

    private void onPlayerLeave(UUID playerId) {
        players.remove(playerId);
        broadcastPacket(new PlayerLeavePacket(playerId));
    }


    private void broadcastPacket(Packet<?> packet) {
        for (ServerPlayer player : getPlayers().values()) {
            player.getConnectionHandler().sendPacket(packet);
        }
    }

    public void disconnect() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }
    }
}
