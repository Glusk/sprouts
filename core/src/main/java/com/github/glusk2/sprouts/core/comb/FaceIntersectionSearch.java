package com.github.glusk2.sprouts.core.comb;

import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Objects of this class can find the intersection between a line segment and
 * a graph face.
 */
public final class FaceIntersectionSearch implements VertexSearch {

    /** A set of directed edges that represent a graph face. */
    private final Set<SproutsEdge> face;
    /** The first line segment boundary. */
    private final Vector2 p0;
    /** The second line segment boundary. */
    private final Vector2 p1;

    /**
     * Constructs a new VertexSearch object that can find the intersection
     * between the line segment {@code p0-p1} and {@code face}.
     *
     * @param face a set of directed edges that represent a graph face
     * @param p0 the first line segment boundary
     * @param p1 the second line segment boundary
     */
    public FaceIntersectionSearch(
        final Set<SproutsEdge> face,
        final Vector2 p0,
        final Vector2 p1
    ) {
        this.face = face;
        this.p0 = p0;
        this.p1 = p1;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result is the intersection point between line segment {@code p0-p1}
     * and {@code face}.
     * <p>
     * The {@code color()} of the Vertex returned is the same as that of the
     * graph {@code face} edge that the line segment {@code p0-p1} crosses.
     * <p>
     * It is not enough for the segment to touch the face - it has to cross it
     * or no intersection will be detected.
     *
     * @return the intersection Vertex between line segment {@code p0-p1} and
     *         {@code face}; if the intersection is not found, and new instance
     *         of {@link VoidVertex} is returned
     */
    @Override
    public Vertex result() {
        for (SproutsEdge edge : face) {
            Color color = edge.color();
            Vertex result =
                new PolylineIntersectionSearch(
                    p0,
                    p1,
                    edge.intersectionPolyline(),
                    color
                ).result();
            if (result.color().equals(color)) {
                return result;
            }
        }
        return new VoidVertex();
    }
}
