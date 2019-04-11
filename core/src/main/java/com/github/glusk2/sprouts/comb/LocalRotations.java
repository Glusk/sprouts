package com.github.glusk2.sprouts.comb;

import java.util.List;

/**
 * Represents a graph Vertex - {@code v} and a list of DirectedEdges
 * {@code (a, b)}. All DirectedEdges are virtually connected to the center
 * Vertex (virtual DirectedEdges {@code (v, a)}).
 * <p>
 * Virtual DirectedEdges {@code (v, a)} are ordered by the clockwise order of
 * {@code a}s around {@code v}. The order of virtual DirectedEdges is used to
 * establish {@code edges()}, which returns pairs {@code (v, b)}
 * <p>
 * Next DirectedEdge of {@code (a, b)} is a DirectedEdge {@code (c, d)}.
 * {@code c} comes strictly after {@code a} in the clockwise order of virtual
 * DirectedEdges - {@code (v, a) < (v, c)}. Method {@code next()} is defined
 * as: {@code (v, d) = next(a, b)}
 * <p>
 * <h3>Definitions:</h3>
 * <pre>
 * v ... the center vertex of this LocalRotations
 * a ... the source vertex of a DirectedEdge; virtually connected to v
 * b ... the destination vertex of a DirectedEdge; virtually connected to v
 * c ... the source vertex of the first DirectedEdge after (a, b); virtually
 *       connected to v
 * d ... the destination vertex of the first DirectedEdge after (a, b);
 *       virtually connected to v
 * </pre>
 */
public interface LocalRotations {
    /**
     * Returns DirectedEdges {@code (v, b)}.
     * <p>
     * Refer to <strong>Definitions</strong> in the interface definition Doc
     * for more info.
     *
     * @return a list of DirectedEdges {@code (v, b)}
     */
    List<DirectedEdge> edges();

    /**
     * Returns the first DirectedEdge after {@code current} in {@code this}
     * LocalRotations.
     * <p>
     * {@code current} need not be a part of {@code this} LocalRotations.
     * <p>
     * Refer to <strong>Definitions</strong> in the interface definition Doc
     * for more info.
     *
     * @param current DirectedEdge {@code (a, b)}
     * @return a new DirectedEdge {@code (v, d)}
     */
    DirectedEdge next(DirectedEdge current);

    /**
     * Returns new LocalRotations with an {@code additionalEdge}.
     * <p>
     * Refer to <strong>Definitions</strong> in the interface definition Doc
     * for more info.
     *
     * @param additionalEdge the DirectedEdge {@code (a, b)} to add
     * @return new LocalRotations with an {@code additionalEdge}
     */
    LocalRotations with(DirectedEdge additionalEdge);

    /**
     * Returns new LocalRotations without the {@code surplusEdge}.
     * <p>
     * Refer to <strong>Definitions</strong> in the interface definition Doc
     * for more info.
     *
     * @param surplusEdge the DirectedEdge {@code (a, b)} to remove
     * @return new LocalRotations without the {@code surplusEdge}
     */
    LocalRotations without(DirectedEdge surplusEdge);
}
