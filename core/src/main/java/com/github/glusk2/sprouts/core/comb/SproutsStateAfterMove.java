package com.github.glusk2.sprouts.core.comb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;
import com.github.glusk2.sprouts.core.moves.MiddleSprout;
import com.github.glusk2.sprouts.core.moves.Move;
import com.github.glusk2.sprouts.core.moves.Submove;

/**
 * Sprouts state after a Move.
 * <p>
 * Once a move is completed, it has to be added to the game state along with
 * the middle sprout.
 * <p>
 * Afterwards, one has to check whether there
 * are any red (cobweb) points with no red (cobweb) edges. If so,
 * remove them.
 */
public final class SproutsStateAfterMove implements SproutsGameState {
    /** The state before {@code this} one. */
    private final SproutsGameState previousState;
    /** The move to draw in {@code previousState}. */
    private final Move move;
    /** The middle sprout to place on the {@code move}. */
    private final MiddleSprout middleSprout;

    /** A cached value of {@link #edges()}. */
    private Set<SproutsEdge> cachedEdges = null;

    /**
     * Creates a new Sprouts state after a Move.
     *
     * @param previousState the state before {@code this} one
     * @param move the move to draw in {@code previousState}
     * @param middleSproutPosition the position of the sprout that should be
     * placed on the new {@code move}
     * @param vertexGlueRadius the acceptable margin of error by which
     * {@code middleSproutPosition} can be placed off the {@code move}
     */
    public SproutsStateAfterMove(
        final SproutsGameState previousState,
        final Move move,
        final Vector2 middleSproutPosition,
        final float vertexGlueRadius
    ) {
        this(
            previousState,
            move,
            new MiddleSprout(move, middleSproutPosition, vertexGlueRadius)
        );
    }

    /**
     * Creates a new Sprouts state after a Move.
     *
     * @param previousState the state before {@code this} one
     * @param move the move to draw in {@code previousState}
     * @param middleSprout the middle sprout to place on the {@code move}
     */
    public SproutsStateAfterMove(
        final SproutsGameState previousState,
        final Move move,
        final MiddleSprout middleSprout
    ) {
        this.previousState = previousState;
        this.move = move;
        this.middleSprout = middleSprout;
    }

    @Override
    public Set<SproutsEdge> edges() {
        if (cachedEdges != null) {
            return cachedEdges;
        }

        // 1. Iterate submoves to get the state after all submoves
        SproutsGameState stateAfterSubmoves = previousState;
        Iterator<Submove> it = move.iterator();
        while (it.hasNext()) {
            Submove submove = it.next();
            stateAfterSubmoves =
                new SproutsStateAfterSubmove(stateAfterSubmoves, submove);
            it = submove;
        }

        // 2. split the edge
        SproutsGameState stateAfterMiddleSprout =
            new SproutsStateAfterMiddleSprout(
                previousState,
                stateAfterSubmoves,
                middleSprout
            );

        // 3. Remove red points
        final SproutsGameState tmp = stateAfterMiddleSprout;
        List<Vertex> verticesToRemove = stateAfterSubmoves.vertices().stream()
            .filter(v ->
                v.color().equals(Color.RED)
            && new VertexDegree(v, tmp, Color.RED).intValue() == 0
                // ==> this cobweb vertex must be "on" the current move
                //     and its black degree is equal to 2
            )
            .collect(Collectors.toList());

        for (Vertex vertexToRemove : verticesToRemove) {
            SproutsEdge firstHalf = stateAfterMiddleSprout.edges().stream()
                .filter(
                    e -> e.isPositive() && vertexToRemove.equals(e.to())
                ).findFirst().get();
            SproutsEdge secondHalf = stateAfterMiddleSprout.edges().stream()
                .filter(
                    e -> e.isPositive() && vertexToRemove.equals(e.from())
                ).findFirst().get();

            Set<SproutsEdge> simplifiedEdges =
                new HashSet<>(stateAfterMiddleSprout.edges());

            List<Vector2> p1 = firstHalf.polyline().points();
            List<Vector2> p2 = secondHalf.polyline().points();

            List<Vector2> mergedPolyline = new ArrayList<>();
            mergedPolyline.addAll(p1);
            mergedPolyline.addAll(p2.subList(1, p2.size()));

            SproutsEdge merged =
                new SproutsEdge(
                    true,
                    new Polyline.WrappedList(mergedPolyline),
                    firstHalf.from().color(), secondHalf.to().color()
                );
            simplifiedEdges.remove(firstHalf);
            simplifiedEdges.remove(firstHalf.reversed());
            simplifiedEdges.remove(secondHalf);
            simplifiedEdges.remove(secondHalf.reversed());
            simplifiedEdges.add(merged);
            simplifiedEdges.add(merged.reversed());

            stateAfterMiddleSprout = () -> simplifiedEdges;
        }
        cachedEdges =
            Collections.unmodifiableSet(stateAfterMiddleSprout.edges());
        return cachedEdges;
    }
}
