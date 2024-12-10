package com.github.shap_po.pencilgames.server.game.impl.go.data;

import com.github.shap_po.pencilgames.common.util.Position;

import java.util.HashSet;
import java.util.Set;

public class GoCellChain<C> {
    private final C cell;
    private final Set<Position> positions = new HashSet<>();
    private int liberties = 0;

    public GoCellChain(C cell, Position start) {
        this.cell = cell;
        positions.add(start);
    }

    public C getCell() {
        return cell;
    }

    public Set<Position> getPositions() {
        return Set.copyOf(positions);
    }

    public int getLiberties() {
        return liberties;
    }

    public void setLiberties(int liberties) {
        this.liberties = liberties;
    }

    public boolean hasLiberties() {
        return liberties > 0;
    }

    public void addLiberties(int n) {
        liberties += n;
    }

    public void addLiberty() {
        addLiberties(1);
    }

    public void removeLiberties(int n) {
        liberties -= n;
    }

    public void removeLiberty() {
        removeLiberties(1);
    }

    public int size() {
        return positions.size();
    }

    /**
     * Merges cells from the other chain into this one.
     *
     * @param other the other chain
     */
    public void merge(GoCellChain<C> other) {
        assert cell.equals(other.getCell());
        positions.addAll(other.getPositions());
        liberties += other.getLiberties();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof GoCellChain<?> otherChain)) return false;

        return cell.equals(otherChain.getCell()) && positions.equals(otherChain.getPositions());
    }
}
