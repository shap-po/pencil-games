package com.github.shap_po.pencilgames.client.game.impl;

import com.github.shap_po.pencilgames.client.game.abc.field.ClientFieldGame;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClientTicTacToeGame extends ClientFieldGame<TicTacToeGame.Cell> implements TicTacToeGame {
    private final Map<UUID, Cell> playerToCellMap;

    public ClientTicTacToeGame(ClientGameLobby lobby) {
        super(lobby, GameField.of(Cell.EMPTY, TicTacToeGame.size.left(), TicTacToeGame.size.right()));

        this.gameScreen.setChangeHandler(this::screenCellChangeHandler);
        this.gameScreen.redraw();

        List<UUID> players = lobby.getPlayerManager().getPlayerOrder();
        this.playerToCellMap = TicTacToeGame.createPlayerToCellMap(players);
    }

    @Override
    public Cell playerToCell(UUID playerId) {
        return playerToCellMap.getOrDefault(playerId, Cell.EMPTY);
    }

    /**
     * Checks if the move is valid: is on the field, is the player's turn, and the cell is empty.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return true if the move is valid
     */
    @Override
    public boolean validateMove(int x, int y) {
        return super.validateMove(x, y) && gameField.get(x, y) == Cell.EMPTY;
    }

    @Override
    public void handleMove(UUID playerId, int x, int y) {
        nextPlayer();

        Cell c = playerToCell(playerId);
        gameField.set(x, y, c);
    }

    private void screenCellChangeHandler(int x, int y, Cell cell) {
        if (cell == null) return;

        gameScreen.getButton(x, y).setText(cell.toString());
        gameScreen.getButton(x, y).setEnabled(cell == Cell.EMPTY);
    }

    public static GameFactory<ClientGameLobby, ClientTicTacToeGame> getFactory() {
        return new GameFactory<>(TicTacToeGame.GAME_ID, ClientTicTacToeGame::new);
    }
}
