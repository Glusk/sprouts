package com.github.glusk2.sprouts.core.comb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.IsPointOnLineSegment;

/**
 * A SubmoveTransformation is a GraphTransformation that creates and returns
 * a new state after applying the {@code submove} to the {@code currentState}.
 * <p>
 * A Submove is viewed as a CompoundEdge by this object.
 */
public final class SubmoveTransformation implements GraphTransformation {
    /**
     * Maximum error margin for detection of intersection between a line
     * segment and a point.
     */
    private static final float LINE_INTERSECT_ERROR = .5f;

    /**
     * A CompoundEdge view of the Submove to apply to the
     * {@code currentState}.
     */
    private final CompoundEdge submove;
    /** The combinatorial state to apply the {@code submove} to. */
    private final Graph currentState;

    /**
     * Creates a new SubmoveTransformation by specifying the {@code submove}
     * and {@code currentState}.
     *
     * @param submove a CompoundEdge view of the Submove to apply to the
     *                {@code currentState}
     * @param currentState the combinatorial state to apply the
     *                     {@code submove} to
     */
    public SubmoveTransformation(
        final CompoundEdge submove,
        final Graph currentState
    ) {
        this.submove = submove;
        this.currentState = currentState;
    }

    /**
     * Creates and returns a new, simplified Graph by applying the
     * {@code submove} to the {@code currentState}.
     * <p>
     * If a {@code submove} crosses the cobweb, that connection has to be
     * split in two.
     * <p>
     * The Graph returned will not contain opposite cobweb edges in separate
     * faces.
     *
     * @return a new, simplified Graph after applying the {@code submove} to
     *         the {@code currentState}
     */
    @Override
    public Graph transformed() {
        Graph result = currentState;
        DirectedEdge currentDirection = submove.direction();
        Vertex tip = currentDirection.to();
        if (tip.color().equals(Color.RED)) {
            for (CompoundEdge edge : currentState.edges()) {
                if (edge.direction().color().equals(Color.RED)) {
                    Vector2 p0 = edge.origin().position();
                    Vector2 p1 = edge.direction().to().position();
                    Vector2 point = tip.position();
                    if (
                        new IsPointOnLineSegment(
                            p0,
                            p1,
                            point,
                            LINE_INTERSECT_ERROR
                        ).check()
                    ) {
                        CompoundEdge twin = new ReversedCompoundEdge(edge);
                        CompoundEdge fromRed =
                            new CompoundEdge.Wrapped(
                                edge.origin(),
                                new StraightLineEdge(tip)
                            );
                        CompoundEdge fromRedRev =
                            new ReversedCompoundEdge(fromRed);
                        CompoundEdge redTo =
                            new CompoundEdge.Wrapped(
                                tip,
                                new StraightLineEdge(edge.direction().to())
                            );
                        CompoundEdge redToRev = new ReversedCompoundEdge(redTo);
                        result = result
                            .without(edge.origin(), edge.direction())
                            .without(twin.origin(), twin.direction())
                            .with(fromRed.origin(), fromRed.direction())
                            .with(fromRedRev.origin(), fromRedRev.direction())
                            .with(redTo.origin(), redTo.direction())
                            .with(redToRev.origin(), redToRev.direction());
                        break;
                    }
                }
            }
        }
        CompoundEdge reversed =
            new ReversedCompoundEdge(submove.origin(), currentDirection);
        return
            result.with(submove.origin(), currentDirection)
                  .with(reversed.origin(), reversed.direction())
                  .simplified();
    }
}
