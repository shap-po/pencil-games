package com.github.shap_po.pencilgames.common.registry;

import com.google.common.collect.ImmutableBiMap;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Base Registry interface.
 * Registries are used to store objects with unique ids
 *
 * @param <K> key type
 * @param <V> value type
 * @see SimpleRegistry
 */
public interface Registry<K, V> {
    @Nullable V get(@Nullable K id);

    default Optional<V> getOrEmpty(@Nullable K id) {
        return Optional.ofNullable(this.get(id));
    }

    void add(@NonNull K id, @NonNull V value);

    void remove(@NonNull K id);

    default boolean contains(K id) {
        return this.get(id) != null;
    }

    Set<K> getKeys();

    Set<Map.Entry<K, V>> getEntries();

    ImmutableBiMap<K, V> getMap();
}
