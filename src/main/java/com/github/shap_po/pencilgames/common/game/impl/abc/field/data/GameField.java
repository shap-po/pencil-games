package com.github.shap_po.pencilgames.common.game.impl.abc.field.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public record GameField<C>(List<List<C>> cells) {
    public static <C> GameField<C> of(BiFunction<Integer, Integer, C> valueFactory, int width, int height) {
        List<List<C>> cells = new ArrayList<>(height);

        for (int y = 0; y < height; y++) {
            List<C> row = new ArrayList<>(width);

            for (int x = 0; x < width; x++) {
                row.add(valueFactory.apply(x, y));
            }

            cells.add(row);
        }

        return new GameField<>(cells);
    }

    public static <C> GameField<C> of(Supplier<C> valueSupplier, int width, int height) {
        return of((x, y) -> valueSupplier.get(), width, height);
    }

    public static <C> GameField<C> of(C value, int width, int height) {
        return of((x, y) -> value, width, height);
    }

    public int getWidth() {
        if (cells.isEmpty()) {
            return 0;
        }
        return cells.getFirst().size();
    }

    public int getHeight() {
        return cells.size();
    }

    public C get(int x, int y) {
        if (x >= getWidth() || y >= getHeight()) {
            return null;
        }
        return cells.get(y).get(x);
    }

    public void set(int x, int y, C value) {
        if (x >= getWidth() || y >= getHeight()) {
            return;
        }
        cells.get(y).set(x, value);
    }

    public boolean isOnField(int x, int y) {
        return x >= getWidth() || y >= getHeight() || x < 0 || y < 0;
    }
}
