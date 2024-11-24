package com.github.shap_po.pencilgames.client.game.abc.field;

import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.client.network.ClientPackets;
import com.github.shap_po.pencilgames.common.game.data.Game;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.FieldGame;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c.InvalidMoveS2CPacket;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c.PlayerMoveS2CPacket;

import java.util.UUID;

public abstract class ClientFieldGame<C> extends Game<ClientGameLobby> implements FieldGame<C> {
    protected final GameField<C> gameField;

    public ClientFieldGame(ClientGameLobby lobby, GameField<C> gameField) {
        super(lobby);
        this.gameField = gameField;
    }

    @Override
    public void onStart() {
        ClientPackets.registerPacketType(PlayerMoveS2CPacket.PACKET_TYPE);
        ClientPackets.registerPacketType(InvalidMoveS2CPacket.PACKET_TYPE);
        ClientPackets.registerReceiver(PlayerMoveS2CPacket.PACKET_TYPE, this::onPlayerMove);
    }

    @Override
    public void onEnd() {
        ClientPackets.unregisterPacketType(PlayerMoveS2CPacket.PACKET_TYPE);
        ClientPackets.registerPacketType(InvalidMoveS2CPacket.PACKET_TYPE);
    }

    private void onPlayerMove(PlayerMoveS2CPacket packet, ClientPackets.ClientPacketContext context) {
        UUID player = packet.playerID();
        int x = packet.x();
        int y = packet.y();

        handleMove(player, x, y);
    }
}
