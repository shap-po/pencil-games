package com.github.shap_po.pencilgames.server.game.impl;

import com.github.shap_po.pencilgames.common.game.data.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c.PlayerMoveS2CPacket;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;
import com.github.shap_po.pencilgames.server.game.abc.field.ServerFieldGame;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import com.github.shap_po.pencilgames.server.network.ServerPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ServerTicTacToeGame extends ServerFieldGame<TicTacToeGame.Cell> implements TicTacToeGame {
    private final HashMap<UUID, TicTacToeGame.Cell> playerToCellMap = new HashMap<>();

    public ServerTicTacToeGame(ServerGameLobby lobby) {
        super(lobby, GameField.of(TicTacToeGame.Cell.EMPTY, TicTacToeGame.size.left(), TicTacToeGame.size.right()));

        List<UUID> players = lobby.getPlayers().keySet().stream().toList();
        for (int i = 0; i < players.size(); i++) {
            this.playerToCellMap.put(players.get(i), TicTacToeGame.Cell.values()[i]);
        }
    }

    @Override
    public GameField<Cell> getGameField() {
        return null;
    }

    @Override
    public Cell playerToCell(UUID playerId) {
        return playerToCellMap.getOrDefault(playerId, TicTacToeGame.Cell.EMPTY);
    }

    @Override
    public boolean validateMove(ServerPlayer player, int x, int y) {
        return super.validateMove(player, x, y) && gameField.get(x, y) == TicTacToeGame.Cell.EMPTY;
    }

    @Override
    public void handleMove(UUID playerId, int x, int y) {
        gameField.set(x, y, playerToCell(playerId));
        lobby.broadcastPacket(new PlayerMoveS2CPacket(playerId, x, y));
    }

    public static GameFactory<ServerGameLobby, ServerTicTacToeGame> getFactory() {
        return new GameFactory<>(TicTacToeGame.GAME_ID, ServerTicTacToeGame::new);
    }
}
