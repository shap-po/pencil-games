package com.github.shap_po.pencilgames.common.event.type;

import com.github.shap_po.pencilgames.common.event.Event;

import java.util.function.BiConsumer;

public class BiConsumerEvent<T, U> extends Event<BiConsumer<T, U>> implements BiConsumer<T, U> {
    @Override
    public void accept(T t, U u) {
        getHandlers().forEach(handler -> handler.accept(t, u));
    }

    public static <T, U> BiConsumerEvent<T, U> create() {
        return new BiConsumerEvent<>();
    }
}