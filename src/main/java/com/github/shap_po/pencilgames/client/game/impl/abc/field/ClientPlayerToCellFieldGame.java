package com.github.shap_po.pencilgames.client.game.impl.abc.field;

import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.PlayerToCellFieldGame;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public abstract class ClientPlayerToCellFieldGame<C> extends ClientFieldGame<C> implements PlayerToCellFieldGame<C> {
    private final Map<UUID, C> playerToCellMap;
    private final C emptyCell;

    public ClientPlayerToCellFieldGame(ClientGameLobby lobby, GameField<C> gameField, Function<Integer, C> cellOf, C emptyCell) {
        super(lobby, gameField);

        this.emptyCell = emptyCell;

        this.gameScreen.setChangeHandler(this::screenCellChangeHandler);
        this.gameScreen.redraw();

        List<UUID> players = lobby.getPlayerManager().getPlayerOrder();
        this.playerToCellMap = PlayerToCellFieldGame.createPlayerToCellMap(players, cellOf);
    }

    @Override
    public C playerToCell(UUID playerId) {
        return playerToCellMap.getOrDefault(playerId, emptyCell);
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
        return super.validateMove(x, y) && emptyCell.equals(gameField.get(x, y));
    }

    @Override
    public void handleMove(UUID playerId, int x, int y) {
        nextPlayer();

        C c = playerToCell(playerId);
        gameField.set(x, y, c);
    }

    private void screenCellChangeHandler(int x, int y, C cell) {
        if (cell == null) return;

        gameScreen.getButton(x, y).setText(cell.toString());
        gameScreen.getButton(x, y).setEnabled(emptyCell.equals(cell));
    }
}
