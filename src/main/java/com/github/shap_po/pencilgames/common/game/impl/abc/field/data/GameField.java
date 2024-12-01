package com.github.shap_po.pencilgames.common.game.impl.abc.field.data;

import com.github.shap_po.pencilgames.common.event.type.ConsumerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Represents a game field/board.
 *
 * @param <C> cell type
 */
public class GameField<C> {
    private final List<List<C>> cells;
    public final ConsumerEvent<ChangeEvent<C>> onChange = new ConsumerEvent<>();

    public GameField(List<List<C>> cells) {
        this.cells = cells;
    }

    /**
     * Create a new game field filled with a value factory.
     *
     * @param valueFactory the factory that accepts x and y coordinates
     * @param width        width
     * @param height       height
     * @param <C>          cell type
     * @return new game field
     */
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

    /**
     * Create a new game field filled with a value supplier.
     *
     * @param valueSupplier the supplier
     * @param width         width
     * @param height        height
     * @param <C>           cell type
     * @return new game field
     */
    public static <C> GameField<C> of(Supplier<C> valueSupplier, int width, int height) {
        return of((x, y) -> valueSupplier.get(), width, height);
    }

    /**
     * Create a new game field filled with a default value.
     *
     * @param value  default value
     * @param width  width
     * @param height height
     * @param <C>    cell type
     * @return new game field
     */
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
        if (!isOnField(x, y)) {
            return;
        }
        C oldValue = get(x, y);
        cells.get(y).set(x, value);

        onChange.accept(new ChangeEvent<>(x, y, oldValue, value));
    }

    public boolean isOnField(int x, int y) {
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    public record ChangeEvent<C>(int x, int y, C oldValue, C newValue) {
    }
}
