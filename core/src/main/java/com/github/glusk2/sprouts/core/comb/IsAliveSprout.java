package com.github.glusk2.sprouts.core.comb;

import java.util.function.Predicate;

import com.badlogic.gdx.graphics.Color;

/**
 * A predicate that checks whether a {@code vertex} is a sprout that is
 * connected to less than 3 sub-move edges in a game state.
 */
public final class IsAliveSprout implements Predicate<Vertex> {
    /** Default sprouts color. */
    private static final Color SPROUT_COLOR = Color.BLACK;
    /** Default submove color. */
    private static final Color SUB_MOVE_COLOR = SPROUT_COLOR;
    @SuppressWarnings("checkstyle:javadocvariable")
    private static final int DEAD_SPROUT_DEGREE = 3;

    /** A graph that represents a game state. */
    private SproutsGameState gameState;

    /**
     * Create a new <em>IsAliveSprout(vertex)</em> predicate.
     * <p>
     * A {@code vertex} is a living sprout if it is connected to at most 3
     * sub-move edges in {@code gameState}.
     *
     * @param gameState a graph that represents a game state
      */
    public IsAliveSprout(
        final SproutsGameState gameState
    ) {
        this.gameState = gameState;
    }

    @Override
    public boolean test(final Vertex vertex) {
         // Check if vertex is a sprout
         if (!vertex.color().equals(SPROUT_COLOR)) {
            return false;
        }
        // Check if the sprout is alive
        return
            new VertexDegree(
                vertex,
                this.gameState,
                SUB_MOVE_COLOR
            ).intValue() < DEAD_SPROUT_DEGREE;
    }
}
