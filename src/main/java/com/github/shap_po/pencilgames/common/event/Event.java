package com.github.shap_po.pencilgames.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An event that can be subscribed to by multiple handlers.
 *
 * @param <T> the type of the event
 */
public class Event<T> implements Consumer<T> {
    private final List<Consumer<T>> handlers = new ArrayList<>();

    public void register(Consumer<T> handler) {
        handlers.add(handler);
    }

    @Override
    public void accept(T t) {
        for (Consumer<T> handler : handlers) {
            handler.accept(t);
        }
    }
}
