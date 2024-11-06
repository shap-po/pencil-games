package com.github.shap_po.pencilgames.common.game;

import com.github.shap_po.pencilgames.common.game.data.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;
import com.github.shap_po.pencilgames.common.registry.SimpleRegistry;
import com.github.shap_po.pencilgames.common.util.Identifier;

/**
 * Registry of all game factories.
 * A game factory must be registered here to be used.
 */
public class GameFactoryRegistry {
    public static final SimpleRegistry<Identifier, GameFactory<?>> REGISTRY = new SimpleRegistry<>();

    static {
        register(TicTacToeGame.getFactory());
    }

    public static void register(GameFactory<?> factory) {
        REGISTRY.add(factory.getId(), factory);
    }
}
