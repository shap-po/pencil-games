package com.github.shap_po.pencilgames.common.event.type;

import com.github.shap_po.pencilgames.common.event.Event;

import java.util.function.Consumer;

public class ConsumerEvent<T> extends Event<Consumer<T>> implements Consumer<T> {
    @Override
    public void accept(T t) {
        getHandlers().forEach(handler -> handler.accept(t));
    }

    public static <T> ConsumerEvent<T> create() {
        return new ConsumerEvent<>();
    }
}