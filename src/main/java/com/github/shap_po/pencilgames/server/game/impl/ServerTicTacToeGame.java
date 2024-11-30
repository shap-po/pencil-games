package com.github.shap_po.pencilgames.server.game.impl;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c.PlayerMoveS2CPacket;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.game.abc.field.ServerFieldGame;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import com.github.shap_po.pencilgames.server.network.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerTicTacToeGame extends ServerFieldGame<TicTacToeGame.Cell> implements TicTacToeGame {
    private final Map<UUID, Cell> playerToCellMap;

    public ServerTicTacToeGame(ServerGameLobby lobby) {
        super(lobby, GameField.of(Cell.EMPTY, size.left(), size.right()));

        List<UUID> players = lobby.getPlayerManager().getPlayerOrder();
        this.playerToCellMap = TicTacToeGame.createPlayerToCellMap(players);
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

        PencilGamesServer.LOGGER.debug("Player {} moved to ({}, {}) with cell {}", playerId, x, y, c);

        gameField.set(x, y, c);
        lobby.broadcastPacket(new PlayerMoveS2CPacket(playerId, x, y));

        return c;
    }

    public static GameFactory<ServerGameLobby, Game<ServerGameLobby>> getFactory() {
        return new GameFactory<>(GAME_ID, ServerTicTacToeGame::new);
    }
}
