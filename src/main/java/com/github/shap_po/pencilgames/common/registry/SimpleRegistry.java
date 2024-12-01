package com.github.shap_po.pencilgames.common.registry;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Simple implementation of {@link Registry}
 *
 * @param <K> key type
 * @param <V> value type
 */
public class SimpleRegistry<K, V> implements Registry<K, V> {
    protected final Map<K, V> registry = new HashMap<>();

    @Override
    @Nullable
    public V get(@Nullable K id) {
        return registry.get(id);
    }

    @Override
    public void remove(@NonNull K id) {
        registry.remove(id);
    }

    @Override
    public void add(@NonNull K id, @NonNull V value) throws IllegalArgumentException {
        if (registry.containsKey(id)) {
            throw new IllegalArgumentException("Id " + id + " already exists");
        }
        registry.put(id, value);
    }

    @Override
    public Set<K> getKeys() {
        return new HashSet<>(registry.keySet());
    }

    @Override
    public Set<V> getValues() {
        return new HashSet<>(registry.values());
    }

    @Override
    public Map<K, V> getMap() {
        return new HashMap<>(registry);
    }
}
