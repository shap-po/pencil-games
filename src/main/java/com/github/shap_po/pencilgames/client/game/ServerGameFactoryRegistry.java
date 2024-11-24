package com.github.shap_po.pencilgames.client.game;

import com.github.shap_po.pencilgames.client.game.impl.ClientTicTacToeGame;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.common.game.data.GameFactory;
import com.github.shap_po.pencilgames.common.registry.SimpleRegistry;
import com.github.shap_po.pencilgames.common.util.Identifier;

/**
 * Registry of all game factories on the server side.
 * A game factory must be registered here to be used.
 */
public class ServerGameFactoryRegistry {
    public static final SimpleRegistry<Identifier, GameFactory<ClientGameLobby, ?>> REGISTRY = new SimpleRegistry<>();

    static {
        register(ClientTicTacToeGame.getFactory());
    }

    public static void register(GameFactory<ClientGameLobby, ?> factory) {
        REGISTRY.add(factory.getId(), factory);
    }
}
