package com.github.glusk2.sprouts.core.comb;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Only edges with the same origin (from()) shall be valid as constructor
 * arguments. Otherwise, invocations of {@code next()} will error with
 * {@code IllegalArgumentException}.
 */
public final class SproutsRotations {

    private SortedSet<SproutsEdge> edges;

    public SproutsRotations(SproutsEdge... edges) {
        this(new TreeSet<>(Arrays.asList(edges)));
    }
    public SproutsRotations(SortedSet<SproutsEdge> edges) {
        this.edges = edges;
    }

    public SproutsEdge next(SproutsEdge current) {
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
