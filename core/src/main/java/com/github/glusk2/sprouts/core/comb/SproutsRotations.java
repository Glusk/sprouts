package com.github.glusk2.sprouts.core.comb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Only edges with the same origin (from()) shall be valid as constructor
 * arguments.
 */
public final class SproutsRotations {

    private SortedSet<SproutsEdge> edges;

    public SproutsRotations(SproutsEdge... edges) {
        this(new TreeSet<>(Arrays.asList(edges)));
    }
    public SproutsRotations(SortedSet<SproutsEdge> edges) {
        Set<Vertex> origins = new HashSet<>();
        for (SproutsEdge e : edges) {
            origins.add(e.from());
        }
        if (origins.size() != 1) {
            throw new IllegalArgumentException(
                "The edge set is either empty or some of the edges "
              + "differ by their origin (from())."
            );
        }
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
