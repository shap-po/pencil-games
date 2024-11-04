package com.github.shap_po.pencilgames.common.event;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An event that can be subscribed to by multiple handlers.
 *
 * @param <T> the type of the event
 */
public class Event<T> {
    private final List<T> handlers = new CopyOnWriteArrayList<>();

    public List<T> getHandlers() {
        return handlers;
    }

    public void register(@NotNull T handler) {
        handlers.add(handler);
    }

    public void unregister(@NotNull T handler) {
        handlers.remove(handler);
    }

    public void clear() {
        handlers.clear();
    }
}
