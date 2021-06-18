package com.github.glusk2.sprouts.core.moves;

import java.util.Iterator;

import com.github.glusk2.sprouts.core.comb.SproutsEdge;

/**
 * Represents a part of the player's move drawn on the screen.
 * <p>
 * Every Submove starts and ends in a Graph vertex (cobweb Vertex or a sprout)
 * of the current combinatorial representation.
 * <p>
 * A Submove is dynamic in a sense that it may not yet be completed as the
 * player draws it. Method {@code isCompleted()} tests for that.
 */
public interface Submove extends Iterator<Submove> {
    /**
     * Represents {@code this} Submove as a directed edge in a graph and
     * returns it.
     *
     * @return {@code this} Submove as a directed edge
     */
    SproutsEdge asEdge();

    /**
     * Checks if {@code this} Submove is completed.
     * <p>
     * A Submove is completed if it intersects a cobweb or
     * {@code asEdge().to()} connects to a sprout.
     *
     * @return {@code true} if {@code this} Submove is completed
     */
    boolean isCompleted();

    /**
     * Checks if {@code this} Submove can be rendered.
     *
     * @return {@code true} if enough of {@code this} Submove has already
     *         been drawn that it can be rendered
     */
    boolean isReadyToRender();
    /**
     * Checks if {@code this} Submove is valid.
     * <p>
     * A Submove is valid if:
     * <ul>
     *   <li>it doesn't cross itself</li>
     *   <li>it doesn't cross any of the already drawn moves</li>
     *   <li>it is not connected to an already dead sprout</li>
     * </ul>
     *
     * @return {@code true} if {@code this} Submove is valid
     */
    boolean isValid();
}
