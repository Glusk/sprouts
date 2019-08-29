package com.github.glusk2.sprouts.comb;

/**
 * Objects that implement this interface represent operations that search for a
 * certain Vertex.
 * <p>
 * Such operations may include but are not limited to:
 * <ul>
 *   <li>finding intersections</li>
 *   <li>finding the closest Vertex from a set of Vertices to a selected
 *       Vertex</li>
 * </ul>
 */
public interface VertexSearch {
    /**
     * Returns the Vertex found by {@code this} VertexSearch.
     *
     * @return the result of {@code this} VertexSearch
     */
    Vertex result();
}
