package com.github.glusk2.sprouts.core.comb;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.github.glusk2.sprouts.core.geom.PolylineBatch;

public interface SproutsGameState {
    Set<SproutsEdge> edges();
    // TODO: make this method default
    Set<Vertex> vertices();
    // TODO: make this method default
    void render(
        ShapeRenderer renderer,
        float lineThickness,
        int circleSegmentCount
    );
    // Should be replaced by a lambda
    final class Wrapped implements SproutsGameState {
        private Set<SproutsEdge> edges;

        public Wrapped(Set<SproutsEdge> edges) {
            this.edges = edges;
        }
    
        @Override
        public Set<SproutsEdge> edges() {
            return edges;
        }
    
        @Override
        public void render(
            ShapeRenderer renderer,
            float lineThickness,
            int circleSegmentCount
        ) {
            /*
            // YAGNI
            // looks smooth and resizes properly;
            // should probably be calculated at a higher level in the call stack
            lineThickness =  Math.min(
                gameBounds.getWidth(),
                gameBounds.getHeight()
            ) / 60f;
            circleSegmentCount = 20;
            */
            
            Set<SproutsEdge> drawnEdges = new HashSet<>();
            renderer.begin(ShapeType.Filled);
            for (SproutsEdge edge : edges()) {
                if (!drawnEdges.contains(edge)) {
                    new PolylineBatch(
                        edge.polyline(),
                        edge.color(),
                        lineThickness,
                        circleSegmentCount,
                        true
                    ).render(renderer);
                    drawnEdges.add(edge);
                    drawnEdges.add(edge.reversed());
                }
            }
            for (Vertex v : vertices()) {
                renderer.setColor(v.color());
                renderer.circle(
                    v.position().x,
                    v.position().y,
                    lineThickness,
                    circleSegmentCount
                );
    
                /*  //todo
                if (isAliveSprout(v)) {
                    renderer.setColor(Color.WHITE);
                } else {
                    renderer.setColor(Color.GRAY);
                }*/
                renderer.setColor(Color.WHITE);
    
                renderer.circle(
                    v.position().x,
                    v.position().y,
                    lineThickness / 2,
                    circleSegmentCount
                );
    
            }
            renderer.end();
        }
    
        @Override
        public Set<Vertex> vertices() {
            Set<Vertex> vertices = new HashSet<>();
            for (SproutsEdge edge : edges()) {
                vertices.add(edge.from());
                vertices.add(edge.to());
            }
            return vertices;
        }
    }
}
