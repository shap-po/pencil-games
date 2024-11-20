package com.github.shap_po.pencilgames.common.game.impl.abc.field;

import com.github.shap_po.pencilgames.client.network.ClientPackets;
import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.game.data.Game;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.c2s.PlayerMoveC2SPacket;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c.PlayerMoveS2CPacket;
import com.github.shap_po.pencilgames.server.network.ServerPackets;

public abstract class FieldGame<T> extends Game {
    protected final GameField<T> gameField;

    public FieldGame(GameLobby<?> lobby, GameField<T> gameField) {
        super(lobby);
        this.gameField = gameField;
    }


    public static void registerClient() {
        // FIXME: ClientPackets does not exist in the server environment
        ClientPackets.registerPacketType(PlayerMoveS2CPacket.PACKET_TYPE);
    }

    public static void registerServer() {
        ServerPackets.REGISTRY.registerPacketType(PlayerMoveC2SPacket.PACKET_TYPE);
    }
}
