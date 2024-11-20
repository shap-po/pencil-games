package com.github.shap_po.pencilgames.common.game.data;

import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.util.Identifier;

import java.util.function.Function;

/**
 * Factory for game types. The factory provides all the necessary information to create a game type.
 */
public class GameFactory<G extends Game> implements Function<GameLobby<?>, G> {
    private final Identifier id;
    private final Function<GameLobby<?>, G> constructorFunction;
    private final Runnable registerClientMethod;
    private final Runnable registerServerMethod;

    public GameFactory(
        Identifier id,
        Function<GameLobby<?>, G> constructorFunction,
        Runnable registerClientMethod,
        Runnable registerServerMethod
    ) {
        this.id = id;
        this.constructorFunction = constructorFunction;
        this.registerClientMethod = registerClientMethod;
        this.registerServerMethod = registerServerMethod;
    }

    public Identifier getId() {
        return id;
    }

    public void registerClient() {
        registerClientMethod.run();
    }

    public void registerServer() {
        registerServerMethod.run();
    }

    @Override
    public G apply(GameLobby<?> lobby) {
        return constructorFunction.apply(lobby);
    }
}
