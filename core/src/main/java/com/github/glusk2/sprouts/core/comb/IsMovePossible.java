package com.github.glusk2.sprouts.core.comb;

import java.util.Set;

import com.github.glusk2.sprouts.core.util.Check;

/** A check that tests whether a Move is possible in {@code gameState}. */
public final class IsMovePossible implements Check {

    /** The graph to check for valid Moves. */
    private final SproutsGameState gameState;

    /**
     * Creates a new Check by specifying the {@code gameState}.
     *
     * @param gameState the graph to check for valid Moves
     */
    public IsMovePossible(final SproutsGameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean check() {
        Set<Set<SproutsEdge>> faces =
            new SproutsFaces(gameState.edges()).faces();
        for (Set<SproutsEdge> face : faces) {
            if (new IsSubmovePossibleInFace(gameState, face).check()) {
                return true;
            }
        }
        return false;
    }
}
