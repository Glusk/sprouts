package com.github.glusk2.sprouts.comb;

import java.util.Iterator;

import com.github.glusk2.sprouts.Move;
import com.github.glusk2.sprouts.Submove;

/**
 * A MoveTransformation is a GraphTransformation that creates and returns
 * a new state after applying the {@code move} to the {@code currentState}.
 */
public final class MoveTransformation implements GraphTransformation {
    /** The Move to apply to the {@code currentState}. */
    private final Move move;
    /** The combinatorial state to apply the {@code move} to. */
    private final Graph currentState;

    /**
     * Creates a new MoveTransformation by specifying the {@code move}
     * and {@code currentState}.
     *
     * @param move the Move to apply to the {@code currentState}
     * @param currentState the combinatorial state to apply the
     *                     {@code move} to
     */
    public MoveTransformation(final Move move, final Graph currentState) {
        this.move = move;
        this.currentState = currentState;
    }

    @Override
    public Graph transformed() {
        Graph transformed = currentState;
        Iterator<Submove> it = move.iterator();
        while (it.hasNext()) {
            Submove submove = it.next();
            transformed =
                new SubmoveTransformation(
                    submove,
                    transformed
                ).transformed();
            it = submove;
        }
        return transformed;
    }
}
