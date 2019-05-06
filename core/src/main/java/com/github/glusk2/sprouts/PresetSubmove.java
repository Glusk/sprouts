package com.github.glusk2.sprouts;

import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.comb.CachedCompoundEdge;
import com.github.glusk2.sprouts.comb.CompoundEdge;
import com.github.glusk2.sprouts.comb.DirectedEdge;
import com.github.glusk2.sprouts.comb.ExtendedEdge;
import com.github.glusk2.sprouts.comb.Graph;
import com.github.glusk2.sprouts.comb.PresetVertex;
import com.github.glusk2.sprouts.comb.ReversedCompoundEdge;
import com.github.glusk2.sprouts.comb.StraightLineEdge;
import com.github.glusk2.sprouts.comb.Vertex;
import com.github.glusk2.sprouts.geom.Polyline;

/** The Submove reference implementation. */
public final class PresetSubmove implements Submove {

    /**
     * The minimal Submove length in segments at which we check for
     * auto-complete.
     */
    private static final int MIN_SUBMOVE_LENGTH = 5;
    /** The Graph Vertex in which {@code this} Submove begins. */
    private final Vertex origin;
    /** The polyline approximation of the move stroke. */
    private final Polyline stroke;
    /** The game state before {@code this} Submove. */
    private final Graph currentState;
    /**
     * The Vertex glue radius, used to auto-complete {@code this} Submove
     * when near a {@code currentState} Vertex.
     */
    private final float vertexGlueRadius;

    /** Completion flag. */
    private boolean isCompleted = false;

    /**
     * Creates a new Submove.
     *
     * @param origin the Graph Vertex in which {@code this} Submove begins
     * @param stroke the polyline approximation of the move stroke
     * @param currentState the game state before {@code this} Submove
     * @param vertexGlueRadius the Vertex glue radius, used to auto-complete
     *                         {@code this} Submove when near a
     *                         {@code currentState} Vertex
     */
    public PresetSubmove(
        final Vertex origin,
        final Polyline stroke,
        final Graph currentState,
        final float vertexGlueRadius
    ) {
        this.origin = origin;
        this.stroke = stroke;
        this.currentState = currentState;
        this.vertexGlueRadius = vertexGlueRadius;
    }

    @Override
    public Vertex origin() {
        return origin;
    }

    @Override
    public DirectedEdge direction() {
        List<Vector2> strokePoints = stroke.points();
        if (strokePoints.size() < 2) {
            throw
                new IllegalStateException(
                    "At least 2 sample points are needed to establish a "
                     + "direction!"
                );
        }

        DirectedEdge direction =
            new StraightLineEdge(
                Color.BLACK,
                new PresetVertex(
                    Color.BLACK,
                    strokePoints.get(1),
                    null
                )
            );
        for (int i = 2; i < strokePoints.size(); i++) {
            DirectedEdge updatedDirection =
                new ExtendedEdge(
                    new PresetVertex(
                        Color.BLACK,
                        strokePoints.get(i),
                        null
                    ),
                    direction
                );
            // Check if close to a Graph Vertex and finnish
            Vertex v = closestToTheEnd(updatedDirection);
            if (
                updatedDirection.polyline().points().size()
                > MIN_SUBMOVE_LENGTH
                &&
                v.position().dst(
                    updatedDirection.to().position()
                ) < vertexGlueRadius
            ) {
                isCompleted = true;
                return new ExtendedEdge(v, updatedDirection);
            }
            // Check if crosses cobweb
            Vector2 crossPoint =
                crossPoint(
                    currentState.edgeFace(
                        new CachedCompoundEdge(origin, direction)
                    ),
                    updatedDirection
                );
            if (crossPoint != null) {
                isCompleted = true;
                return
                    new ExtendedEdge(
                        new PresetVertex(
                            Color.RED,
                            crossPoint,
                            null
                        ),
                        direction
                    );
            }
            direction = updatedDirection;
        }
        return direction;
    }

    /**
     * Returns the intersection point between {@code move} and {@code face}.
     *
     * @param face a Graph face
     * @param move a DirectedEdge move direction
     * @return the intersection; can be null if {@code move} and {@code face}
     *         don't cross
     */
    private static Vector2 crossPoint(
        final Set<CompoundEdge> face,
        final DirectedEdge move
    ) {
        Vector2 intersection = new Vector2();
        List<Vector2> movePoints = move.polyline().points();
        for (CompoundEdge edge : face) {
            // Check only red links!
            if (edge.direction().color() != Color.RED) {
                continue;
            }
            List<Vector2> edgePoints =
                new ExtendedEdge(
                    edge.origin(),
                    edge.direction()
                ).polyline().points();
            // Don't start comparing for an intersection too close to the
            // origin
            for (int i = 1; i < movePoints.size(); i++) {
                for (int j = 1; j < edgePoints.size(); j++) {
                    boolean intersects =
                        Intersector.intersectSegments(
                            movePoints.get(i - 1),
                            movePoints.get(i),
                            edgePoints.get(j - 1),
                            edgePoints.get(j),
                            intersection
                        );
                    if (intersects) {
                        return intersection;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Finds and returns the Graph Vertex in {@code currentState} that is the
     * closest to {@code direction.to()}.
     *
     * @param direction the direction of a Submove
     * @return {@code currentState} Vertex closest to {@code direction.to()}
     */
    private Vertex closestToTheEnd(final DirectedEdge direction) {
        float minDistance = Float.MAX_VALUE;
        Vertex minVertex = null;
        for (Vertex v : currentState.vertices()) {
            float nextDistance = v.position().dst(direction.to().position());
            if (nextDistance < minDistance) {
                minDistance = nextDistance;
                minVertex = v;
            }
        }
        return minVertex;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public boolean isReadyToRender() {
        return stroke.points().size() >= 2;
    }

    @Override
    public boolean isValid() {
        // ToDo: add logic
        return true;
    }

    @Override
    public Graph updatedState() {
        if (!isCompleted()) {
            throw new IllegalStateException("Submove not yet completed!");
        }
        CompoundEdge reversed = new ReversedCompoundEdge(origin(), direction());
        return
            currentState.with(origin(), direction())
                        .with(reversed.origin(), reversed.direction())
                        .simplified();
    }
}
