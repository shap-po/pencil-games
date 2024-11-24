package com.github.shap_po.pencilgames.common.game;

import com.github.shap_po.pencilgames.common.util.Identifier;

import java.util.function.Function;

/**
 * Factory for game types. The factory provides all the necessary information to create a game type.
 */
public class GameFactory<L extends GameLobby<?>, G extends Game<L>> implements Function<L, G> {
    private final Identifier id;
    private final Function<L, G> constructorFunction;

    // TODO: check for min/max players requirement
    public GameFactory(
        Identifier id,
        Function<L, G> constructorFunction
    ) {
        this.id = id;
        this.constructorFunction = constructorFunction;
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public G apply(L lobby) {
        return constructorFunction.apply(lobby);
    }
}
