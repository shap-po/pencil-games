package com.github.shap_po.pencilgames.common.util;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Utility class with predefined directions
 */
public final class Direction {
    public static final Position TOP = Position.of(0, -1);
    public static final Position BOTTOM = Position.of(0, 1);
    public static final Position LEFT = Position.of(-1, 0);
    public static final Position RIGHT = Position.of(1, 0);

    public static final Position TOP_LEFT = Position.of(-1, -1);
    public static final Position TOP_RIGHT = Position.of(1, -1);
    public static final Position BOTTOM_LEFT = Position.of(-1, 1);
    public static final Position BOTTOM_RIGHT = Position.of(1, 1);

    public static final Position CENTER = Position.of(0, 0);

    /**
     * Four cardinal directions: {@link #TOP}, {@link #BOTTOM}, {@link #LEFT}, {@link #RIGHT}
     */
    public static final Set<Position> ADJACENT = Set.of(TOP, BOTTOM, LEFT, RIGHT);
    /**
     * Four diagonal directions: {@link #TOP_LEFT}, {@link #TOP_RIGHT}, {@link #BOTTOM_LEFT}, {@link #BOTTOM_RIGHT}
     */
    public static final Set<Position> DIAGONAL = Set.of(TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT);

    /**
     * Eight surrounding directions: {@link #ADJACENT}, {@link #DIAGONAL}
     */
    public static final Set<Position> SURROUNDING = Sets.union(ADJACENT, DIAGONAL);

    /**
     * Nine directions: {@link #SURROUNDING}, {@link #CENTER}
     */
    public static final Set<Position> ALL = Sets.union(SURROUNDING, Set.of(CENTER));
}
