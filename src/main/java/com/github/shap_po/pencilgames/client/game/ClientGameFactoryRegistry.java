package com.github.shap_po.pencilgames.client.game;

import com.github.shap_po.pencilgames.client.game.impl.go.ClientGoGame;
import com.github.shap_po.pencilgames.client.game.impl.tictactoe.ClientTicTacToeGame;
import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.registry.SimpleRegistry;
import com.github.shap_po.pencilgames.common.util.Identifier;

/**
 * Registry of all game factories on the server side.
 * A game factory must be registered here to be used.
 */
public class ClientGameFactoryRegistry {
    public static final SimpleRegistry<Identifier, GameFactory<ClientGameLobby, ? extends ClientGame>> REGISTRY = new SimpleRegistry<>();

    static {
        register(ClientTicTacToeGame.getFactory());
        register(ClientGoGame.getFactory());
    }

    public static void register(GameFactory<ClientGameLobby, ? extends ClientGame> factory) {
        REGISTRY.add(factory.getId(), factory);
    }
}
