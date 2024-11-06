package com.github.shap_po.pencilgames.common.util;

/**
 * Holds two values
 *
 * @param left  left value
 * @param right right value
 * @param <L>   type of left
 * @param <R>   type of right
 */
public record Pair<L, R>(L left, R right) {
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }
}
