package com.github.glusk2.sprouts.core.comb;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

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
        SproutsGameState simplified =
            new SproutsStateWithoutCobwebVertices(stateAfterMiddleSprout);

        cachedEdges = Collections.unmodifiableSet(simplified.edges());
        return cachedEdges;
    }
}
