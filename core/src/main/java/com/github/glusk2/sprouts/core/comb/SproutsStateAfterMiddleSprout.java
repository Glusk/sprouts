package com.github.glusk2.sprouts.core.comb;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;
import com.github.glusk2.sprouts.core.moves.MiddleSprout;

/**
 * Sprouts State After adding the Middle Sprout.
 * <p>
 * When a player adds the middle sprout, an edge that the sprout is placed upon
 * is split into two. More formally, the previous edge is removed and re-added
 * into the game state as two halves.
 */
public final class SproutsStateAfterMiddleSprout implements SproutsGameState {
    /** The state before a move is drawn. */
    private final SproutsGameState previousState;
    /** The state after the move is drawn. */
    private final SproutsGameState stateAfterMove;
    /** The middle sprout to add into {@code stateAfterMove}.*/
    private final MiddleSprout middleSprout;

    /**
     * Creates a new SproutsStateAfterMiddleSprout.
     *
     * @param previousState the state before a move is drawn
     * @param stateAfterMove the state after the move is drawn
     * @param middleSprout the middle sprout to add into {@code stateAfterMove}
     */
    public SproutsStateAfterMiddleSprout(
        final SproutsGameState previousState,
        final SproutsGameState stateAfterMove,
        final MiddleSprout middleSprout
    ) {
        this.previousState = previousState;
        this.stateAfterMove = stateAfterMove;
        this.middleSprout = middleSprout;
    }

    @Override
    public Set<SproutsEdge> edges() {
        SproutsEdge edgeToSplit = middleSprout.submove();
        int splitIndex = middleSprout.submovePolylineIndex();

        if (edgeToSplit != null) {
            List<Vector2> points = edgeToSplit.polyline().points();
            Set<SproutsEdge> edges = new HashSet<>(stateAfterMove.edges());
            edges.remove(edgeToSplit);
            edges.remove(edgeToSplit.reversed());

            // split edge
            SproutsEdge s1 = new SproutsEdge(
                true,
                new Polyline.WrappedList(
                    points.subList(0, splitIndex + 1)
                ),
                edgeToSplit.from().color(),
                Color.BLACK
            );
            SproutsEdge s2 = new SproutsEdge(
                true,
                new Polyline.WrappedList(
                    points.subList(splitIndex, points.size())
                ),
                Color.BLACK,
                edgeToSplit.to().color()
            );

            // add both ends to the graph
            edges.add(s1);
            edges.add(s2);
            // add both opposites to the graph
            edges.add(s1.reversed());
            edges.add(s2.reversed());

            return Collections.unmodifiableSet(edges);
        } else {
            return Collections.unmodifiableSet(previousState.edges());
        }
    }
}
