package com.github.glusk2.sprouts;

import java.util.Iterator;

import com.github.glusk2.sprouts.comb.DirectedEdge;
import com.github.glusk2.sprouts.comb.Graph;
import com.github.glusk2.sprouts.comb.Vertex;

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
     * Returns the origin of {@code this} Submove.
     * <p>
     * The origin is a graph Vertex in which {@code this} Submove was started
     *
     * @return the origin of {@code this} Submove
     */
     Vertex origin();

     /**
     * Determines the direction of {@code this} Submove so that it can be
     * determined in which face of a Graph it is being drawn.
     *
     * @return the direction of {@code this} Submove
     */
    DirectedEdge direction();

    /**
     * Returns a new state after {@code this} Move is drawn.
     *
     * @return a new state after {@code this} Move is drawn
     */
    Graph updatedState();

    /**
     * Checks if {@code this} Submove is completed.
     * <p>
     * A Submove is completed if it intersects a cobweb or
     * {@code direction().to()} connects to a Graph Vertex.
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
