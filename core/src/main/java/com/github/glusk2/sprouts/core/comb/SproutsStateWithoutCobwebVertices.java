package com.github.glusk2.sprouts.core.comb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.geom.Polyline;

/**
 * Sprouts State Without redundant Cobweb Vertices.
 * <p>
 * A state that removes all RED vertices with the red degree of zero from
 * {@code previousState} and returns the result as a new state.
 * <p>
 * Every red vertex to remove will have a black degree of two. Effectively,
 * that means we create one edge out of two, joining them at the red vertex,
 * which gets removed.
 */
public final class SproutsStateWithoutCobwebVertices
    implements SproutsGameState {

    /** The state from which to remove the redundant cobweb vertices. */
    private final SproutsGameState previousState;

    /**
     * Creates a new state without redundant cobweb vertices by wrapping a
     * state that may contain such vertices.
     *
     * @param previousState the state from which to remove redundant cobweb
     *                      vertices
     */
    public SproutsStateWithoutCobwebVertices(
        final SproutsGameState previousState
    ) {
        this.previousState = previousState;
    }

    @Override
    public Set<SproutsEdge> edges() {
        List<Vertex> verticesToRemove = previousState.vertices().stream()
            .filter(v ->
                v.color().equals(Color.RED)
             && new VertexDegree(v, previousState, Color.RED).intValue() == 0
            )
            .collect(Collectors.toList());

        SproutsGameState result = previousState;
        for (Vertex vertexToRemove : verticesToRemove) {
            SproutsEdge firstHalf = result.edges().stream()
                .filter(
                    e -> e.isPositive() && vertexToRemove.equals(e.to())
                ).findFirst().get();
            SproutsEdge secondHalf = result.edges().stream()
                .filter(
                    e -> e.isPositive() && vertexToRemove.equals(e.from())
                ).findFirst().get();

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

            Set<SproutsEdge> simplifiedEdges = new HashSet<>(result.edges());
            simplifiedEdges.remove(firstHalf);
            simplifiedEdges.remove(firstHalf.reversed());
            simplifiedEdges.remove(secondHalf);
            simplifiedEdges.remove(secondHalf.reversed());
            simplifiedEdges.add(merged);
            simplifiedEdges.add(merged.reversed());

            result = () -> simplifiedEdges;
        }
        return result.edges();
    }
}
