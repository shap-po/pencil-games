package com.github.shap_po.pencilgames.common.game.data;

import com.github.shap_po.pencilgames.common.data.Settings;
import com.github.shap_po.pencilgames.common.game.GameLobby;
import com.github.shap_po.pencilgames.common.util.Identifier;

import java.util.function.BiFunction;

/**
 * Factory for game types. The factory provides all the necessary information to create a game type.
 */
public class GameFactory<G extends Game> implements BiFunction<GameLobby<?>, Settings.Instance, G> {
    private final Identifier id;
    private final Settings optionsFactory;
    private final BiFunction<GameLobby<?>, Settings.Instance, G> constructorFunction;
    private final Runnable registerClientMethod;
    private final Runnable registerServerMethod;

    public GameFactory(
        Identifier id,
        Settings options,
        BiFunction<GameLobby<?>, Settings.Instance, G> constructorFunction,
        Runnable registerClientMethod,
        Runnable registerServerMethod
    ) {
        this.id = id;
        this.optionsFactory = options;
        this.constructorFunction = constructorFunction;
        this.registerClientMethod = registerClientMethod;
        this.registerServerMethod = registerServerMethod;
    }

    public Identifier getId() {
        return id;
    }

    public Settings getOptionsFactory() {
        return optionsFactory;
    }

    public void registerClient() {
        registerClientMethod.run();
    }

    public void registerServer() {
        registerServerMethod.run();
    }

    @Override
    public G apply(GameLobby<?> lobby, Settings.Instance options) {
        return constructorFunction.apply(lobby, options);
    }
}
