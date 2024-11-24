package com.github.shap_po.pencilgames.client.game.abc.field;

import com.github.shap_po.pencilgames.client.game.ClientGame;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.client.network.ClientPackets;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.screen.game.FieldGameScreen;
import com.github.shap_po.pencilgames.client.ui.screen.game.GameScreen;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.FieldGame;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.c2s.PlayerMoveC2SPacket;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c.InvalidMoveS2CPacket;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c.PlayerMoveS2CPacket;

import java.util.UUID;

public abstract class ClientFieldGame<C> extends ClientGame implements FieldGame<C> {
    protected final GameField<C> gameField;

    public ClientFieldGame(ClientGameLobby lobby, GameField<C> gameField) {
        super(lobby);
        this.gameField = gameField;
    }

    @Override
    public void onStart() {
        ClientPackets.registerPacketType(PlayerMoveS2CPacket.PACKET_TYPE);
        ClientPackets.registerPacketType(InvalidMoveS2CPacket.PACKET_TYPE);

        ClientPackets.registerReceiver(PlayerMoveS2CPacket.PACKET_TYPE, (packet, context) -> {
            UUID player = packet.playerID();
            int x = packet.x();
            int y = packet.y();

            handleMove(player, x, y);
        });
        // Restore the correct cell state if the player made an invalid move
        ClientPackets.registerReceiver(InvalidMoveS2CPacket.PACKET_TYPE, (packet, context) -> {
            int x = packet.x();
            int y = packet.y();

            @SuppressWarnings({"unchecked"})
            C lastState = (C) packet.lastState();

            gameField.set(x, y, lastState);
        });
    }

    @Override
    public void onEnd() {
        ClientPackets.unregisterPacketType(PlayerMoveS2CPacket.PACKET_TYPE);
        ClientPackets.registerPacketType(InvalidMoveS2CPacket.PACKET_TYPE);
    }

    /**
     * Checks if the move is valid.
     * Defaults to making sure the move is on the field.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return true if the move is valid
     */
    public boolean validateMove(int x, int y) {
        return gameField.isOnField(x, y);
    }

    public void move(int x, int y) {
        if (!validateMove(x, y)) {
            return;
        }

        handleMove(lobby.getLocalPlayer().getId(), x, y);
        lobby.sendPacket(new PlayerMoveC2SPacket(x, y));
    }

    @Override
    public GameScreen<ClientFieldGame<C>> getGameScreen(GameWindow root) {
        return new FieldGameScreen<>(root, this);
    }
}
