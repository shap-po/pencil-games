package com.github.shap_po.pencilgames.server.game.impl.go;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.data.GameField;
import com.github.shap_po.pencilgames.common.game.impl.abc.field.packet.s2c.FieldUpdatesS2CPacket;
import com.github.shap_po.pencilgames.common.game.impl.go.GoGame;
import com.github.shap_po.pencilgames.common.util.Position;
import com.github.shap_po.pencilgames.server.game.impl.abc.field.ServerPlayerToCellFieldGame;
import com.github.shap_po.pencilgames.server.game.impl.go.data.GoCellChain;
import com.github.shap_po.pencilgames.server.game.player.ServerPlayer;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import com.github.shap_po.pencilgames.common.util.Direction;

import java.util.*;

public class ServerGoGame extends ServerPlayerToCellFieldGame<GoGame.Cell> implements GoGame {
    private int moveCount = 0;
    private final Map<UUID, Integer> cellsCaptured;
    private final GameField<GoCellChain<Cell>> chains;

    public ServerGoGame(ServerGameLobby lobby) {
        super(lobby, GameField.of(GoGame.Cell.EMPTY, GoGame.SIZE.getLeft(), GoGame.SIZE.getRight()), GoGame.Cell::of, GoGame.Cell.EMPTY);

        cellsCaptured = new HashMap<>();
        for (UUID playerId : lobby.getPlayerManager().getPlayerOrder()) {
            cellsCaptured.put(playerId, 0);
        }

        chains = GameField.of(() -> null, GoGame.SIZE.getLeft(), GoGame.SIZE.getRight());
    }

    @Override
    public void handleMove(UUID playerId, int x, int y) {
        GoGame.Cell cell = playerToCell(playerId);
        if (cell == null) {
            return;
        }

        super.handleMove(playerId, x, y);
        handleUpdate(playerId, x, y);

        moveCount++;
        if (moveCount >= MOVE_COUNT || gameField.isFull(GoGame.Cell.EMPTY)) {
            end(getWinnersByCaptured());
        }
    }

    @Override
    public boolean validateMove(ServerPlayer player, int x, int y) {
        return super.validateMove(player, x, y) && !checkSuicide(playerToCell(player.getId()), x, y);
    }

    /**
     * Handles an update to the game field: captures cells and sends the new state to the players.
     *
     * @param playerId player ID
     * @param x        x coordinate
     * @param y        y coordinate
     */
    private void handleUpdate(UUID playerId, int x, int y) {
        GoGame.Cell cell = gameField.get(x, y);
        assert cell != null;

        Map<Position, GoGame.Cell> updates = new HashMap<>();
        updates.put(Position.of(x, y), cell);
        int captured = 0;

        // create a new chain
        GoCellChain<GoGame.Cell> chain = new GoCellChain<>(cell, Position.of(x, y));
        chains.set(x, y, chain);

        for (Position direction : Direction.ADJACENT) {
            GoGame.Cell otherCell = gameField.get(direction.add(x, y));

            // add liberties for empty cells or cells not on the board
            if (otherCell == GoGame.Cell.EMPTY || otherCell == null) {
                chain.addLiberty();
                continue;
            }

            GoCellChain<GoGame.Cell> otherChain = chains.get(direction.add(x, y));
            assert otherChain != null;

            // merge chains
            if (otherCell == cell) {
                chain.merge(otherChain);
                // update positions of the other chain to point to the new one
                for (Position position : otherChain.getPositions()) {
                    chains.set(position.getX(), position.getY(), chain);
                }
            }

            // remove liberties from the chain of the other player
            otherChain.removeLiberty();

            // capture cells if they have no liberties
            if (!otherChain.hasLiberties()) {
                for (Position position : otherChain.getPositions()) {
                    chains.set(position.getX(), position.getY(), null);
                    gameField.set(position.getX(), position.getY(), GoGame.Cell.EMPTY);
                    updates.put(position, GoGame.Cell.EMPTY);
                }
                // add liberties to the chain
                chain.addLiberties(otherChain.getLiberties());
                // add captured cells to the temp score
                captured += otherChain.getPositions().size();
            }
        }

        // add captured cells to the score
        cellsCaptured.put(playerId, cellsCaptured.get(playerId) + captured);

        lobby.broadcastPacket(new FieldUpdatesS2CPacket(updates));
    }

    /**
     * Checks if a move is suicidal.
     *
     * @param cell cell
     * @param x    x coordinate
     * @param y    y coordinate
     */
    private boolean checkSuicide(GoGame.Cell cell, int x, int y) {
        // create a new chain
        GoCellChain<GoGame.Cell> chain = new GoCellChain<>(cell, Position.of(x, y));
        for (Position direction : Direction.ADJACENT) {
            GoGame.Cell otherCell = gameField.get(direction.add(x, y));

            // add liberties for empty cells or cells not on the board
            if (otherCell == GoGame.Cell.EMPTY || otherCell == null) {
                chain.addLiberty();
                continue;
            }
            if (otherCell != cell) {
                continue;
            }

            // merge chains
            GoCellChain<GoGame.Cell> otherChain = chains.get(direction.add(x, y));
            assert otherChain != null;
            chain.merge(otherChain);
        }

        return !chain.hasLiberties();
    }

    /**
     * Gets players with the most cells captured.
     *
     * @return the set of winners
     */
    private Set<UUID> getWinnersByCaptured() {
        Set<UUID> winners = new HashSet<>();

        int maxScore = 0;
        for (UUID playerId : cellsCaptured.keySet()) {
            if (cellsCaptured.get(playerId) > maxScore) {
                maxScore = cellsCaptured.get(playerId);
                winners.clear();
                winners.add(playerId);
            } else if (cellsCaptured.get(playerId) == maxScore) {
                winners.add(playerId);
            }
        }

        return winners;
    }

    /**
     * Gets players with the most cells on the board.
     *
     * @return the set of winners
     */
    private Set<UUID> getWinnersByPlaced() {
        Map<GoGame.Cell, Integer> stats = gameField.getStats();
        stats.remove(GoGame.Cell.EMPTY);

        Set<GoGame.Cell> mostPoints = new HashSet<>();
        int max = 0;
        for (Map.Entry<GoGame.Cell, Integer> entry : stats.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                mostPoints.clear();
                mostPoints.add(entry.getKey());
            } else if (entry.getValue() == max) {
                mostPoints.add(entry.getKey());
            }
        }

        Set<UUID> winners = new HashSet<>();
        for (GoGame.Cell c : mostPoints) {
            UUID playerId = cellToPlayer(c);
            if (playerId != null) {
                winners.add(playerId);
            }
        }

        return winners;
    }

    public static GameFactory<ServerGameLobby, Game<ServerGameLobby>> getFactory() {
        return new GameFactory<>(GoGame.GAME_ID, ServerGoGame::new);
    }
}
