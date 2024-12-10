package com.github.shap_po.pencilgames.common.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * Holds two values
 *
 * @param <L> type of left
 * @param <R> type of right
 */
public class Pair<L, R> implements Serializable {
    private final L left;
    private final R right;

    protected Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + ", " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair<?, ?> pair)) return false;
        return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
    }
}
