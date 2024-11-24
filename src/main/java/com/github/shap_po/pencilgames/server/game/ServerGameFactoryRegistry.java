package com.github.shap_po.pencilgames.server.game;

import com.github.shap_po.pencilgames.common.game.data.GameFactory;
import com.github.shap_po.pencilgames.common.game.impl.tictactoe.TicTacToeGame;
import com.github.shap_po.pencilgames.common.registry.SimpleRegistry;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.server.game.impl.ServerTicTacToeGame;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;

/**
 * Registry of all game factories on the server side.
 * A game factory must be registered here to be used.
 */
public class ServerGameFactoryRegistry {
    public static final SimpleRegistry<Identifier, GameFactory<ServerGameLobby,?>> REGISTRY = new SimpleRegistry<>();

    static {
        register(ServerTicTacToeGame.getFactory());
    }

    public static void register(GameFactory<ServerGameLobby,?> factory) {
        REGISTRY.add(factory.getId(), factory);
    }
}
