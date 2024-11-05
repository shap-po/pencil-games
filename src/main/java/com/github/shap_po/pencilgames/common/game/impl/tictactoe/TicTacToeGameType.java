package com.github.shap_po.pencilgames.common.game.impl.tictactoe;

import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.game.data.Field;
import com.github.shap_po.pencilgames.common.game.type.FieldGameType;
import com.google.common.collect.BiMap;

import java.util.UUID;

public class TicTacToeGameType extends FieldGameType<TicTacToeGameType.Cell> {
    private BiMap<UUID, Cell> playerToCell;

    public TicTacToeGameType(GameLobby<?> lobby, Field<Cell> field) {
        super(lobby, field);
    }

    public static void register() {

    }

    @Override
    public void start() {

    }

    @Override
    public void addPlayer(UUID playerId) {
        if (playerToCell.size() >= 2) {
            throw new IllegalStateException("Game is full");
        }
        if (playerToCell.containsKey(playerId)) {
            return; // player is already in the game, do nothing
        }

        Cell cell = Cell.values()[playerToCell.size() + 1];
        playerToCell.put(playerId, cell);
    }

    public enum Cell {
        EMPTY, X, O
    }
}

