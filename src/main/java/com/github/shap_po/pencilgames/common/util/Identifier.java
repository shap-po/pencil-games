package com.github.shap_po.pencilgames.common.util;

import java.io.Serializable;

/**
 * An identifier for an object. Most of the time used as a key in maps.
 * <p>
 * Makes it easy to distinguish text strings from identifiers in code.
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
     * <p>
     * Use {@link #fromString} for parsing identifier strings.
     *
     * @param value the value
     * @return the identifier
     */
    public static Identifier of(String value) {
        return new Identifier(value);
    }

    /**
     * Parses an identifier from a string.
     * <p>
     * Use {@link #of} for creating identifiers.
     *
     * @param string the string
     * @return the identifier
     */
    public static Identifier fromString(String string) {
        return new Identifier(string);
    }

    @Override
    public String toString() {
        return id;
    }
}
