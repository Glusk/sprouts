package com.github.glusk2.sprouts.core.comb;

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
     *     {@literal new TreeSet<DirectedEdge>(}
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
    public List<CompoundEdge> edges() {
        List<CompoundEdge> result = new ArrayList<CompoundEdge>();
        for (DirectedEdge e : edges) {
            result.add(new CachedCompoundEdge(center, e));
        }
        return result;
    }

    @Override
    public CompoundEdge next(final DirectedEdge current) {
        CompoundEdge[] withCurrent =
            this.with(current)
                .edges()
                .toArray(new CompoundEdge[0]);

        for (int i = 0; i < withCurrent.length; i++) {
            if (
                edges.comparator()
                     .compare(current, withCurrent[i].direction()) == 0
            ) {
                return withCurrent[(i + 1) % withCurrent.length];
            }
        }
        throw new AssertionError("Programming error!");
    }

    /**
     * Creates and returns a copy of {@code this.edges}.
     * <p>
     * The copy uses the same Comparator as {@code this.edges}.
     *
     * @return a new SortedSet, containing all the elements from
     *         {@code this.edges}
     */
    private SortedSet<DirectedEdge> cloneEdges() {
        SortedSet<DirectedEdge> copy =
            new TreeSet<DirectedEdge>(edges.comparator());
        for (DirectedEdge edge : edges) {
            copy.add(edge);
        }
        return copy;
    }

    @Override
    public LocalRotations with(final DirectedEdge... additionalEdges) {
        SortedSet<DirectedEdge> copy = cloneEdges();
        for (DirectedEdge additionalEdge : additionalEdges) {
            copy.add(additionalEdge);
        }
        return new PresetRotations(center, copy);
    }

    @Override
    public LocalRotations without(final DirectedEdge... surplusEdges) {
        SortedSet<DirectedEdge> copy = cloneEdges();
        for (DirectedEdge surplusEdge : surplusEdges) {
            copy.remove(surplusEdge);
        }
        return new PresetRotations(center, copy);
    }
}
