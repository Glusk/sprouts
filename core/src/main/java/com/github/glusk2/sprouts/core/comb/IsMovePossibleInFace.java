package com.github.glusk2.sprouts.core.comb;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.github.glusk2.sprouts.core.util.Check;

/**
 * A check that tests whether a Move is possible in the {@code face} of
 * the {@code gameState}.
 */
public final class IsMovePossibleInFace implements Check {
    /** Maximum sprout lives. */
    private static final int MAXIMUM_SPROUT_LIVES = 3;
    /**
     * The minimum amount of total sprouts in a face that a valid move is
     * still possible.
     */
    private static final int MINIMUM_FACE_LIVES = 2;

    /** The Graph that the {@code face} belongs to. */
    private final Graph gameState;
    /** A Set of CompoundEdges that comprise a face of the {@code gameState}. */
    private final Set<CompoundEdge> face;

    /**
     * Creates a new Check by specifying the {@code gameState} and the
     * {@code face}.
     *
     * @param gameState the Graph that the {@code face} belongs to
     * @param face a Set of CompoundEdges that comprise a face of the
     *             {@code gameState}
     */
    public IsMovePossibleInFace(
        final Graph gameState,
        final Set<CompoundEdge> face
    ) {
        this.gameState = gameState;
        this.face = face;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if {@code face} is not part of the
     *                                  {@code gameState}
     */
    @Override
    public boolean check() {
        if (!gameState.faces().contains(face)) {
            throw new IllegalArgumentException(
                "\"face\" is not part of the \"gameState\"!"
            );
        }
        Set<Vertex> faceVertices = new HashSet<Vertex>();
        for (CompoundEdge edge : face) {
            faceVertices.add(edge.origin());
            faceVertices.add(edge.direction().to());
        }

        int faceLives = 0;
        for (Vertex v : faceVertices) {
            faceLives +=
                MAXIMUM_SPROUT_LIVES - gameState.vertexDegree(v, Color.BLACK);
        }
        return faceLives >= MINIMUM_FACE_LIVES;
    }
}
