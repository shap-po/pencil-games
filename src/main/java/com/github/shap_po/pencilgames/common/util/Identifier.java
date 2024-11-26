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

    /**
     * Creates an identifier.
     *
     * @param value the value
     * @return the identifier
     * @see #fromString for parsing
     */
    public static Identifier of(String value) {
        return new Identifier(value);
    }

    /**
     * Gets an identifier from a string.
     *
     * @param string the string
     * @return the identifier
     * @see #of for creating
     */
    public static Identifier fromString(String string) {
        return new Identifier(string);
    }

    @Override
    public String toString() {
        return id;
    }
}
