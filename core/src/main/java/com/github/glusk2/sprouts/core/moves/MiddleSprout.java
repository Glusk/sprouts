package com.github.glusk2.sprouts.core.moves;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.comb.SproutsEdge;

/**
 * A middle sprout.
 * <p>
 * Iterate all submoves of a given {@code move}. Find the first point on a
 * submove that is at most {@code vertexGlueRadius} away from
 * {@code middleSproutPosition}. This is the middle sprout! Store the submove
 * and the index of the point on the polyine.
 * <p>
 * We will not allow the middle sprout to be within the
 * {@code 2 * vertexGlueRadius} radius of any submove endpoint.
 */
public final class MiddleSprout {
    /**
     * This is the point where <em>player</em> wants to place a sprout.
     */
    private final Vector2 middleSproutPosition;
    /** This is the move on which a sprout is to be placed. */
    private final Move move;
    /**
     * The acceptable margin of error by which
     * {@code middleSproutPosition} can be placed off the {@code move}. */
    private final float vertexGlueRadius;

    /**
     * A variable that holds the result set in {@link #findMiddleSprout()}
     * and returns it as {@link #submove()}.
     */
    private SproutsEdge edgeToSplit;
    /**
     * A variable that holds the result set in {@link #findMiddleSprout()}
     * and returns it as {@link #submovePolylineIndex()}.
     */
    private int splitIndex = -1;

    /**
     * Creates a new middle sprout.
     *
     * @param move this is the move on which a sprout is to be placed
     * @param middleSproutPosition this is the point where <em>player</em>
     *                             wants to place a sprout
     * @param vertexGlueRadius the acceptable margin of error by which
     * {@code middleSproutPosition} can be placed off the {@code move}
     */
    public MiddleSprout(
        final Move move,
        final Vector2 middleSproutPosition,
        final float vertexGlueRadius
    ) {
        this.middleSproutPosition = middleSproutPosition;
        this.move = move;
        this.vertexGlueRadius = vertexGlueRadius;
    }

    /**
     * Tries to find point closest to {@code middleSproutPosition} on the
     * {@code move}.
     */
    private void findMiddleSprout() {
        Iterator<Submove> it = move.iterator();
        while (it.hasNext()) {
            Submove submove = it.next();
            List<Vector2> points = submove.asEdge().polyline().points();
            for (int i = 0; i < points.size(); i++) {
                if (
                    points.get(i).dst(middleSproutPosition) <= vertexGlueRadius
                ) {
                    // It would be nice to have some tests written for this
                    // piece of code.
                    //---------------------------------------------------------
                    Vector2 submoveStart = submove.asEdge().from().position();
                    Vector2 submoveEnd = submove.asEdge().to().position();
                    while (
                        i < points.size()
                     && (
                        submoveStart.dst(points.get(i)) <= 2 * vertexGlueRadius
                     || submoveEnd.dst(points.get(i)) <= 2 * vertexGlueRadius
                        )
                    ) {
                        i++;
                    }
                    if (i == points.size()) {
                        continue;
                    }
                    //---------------------------------------------------------
                    edgeToSplit = submove.asEdge();
                    splitIndex = i;
                    return;
                }
            }
            it = submove;
        }
    }

    /**
     * The submove on which this middle sprout is being placed.
     *
     * @return The submove returned as edge. Can be {@code null}
     *         if player placed the middle sprout
     *         on an invalid location.
     */
    public SproutsEdge submove() {
        findMiddleSprout();
        return edgeToSplit;
    }

    /**
     * The index of an element in: {@code this.submove().polyline().points()}
     * that represents this middle sprout's position.
     *
     * @return A non-negative integer that represents a list index. If the
     *         index returned is negative, player placed the middle sprout
     *         on an invalid location.
     */
    public int submovePolylineIndex() {
        findMiddleSprout();
        return splitIndex;
    }
}
