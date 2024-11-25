package com.github.shap_po.pencilgames.client.game.impl;

import com.github.shap_po.pencilgames.client.game.abc.field.ClientFieldGame;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClientTicTacToeGame extends ClientFieldGame<TicTacToeGame.Cell> implements TicTacToeGame {
    private final HashMap<UUID, Cell> playerToCellMap = new HashMap<>();

    public ClientTicTacToeGame(ClientGameLobby lobby) {
        super(lobby, GameField.of(Cell.EMPTY, TicTacToeGame.size.left(), TicTacToeGame.size.right()));

        List<UUID> players = lobby.getPlayers().keySet().stream().toList();
        for (int i = 0; i < players.size(); i++) {
            this.playerToCellMap.put(players.get(i), Cell.of(i + 1));
        }
    }

    @Override
    public Cell playerToCell(UUID playerId) {
        return playerToCellMap.getOrDefault(playerId, Cell.EMPTY);
    }

    @Override
    public boolean validateMove(int x, int y) {
        return super.validateMove(x, y) && gameField.get(x, y) == Cell.EMPTY;
    }

    @Override
    public Cell handleMove(UUID playerId, int x, int y) {
        Cell c = playerToCell(playerId);
        gameField.set(x, y, c);

        return c;
    }

    public static GameFactory<ClientGameLobby, ClientTicTacToeGame> getFactory() {
        return new GameFactory<>(TicTacToeGame.GAME_ID, ClientTicTacToeGame::new);
    }
}
