package com.github.shap_po.pencilgames.server.game.impl;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
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
    private final HashMap<UUID, Cell> playerToCellMap = new HashMap<>();

    public ServerTicTacToeGame(ServerGameLobby lobby) {
        super(lobby, GameField.of(Cell.EMPTY, size.left(), size.right()));

        List<UUID> players = lobby.getPlayers().keySet().stream().toList();
        for (int i = 0; i < players.size(); i++) {
            this.playerToCellMap.put(players.get(i), Cell.values()[i]);
        }
    }

    @Override
    public GameField<Cell> getGameField() {
        return null;
    }

    @Override
    public Cell playerToCell(UUID playerId) {
        return playerToCellMap.getOrDefault(playerId, Cell.EMPTY);
    }

    @Override
    public boolean validateMove(ServerPlayer player, int x, int y) {
        // TODO: check if player's turn
        return super.validateMove(player, x, y) && gameField.get(x, y) == Cell.EMPTY;
    }

    @Override
    public Cell handleMove(UUID playerId, int x, int y) {
        Cell c = playerToCell(playerId);

        gameField.set(x, y, c);
        lobby.broadcastPacket(new PlayerMoveS2CPacket(playerId, x, y));

        return c;
    }

    public static GameFactory<ServerGameLobby, Game<ServerGameLobby>> getFactory() {
        return new GameFactory<>(GAME_ID, ServerTicTacToeGame::new);
    }
}
