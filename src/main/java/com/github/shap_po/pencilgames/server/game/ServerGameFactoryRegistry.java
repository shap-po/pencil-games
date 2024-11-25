package com.github.shap_po.pencilgames.server.game;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.registry.SimpleRegistry;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.server.game.impl.ServerTicTacToeGame;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;

/**
 * Registry of all game factories on the server side.
 * A game factory must be registered here to be used.
 */
public class ServerGameFactoryRegistry {
    public static final SimpleRegistry<Identifier, GameFactory<ServerGameLobby, Game<ServerGameLobby>>> REGISTRY = new SimpleRegistry<>();

    static {
        register(ServerTicTacToeGame.getFactory());
    }

    public static void register(GameFactory<ServerGameLobby, Game<ServerGameLobby>> factory) {
        REGISTRY.add(factory.getId(), factory);
    }
}
