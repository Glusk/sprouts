package com.github.glusk2.sprouts.comb;

/**
 * This interface represents any type of Graph transformation, such as adding
 * a Submove to a Graph.
 */
public interface GraphTransformation {
    /**
     * Creates and returns a new Graph after applying {@code this}
     * GraphTransformation to the existing Graph.
     *
     * @return a new Graph after {@code this} GraphTransformation has been
     *         applied to the existing Graph
     */
    Graph transformed();
}
