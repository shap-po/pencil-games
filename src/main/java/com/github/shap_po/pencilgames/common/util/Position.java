package com.github.shap_po.pencilgames.common.util;

public class Position extends Pair<Integer, Integer> {
    protected Position(int x, int y) {
        super(x, y);
    }

    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    public int getX() {
        return getLeft();
    }

    public int getY() {
        return getRight();
    }

    public Position add(int x, int y) {
        return of(getX() + x, getY() + y);
    }

    public Position add(Position position) {
        return add(position.getX(), position.getY());
    }

    public Position subtract(int x, int y) {
        return of(getX() - x, getY() - y);
    }

    public Position subtract(Position position) {
        return subtract(position.getX(), position.getY());
    }
}
