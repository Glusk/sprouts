package com.github.glusk2.sprouts.core.comb;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Sprouts local Rotations.
 * <p>
 * Represents a local rotations object of edges with a common origin
 * ({@link SproutsEdge#from()}).
 * <p>
 * Only edges with the same origin shall be valid as constructor
 * arguments. Otherwise, invocations of {@link #next(SproutsEdge)} will error
 * with {@code IllegalArgumentException}.
 */
public final class SproutsRotations {
    /** Common origin edges. */
    private SortedSet<SproutsEdge> edges;

    /**
     * Creates local rotations from an array of {@code edges}.
     *
     * @param edges common origin edges
     */
    public SproutsRotations(final SproutsEdge... edges) {
        this(new TreeSet<>(Arrays.asList(edges)));
    }
    /**
     * Creates local rotations from an sorted set of {@code edges}.
     *
     * @param edges common origin edges
     */
    public SproutsRotations(final SortedSet<SproutsEdge> edges) {
        this.edges = edges;
    }

    /**
     * Returns the first SproutsEdge after {@code current} in this local
     * rotations object.
     * <p>
     * {@code current} need not be a part of this local rotations object,
     * but it must have the same origin
     * ({@link SproutsEdge#from()}).
     *
     * @param current an edge in that shares the same origin
     * ({@link SproutsEdge#from()}) with edges in {@code this} local rotations
     * @return the first edge after {@code current} in this local rotations
     *         object
     */
    public SproutsEdge next(final SproutsEdge current) {
        SortedSet<SproutsEdge> withCurrent = new TreeSet<>(edges);
        withCurrent.add(current);

        SproutsEdge[] rotations = withCurrent.toArray(new SproutsEdge[0]);
        for (int i = 0; i < rotations.length; i++) {
            if (rotations[i].equals(current)) {
                return rotations[(i + 1) % rotations.length];
            }
        }
        throw new AssertionError("Programming error!");
    }
}
