package com.github.glusk2.sprouts.comb;

import java.util.Set;

/**
 * A face of a planar graph.
 *
 * @see <a href="https://proofwiki.org/wiki/Definition:Planar_Graph/Face"></a>
 */
public final class PlanarFace implements Face {
    /**
     * A set of edges that make up the border of {@code this} face.
     */
    private final Set<Edge> edgeBoundary;

    /**
     * Constructs a new face from border edges.
     *
     * @param edgeBoundary a set of edges that make up the border of
     *                     {@code this}
     */
    public PlanarFace(final Set<Edge> edgeBoundary) {
        this.edgeBoundary = edgeBoundary;
    }

    /**
     * Getter for boundary edges.
     *
     * @return a set of edges that make up the border of {@code this}
     *         face.
     */
    @Override
    public Set<Edge> boundary() {
        return edgeBoundary;
    }

    /**
     * Equivalent to:
     * <pre>
     * return boundary().hashCode();
     * </pre>.
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return boundary().hashCode();
    }

    /**
     * Checks if boundaries equal.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof PlanarFace)) {
            return false;
        }
        PlanarFace that = (PlanarFace) obj;
        return this.boundary().equals(that.boundary());
    }
}
