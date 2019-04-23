package com.github.glusk2.sprouts.comb;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/** A reference LocalRotations implementation. */
public final class PresetRotations implements LocalRotations {

    /** The center Vertex - {@code v} - of {@code this} LocalRotations. */
    private final Vertex center;
    /** A sorted set of DirectedEdges {@code (a, b)}. */
    private final SortedSet<DirectedEdge> edges;

    /**
     * Constructs a new LocalRotations object from the center Vertex and an
     * empty sorted set of edges.
     * <p>
     * Equivalent to:
     * <pre>
     * new PresetRotations(
     *     center,
     *     new TreeSet<DirectedEdge>(
     *         new ClockwiseComparator(center)
     *     )
     * )
     * </pre>
     *
     * @param center the center Vertex - {@code v}
     */
    public PresetRotations(final Vertex center) {
        this(
            center,
            new TreeSet<DirectedEdge>(
                new ClockwiseComparator(center)
            )
        );
    }

    /**
     * Constructs a new LocalRotations object from the {@code center} Vertex
     * and copy of {@code edges}.
     * <p>
     * Refer to {@link LocalRotations} interface definition for notes on
     * notation.
     *
     * @param center the center Vertex - {@code v}
     * @param edges a sorted set of DirectedEdges {@code (a, b)}
     */
    public PresetRotations(
        final Vertex center,
        final SortedSet<DirectedEdge> edges
    ) {
        this.center = center;
        this.edges = new TreeSet<DirectedEdge>(edges);
    }

    @Override
    public List<DirectedEdge> edges() {
        List<DirectedEdge> result = new ArrayList<DirectedEdge>();
        for (DirectedEdge e : edges) {
            result.add(new ExtendedEdge(false, center, e));
        }
        return result;
    }

    @Override
    public DirectedEdge next(final DirectedEdge current) {
        DirectedEdge[] withCurrent =
            this.with(current)
                .edges()
                .toArray(new DirectedEdge[0]);
        for (int i = 0; i < withCurrent.length; i++) {
            if (current.to().equals(withCurrent[i].to())) {
                return withCurrent[(i + 1) % withCurrent.length];
            }
        }
        throw new AssertionError("Programming error!");
    }

    @Override
    public DirectedEdge next(final Vertex current) {
        DirectedEdge[] withCurrent =
            this.edges()
                .toArray(new DirectedEdge[0]);
        for (int i = 0; i < withCurrent.length; i++) {
            if (current.equals(withCurrent[i].to())) {
                return withCurrent[(i + 1) % withCurrent.length];
            }
        }
        throw new IllegalStateException(
            "This LocalRotations object is empty! It has no edges."
        );
    }

    @Override
    public LocalRotations with(final DirectedEdge... additionalEdges) {
        SortedSet<DirectedEdge> copy = new TreeSet<DirectedEdge>(edges);
        for (DirectedEdge additionalEdge : additionalEdges) {
            copy.add(additionalEdge);
        }
        return new PresetRotations(center, copy);
    }

    @Override
    public LocalRotations without(final DirectedEdge... surplusEdges) {
        SortedSet<DirectedEdge> copy = new TreeSet<DirectedEdge>(edges);
        for (DirectedEdge surplusEdge : surplusEdges) {
            copy.remove(surplusEdge);
        }
        return new PresetRotations(center, copy);
    }
}
