package com.github.glusk2.sprouts.core.comb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.Move;
import com.github.glusk2.sprouts.core.Submove;
import com.github.glusk2.sprouts.core.geom.Polyline;

/**
 * Sprouts state after a Move.
 * <p>
 * After a Move has been completed, one has to check whether there
 * are any red (cobweb) points with no red (cobweb) edges. If so,
 * remove them.
 */
public final class SproutsStateAfterMove implements SproutsGameState {
    private final SproutsGameState previousState;
    private final Move move;

    public SproutsStateAfterMove(
        final SproutsGameState previousState,
        final Move move
    ) {
        this.previousState = previousState;
        this.move = move;
    }

    private Set<SproutsEdge> cachedEdges = null;
    @Override
    public Set<SproutsEdge> edges() {
        if (cachedEdges != null) {
            return cachedEdges;
        }
        SproutsGameState stateAfterSubmoves = previousState;
        Iterator<Submove> it = move.iterator();
            while (it.hasNext()) {
                Submove submove = it.next();
                stateAfterSubmoves =
                    new SproutsStateAfterSubmove(stateAfterSubmoves, submove);
                it = submove;
            }

        SproutsGameState stateAfterMove = stateAfterSubmoves;

        final SproutsGameState tmp = stateAfterMove;
        List<Vertex> verticesToRemove = stateAfterSubmoves.vertices().stream()
            .filter(v ->
                v.color().equals(Color.RED)
            && new VertexDegree(v, tmp, Color.RED).intValue() == 0
                // ==> this cobweb vertex must be "on" the current move
                //     and its black degree is equal to 2
            )
            .collect(Collectors.toList());

        for (Vertex vertexToRemove : verticesToRemove) {
            SproutsEdge firstHalf = stateAfterMove.edges().stream().filter(
                e -> e.isPositive() && vertexToRemove.equals(e.to())
            ).findFirst().get();
            SproutsEdge secondHalf = stateAfterMove.edges().stream().filter(
                e -> e.isPositive() && vertexToRemove.equals(e.from())
            ).findFirst().get();
    
            Set<SproutsEdge> simplifiedEdges =
                new HashSet<>(stateAfterMove.edges());

            List<Vector2> p1 = firstHalf.polyline().points();
            List<Vector2> p2 = secondHalf.intersectionPolyline().points();
            Vector2 p3 = secondHalf.to().position();

            List<Vector2> mergedPolyline = new ArrayList<>();
            mergedPolyline.addAll(p1);
            mergedPolyline.addAll(p2);
            mergedPolyline.add(p3);

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

            stateAfterMove = () -> simplifiedEdges;
        }
        cachedEdges = Collections.unmodifiableSet(stateAfterMove.edges());
        return cachedEdges;
    }
}
