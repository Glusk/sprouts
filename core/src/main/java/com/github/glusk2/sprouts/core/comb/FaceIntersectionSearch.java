package com.github.glusk2.sprouts.core.comb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

/**
 * Objects of this class can find the intersection between a line segment and
 * a Graph face.
 */
public final class FaceIntersectionSearch implements VertexSearch {

    /** A set of CompoundEdges that represent a Graph face. */
    private final Set<CompoundEdge> face;
    /** The first line segment boundary. */
    private final Vector2 p0;
    /** The second line segment boundary. */
    private final Vector2 p1;

    /**
     * Constructs a new VertexSearch object that can find the intersection
     * between the line segment {@code p0-p1} and {@code face}.
     *
     * @param face a set of CompoundEdges that represent a Graph face
     * @param p0 the first line segment boundary
     * @param p1 the second line segment boundary
     */
    public FaceIntersectionSearch(
        final Set<CompoundEdge> face,
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
     * Graph {@code face} edge that the line segment {@code p0-p1} crosses.
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
        for (CompoundEdge edge : face) {
            List<Vector2> edgePoints = new ArrayList<Vector2>();
            edgePoints.add(edge.origin().position());
            edgePoints.addAll(edge.direction().polyline().points());
            Color color = edge.direction().color();
            Vertex result =
                new PolylineIntersectionSearch(
                    p0,
                    p1,
                    new Polyline.WrappedList(edgePoints),
                    color
                ).result();
            if (result.color().equals(color)) {
                return result;
            }
        }
        return new VoidVertex();
    }
}
