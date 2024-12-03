package com.github.shap_po.pencilgames.client.game.abc.field;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
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
import com.github.shap_po.pencilgames.common.network.packet.s2c.game.EndGameS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class ClientFieldGame<C> extends ClientGame implements FieldGame<C> {
    protected final GameField<C> gameField;
    protected final FieldGameScreen<C> gameScreen;

    public ClientFieldGame(ClientGameLobby lobby, GameField<C> gameField) {
        super(lobby);

        this.gameField = gameField;
        this.gameScreen = new FieldGameScreen<>(PencilGamesClient.gameWindow, this);
    }

    @Override
    public GameField<C> getGameField() {
        return gameField;
    }

    @Override
    public void onStart() {
        super.onStart();
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
        ClientPackets.registerReceiver(EndGameS2CPacket.PACKET_TYPE, (packet, context) -> {
            gameScreen.disableInteractions();
        });
    }

    @Override
    public void onEnd() {
        super.onEnd();
        ClientPackets.unregisterPacketType(PlayerMoveS2CPacket.PACKET_TYPE);
        ClientPackets.unregisterPacketType(InvalidMoveS2CPacket.PACKET_TYPE);
    }

    /**
     * Checks if the move is valid: is on the field and is the player's turn.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return true if the move is valid
     */
    public boolean validateMove(int x, int y) {
        return gameField.isOnField(x, y) && isMyTurn();
    }

    /**
     * Handles player's move on the field and returns the new cell state.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return the new cell state
     */
    public @Nullable C move(int x, int y) {
        if (!validateMove(x, y)) {
            return null;
        }

        UUID playerId = lobby.getLocalPlayerId();
        if (playerId == null) {
            PencilGamesClient.LOGGER.warn("Tried to make a move before the player id was set");
            return null;
        }

        handleMove(playerId, x, y);
        lobby.sendPacket(new PlayerMoveC2SPacket(x, y));

        return gameField.get(x, y);
    }

    @Override
    public GameScreen<ClientFieldGame<C>> getGameScreen(GameWindow root) {
        return gameScreen;
    }
}
