package com.github.shap_po.pencilgames.common.data;

import com.github.shap_po.pencilgames.common.util.Validatable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Represents a settings builder that provides a list of fields that must be set.
 *
 * @see com.github.shap_po.pencilgames.common.game.data.GameFactory
 */
public class Settings {
    private final Map<String, Field<?>> fields = new HashMap<>();

    public ImmutableMap<String, Field<?>> getFields() {
        return ImmutableMap.copyOf(fields);
    }

    public <T> Settings addField(String name, Field<T> field) {
        fields.put(name, field);
        return this;
    }

    public ImmutableSet<String> getFieldNames() {
        return ImmutableSet.copyOf(fields.keySet());
    }

    public boolean hasField(String name) {
        return fields.containsKey(name);
    }

    /**
     * A filled instance of the settings.
     */
    public class Instance implements Validatable, Serializable {
        private final Map<String, Object> values = new HashMap<>();

        public Instance() {
            Settings.this.getFields().forEach((name, field) -> {
                Object value = field.hasDefaultValue() ? field.getDefaultValue() : null;
                values.put(name, value);
            });
        }

        public Instance set(String name, Object value) {
            this.values.put(name, value);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String name) throws IllegalArgumentException {
            if (!hasValue(name)) {
                throw new IllegalArgumentException("Tried to get field \"" + name + "\" from data " + this + ", which did not exist.");
            }
            return (T) values.get(name);
        }

        public boolean hasValue(String name) {
            if (!hasField(name)) {
                return false;
            }
            return values.get(name) != null;
        }

        @Override
        public void validate() throws IllegalArgumentException {
            Settings.this.getFields().forEach((name, field) -> {
                if (!hasValue(name) && !field.hasDefaultValue()) {
                    throw new IllegalArgumentException("Tried to get field \"" + name + "\" from data " + this + ", which did not exist.");
                }
            });
        }
    }

    public static class Field<T> implements Serializable {
        // default value is a Supplier to prevent overwriting it
        private final @Nullable Supplier<T> defaultValue;

        public Field(@Nullable Supplier<T> defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Field() {
            this(null);
        }

        public boolean hasDefaultValue() {
            return defaultValue != null;
        }

        public T getDefaultValue() {
            if (defaultValue == null) {
                throw new IllegalStateException("This field does not have a default value.");
            }
            return defaultValue.get();
        }
    }
}
