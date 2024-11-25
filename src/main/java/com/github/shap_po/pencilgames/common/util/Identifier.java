package com.github.shap_po.pencilgames.common.util;

import java.io.Serializable;

/**
 * An identifier for an object. Most of the time used as a key in maps.
 *
 * @param id the id. Must contain only lowercase letters, numbers, underscores, and forward slashes.
 */
public record Identifier(String id) implements Comparable<Identifier>, Serializable {
    @Override
    public int compareTo(Identifier other) {
        return id.compareTo(other.id);
    }

    public static Identifier of(String id) {
        return new Identifier(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
