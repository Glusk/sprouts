package com.github.glusk2.sprouts.core.comb;

import com.badlogic.gdx.graphics.Color;
import com.github.glusk2.sprouts.core.util.Check;

/**
 * Checks whether {@code vertex} is a sprout that is connected to less than 3
 * sub-move edges.
 */
public final class IsAliveSprout implements Check {
    private static final Color SPROUT_COLOR = Color.BLACK;
    private static final Color SUB_MOVE_COLOR = Color.BLACK;
    private static final int DEAD_SPROUT_DEGREE = 3;

    private Vertex vertex;
    private SproutsGameState gameState;

    public IsAliveSprout(Vertex vertex, SproutsGameState gameState) {
        this.vertex = vertex;
        this.gameState = gameState;
    }

    @Override
    public boolean check() {
        // Check if vertex is a sprout
        if (!vertex.color().equals(SPROUT_COLOR)) {
            return false;
        }
        // Check if the sprout is alive
        return
            new VertexDegree(
                vertex,
                gameState,
                SUB_MOVE_COLOR
            ).intValue() < DEAD_SPROUT_DEGREE;
    }
}
