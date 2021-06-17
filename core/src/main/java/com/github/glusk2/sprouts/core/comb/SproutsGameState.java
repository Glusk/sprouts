package com.github.glusk2.sprouts.core.comb;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.github.glusk2.sprouts.core.geom.PolylineBatch;

/** The graph representation of a sprouts game position. */
@FunctionalInterface
public interface SproutsGameState {
    /**
     * Returns the cobweb and submove edges of {@code this} game state.
     *
     * @return cobweb and submove edges of {@code this} game state as a set
     */
    Set<SproutsEdge> edges();

    /**
     * Returns the cobweb vertices and sprouts for {@code this}
     * game state.
     *
     * @return cobweb vertices and sprouts of {@code this} game state as a set
     */
    default Set<Vertex> vertices() {
        Set<Vertex> vertices = new HashSet<>();
        for (SproutsEdge edge : edges()) {
            vertices.add(edge.from());
            vertices.add(edge.to());
        }
        return vertices;
    }

    /**
     * Renders {@code this} game state.
     * <p>
     * Edges are rendered as polyline points (circles) with straight line
     * segments connecting them.
     *
     * @param renderer the renderer to render with
     * @param thickness the thickness of edges and the radius of vertices
     * @param circleSegmentCount the number of segments for the circles drawn
     * @param displayCobweb if {@code true}, display cobweb vertices and edges
     */
    default void render(
        ShapeRenderer renderer,
        float thickness,
        int circleSegmentCount,
        boolean displayCobweb
    ) {
        Set<SproutsEdge> drawnEdges = new HashSet<>();
        renderer.begin(ShapeType.Filled);
        for (SproutsEdge edge : edges()) {
            if (!displayCobweb && edge.color().equals(Color.RED)) {
                continue;
            }
            if (!drawnEdges.contains(edge)) {
                new PolylineBatch(
                    edge.polyline(),
                    edge.color(),
                    thickness,
                    circleSegmentCount,
                    true
                ).render(renderer);
                drawnEdges.add(edge);
                drawnEdges.add(edge.reversed());
            }
        }
        for (Vertex v : vertices()) {
            if (!displayCobweb && v.color().equals(Color.RED)) {
                continue;
            }
            renderer.setColor(v.color());
            renderer.circle(
                v.position().x,
                v.position().y,
                thickness,
                circleSegmentCount
            );

            if (new IsAliveSprout(this).test(v)) {
                renderer.setColor(Color.WHITE);
            } else {
                renderer.setColor(Color.GRAY);
            }

            renderer.circle(
                v.position().x,
                v.position().y,
                thickness / 2,
                circleSegmentCount
            );

        }
        renderer.end();
    }
}
