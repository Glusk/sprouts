package com.github.glusk2.sprouts.core.comb;

import java.util.List;
import java.util.Set;

import com.github.glusk2.sprouts.core.util.Check;

/** A check that tests whether a Move is possible in {@code gameState}. */
public final class IsMovePossible implements Check {

    /** The Graph to check for valid Moves. */
    private final Graph gameState;

    /**
     * Creates a new Check by specifying the {@code gameState}.
     *
     * @param gameState the Graph to check for valid Moves
     */
    public IsMovePossible(final Graph gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean check() {
        List<Set<CompoundEdge>> faces = gameState.faces();
        for (Set<CompoundEdge> face : faces) {
            if (new IsSubmovePossibleInFace(gameState, face).check()) {
                return true;
            }
        }
        return false;
    }
}
