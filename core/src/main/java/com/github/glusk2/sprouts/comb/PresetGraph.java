package com.github.glusk2.sprouts.comb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

/** A reference Graph implementation. */
public final class PresetGraph implements Graph {
    /** Default circle segment count. */
    private static final int DEFAULT_CIRCLE_SEGMENT_COUNT = 16;

    /**
     * The default thickness of the lines drawn on screen, measured in pixels.
     */
    private static final float DEFAULT_LINE_THICKNESS = 10;

    /** A list of Vertices and its LocalRotations. */
    private final Map<Vertex, LocalRotations> rotationsList;
    /** The thickness of the lines drawn on screen, measured in pixels. */
    private final float lineThickness;
    /**
     * Circle segment count.
     * <p>
     * Defines the number of points that define the border polyline of circles
     * drawn on screen.
     */
    private final int circleSegmentCount;

    /**
     * Constructs a new Graph from {@code rotationsList} and the default
     * rendering settings.
     *
     * @param rotationsList a list of Vertices and its LocalRotations
     */
    public PresetGraph(final Map<Vertex, LocalRotations> rotationsList) {
        this(
            rotationsList,
            DEFAULT_LINE_THICKNESS,
            DEFAULT_CIRCLE_SEGMENT_COUNT
        );
    }

    /**
     * Constructs a new Graph from {@code rotationsList} and the specified
     * rendering settings.
     *
     * @param rotationsList a list of Vertices and its LocalRotations
     * @param lineThickness the thickness of the lines drawn on screen,
     *                      measured in pixels
     * @param circleSegmentCount the number of points that define the border
     *                           polyline when rendering circles
     */
    public PresetGraph(
        final Map<Vertex, LocalRotations> rotationsList,
        final float lineThickness,
        final int circleSegmentCount
    ) {
        this.rotationsList = rotationsList;
        this.lineThickness = lineThickness;
        this.circleSegmentCount = circleSegmentCount;
    }

    @Override
    public void renderTo(final ShapeRenderer renderer) {
        Set<DirectedEdge> drawnEdges = new HashSet<DirectedEdge>();
        renderer.begin(ShapeType.Filled);
        for (DirectedEdge edge : edges()) {
            if (!drawnEdges.contains(edge)) {
                renderer.setColor(edge.color());
                List<Vector2> points = edge.polyline().points();
                for (int i = 1; i < points.size(); i++) {
                    Vector2 p1 = points.get(i - 1);
                    Vector2 p2 = points.get(i);
                    renderer.rectLine(p1, p2, lineThickness);
                    renderer.circle(
                        p2.x,
                        p2.y,
                        lineThickness / 2,
                        circleSegmentCount
                    );
                }
                drawnEdges.add(edge);
                // ToDo: ReversedEdge implementation!
                List<DirectedEdge> tmp =
                    rotationsList.get(edge.to()).edges();
                for (DirectedEdge e : tmp) {
                    if (e.to().equals(edge.from())) {
                        drawnEdges.add(e);
                        break;
                    }
                }
                // end of ToDo
            }
        }
        for (Vertex v : rotationsList.keySet()) {
            renderer.setColor(v.color());
            renderer.circle(
                v.position().x,
                v.position().y,
                lineThickness,
                circleSegmentCount
            );
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

    /**
     * Returns a Set of DirectedEdges of {@code this} Graph.
     *
     * @return a Set of DirectedEdges of {@code this} Graph
     */
    private Set<DirectedEdge> edges() {
        Set<DirectedEdge> result = new HashSet<DirectedEdge>();
        for (Vertex v: rotationsList.keySet()) {
            result.addAll(rotationsList.get(v).edges());
        }
        return result;
    }

    @Override
    public List<Set<DirectedEdge>> faces() {
        List<Set<DirectedEdge>> faces = new ArrayList<Set<DirectedEdge>>();

        Set<DirectedEdge> nextFace = new HashSet<DirectedEdge>();
        Set<DirectedEdge> burntEdges = new HashSet<DirectedEdge>();
        for (DirectedEdge edge : edges()) {
            DirectedEdge nextEdge = edge;
            while (
                !nextFace.contains(nextEdge)
             && !burntEdges.contains(nextEdge)
            ) {
                burntEdges.add(nextEdge);
                nextFace.add(nextEdge);
                // nextEdge = next(rev(nextEdge))
                // ToDo: ReversedEdge, NextEdge implementations!
                nextEdge = rotationsList.get(nextEdge.to())
                                        .next(nextEdge.from());
                // end of ToDo
            }
            if (!nextFace.isEmpty()) {
                faces.add(nextFace);
                nextFace = new HashSet<DirectedEdge>();
            }
        }
        return faces;
    }
}
