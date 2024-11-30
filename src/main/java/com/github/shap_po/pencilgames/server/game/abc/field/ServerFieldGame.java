package com.github.shap_po.pencilgames.server.game.abc.field;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.FieldGame;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.c2s.PlayerMoveC2SPacket;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c.InvalidMoveS2CPacket;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c.PlayerMoveS2CPacket;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import com.github.shap_po.pencilgames.server.network.ServerPackets;
import com.github.shap_po.pencilgames.server.network.ServerPlayer;

public abstract class ServerFieldGame<C> extends Game<ServerGameLobby> implements FieldGame<C> {
    protected final GameField<C> gameField;

    public ServerFieldGame(ServerGameLobby lobby, GameField<C> gameField) {
        super(lobby);
        this.gameField = gameField;
    }

    @Override
    public void onStart() {
        ServerPackets.registerPacketType(PlayerMoveC2SPacket.PACKET_TYPE);
        ServerPackets.registerReceiver(PlayerMoveC2SPacket.PACKET_TYPE, this::onPlayerMove);
    }

    @Override
    public void onEnd() {
        ServerPackets.unregisterPacketType(PlayerMoveC2SPacket.PACKET_TYPE);
    }

    private void onPlayerMove(PlayerMoveC2SPacket packet, ServerPackets.ServerPacketContext context) {
        PencilGamesServer.LOGGER.debug("Player {} moved to ({}, {})", context.player().getId(), packet.x(), packet.y());

        ServerPlayer player = context.player();
        int x = packet.x();
        int y = packet.y();

        if (!validateMove(player, x, y)) {
            PencilGamesServer.LOGGER.debug("Move ({}, {}) is invalid", x, y);
            player.connectionHandler().sendPacket(new InvalidMoveS2CPacket(x, y, gameField.get(x, y)));
            return;
        }

        handleMove(player.getId(), x, y);
        lobby.broadcastPacket(new PlayerMoveS2CPacket(player.getId(), x, y), player.getId());
    }

    /**
     * Checks if the move is valid: is on the field and is the player's turn.
     *
     * @param player the player performing the move
     * @param x      x coordinate
     * @param y      y coordinate
     * @return true if the move is valid
     */
    public boolean validateMove(ServerPlayer player, int x, int y) {
        return gameField.isOnField(x, y) && isPlayerTurn(player);
    }
}
