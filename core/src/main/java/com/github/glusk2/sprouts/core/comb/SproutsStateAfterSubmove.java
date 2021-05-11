package com.github.glusk2.sprouts.core.comb;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.github.glusk2.sprouts.core.Submove;
import com.github.glusk2.sprouts.core.geom.IsPointOnLineSegment;
import com.github.glusk2.sprouts.core.geom.Polyline;

/**
 * Sprouts state after a Submove.
 * <p>
 * A submove may tear the cobweb. When that happens, the cobweb edge has to be
 * split at the intersection and a cobweb vertex added to the game state.
 * <p>
 * If afterwards there's a cobweb edge that resides in two faces,
 * remove it.
 */
public final class SproutsStateAfterSubmove implements SproutsGameState {
    /**
     * Maximum error margin for detection of intersection between a line
     * segment and a point.
     */
    private static final float LINE_INTERSECT_ERROR = .5f;

    /** The state before {@code this} one. */
    private final SproutsGameState previousState;
    /** The submove to draw in {@code previousState}. */
    private final Submove submove;

    /** A cached value of {@link #edges()}. */
    private Set<SproutsEdge> edgeCache;

    /**
     * Creates a new Sprouts state after a Submove.
     *
     * @param previousState the state before {@code this} one
     * @param submove the submove to draw in {@code previousState}
     */
    public SproutsStateAfterSubmove(
        final SproutsGameState previousState,
        final Submove submove
    ) {
        this.previousState = previousState;
        this.submove = submove;
    }

    @Override
    public Set<SproutsEdge> edges() {
        if (edgeCache != null) {
            return edgeCache;
        }
        SproutsEdge submoveEdge = submove.asEdge();
        Set<SproutsEdge> updatedEdgeSet =
            new HashSet<SproutsEdge>(previousState.edges());

        Vertex tip = submoveEdge.to();
        if (tip.color().equals(Color.RED)) {
            Set<SproutsEdge> submoveFace =
                new SproutsFaces(
                    previousState.edges()
                ).drawnIn(submoveEdge);
            for (SproutsEdge edge : submoveFace) {
                if (edge.color().equals(Color.RED)) {
                    if (
                        new IsPointOnLineSegment(
                            edge.from().position(),
                            edge.to().position(),
                            tip.position(),
                            LINE_INTERSECT_ERROR
                        ).check()
                    ) {
                        SproutsEdge firstHalf =
                            new SproutsEdge(
                                new Polyline.WrappedList(
                                    edge.from().position(),
                                    tip.position()
                                ),
                                edge.from().color(),
                                tip.color()
                            );
                        SproutsEdge secondHalf =
                            new SproutsEdge(
                                new Polyline.WrappedList(
                                    tip.position(),
                                    edge.to().position()
                                ),
                                tip.color(),
                                edge.to().color()
                            );
                        updatedEdgeSet.remove(edge);
                        updatedEdgeSet.remove(edge.reversed());
                        updatedEdgeSet.add(firstHalf);
                        updatedEdgeSet.add(firstHalf.reversed());
                        updatedEdgeSet.add(secondHalf);
                        updatedEdgeSet.add(secondHalf.reversed());
                        break;
                    }
                }
            }
        }

        updatedEdgeSet.add(submoveEdge);
        updatedEdgeSet.add(submoveEdge.reversed());

        SproutsEdge redEdgeInTwoFaces =
            new SproutsFaces(
                updatedEdgeSet
            ).findFirstCobwebEdgeInTwoFaces(Color.RED);
        if (redEdgeInTwoFaces != null) {
            updatedEdgeSet.remove(redEdgeInTwoFaces);
            updatedEdgeSet.remove(redEdgeInTwoFaces.reversed());
        }
        edgeCache = Collections.unmodifiableSet(updatedEdgeSet);
        return edgeCache;
    }
}
