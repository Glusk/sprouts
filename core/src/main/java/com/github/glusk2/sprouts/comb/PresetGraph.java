package com.github.glusk2.sprouts.comb;

import java.util.ArrayList;
import java.util.HashMap;
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
        Set<CompoundEdge> drawnEdges = new HashSet<CompoundEdge>();
        renderer.begin(ShapeType.Filled);
        for (CompoundEdge edge : edges()) {
            if (!drawnEdges.contains(edge)) {
                renderer.setColor(edge.direction().color());
                List<Vector2> points = edge.direction().polyline().points();
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
                drawnEdges.add(new ReversedCompoundEdge(edge));
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
    public Set<CompoundEdge> edges() {
        Set<CompoundEdge> result = new HashSet<CompoundEdge>();
        for (Vertex v : vertices()) {
            result.addAll(rotationsList.get(v).edges());
        }
        return result;
    }

    @Override
    public List<Set<CompoundEdge>> faces() {
        List<Set<CompoundEdge>> faces = new ArrayList<Set<CompoundEdge>>();

        Set<CompoundEdge> nextFace = new HashSet<CompoundEdge>();
        Set<CompoundEdge> burntEdges = new HashSet<CompoundEdge>();
        for (CompoundEdge edge : edges()) {
            CompoundEdge nextEdge = edge;
            while (
                !nextFace.contains(nextEdge)
             && !burntEdges.contains(nextEdge)
            ) {
                burntEdges.add(nextEdge);
                nextFace.add(nextEdge);
                nextEdge =
                    new CachedCompoundEdge(
                        new NextCompoundEdge(
                            rotationsList,
                            new ReversedCompoundEdge(nextEdge)
                        )
                    );
            }
            if (!nextFace.isEmpty()) {
                faces.add(nextFace);
                nextFace = new HashSet<CompoundEdge>();
            }
        }
        return faces;
    }

    @Override
    public Graph with(final Vertex origin, final DirectedEdge direction) {
        Map<Vertex, LocalRotations> newRotations =
            new HashMap<Vertex, LocalRotations>(rotationsList);
        if (rotationsList.containsKey(origin)) {
            newRotations.put(
                origin,
                rotationsList.get(origin).with(direction)
            );
        } else {
            newRotations.put(
                origin,
                new PresetRotations(origin).with(direction)
            );
        }
        return
            new PresetGraph(
                newRotations,
                lineThickness,
                circleSegmentCount
            );
    }

    @Override
    public Graph without(final Vertex origin, final DirectedEdge direction) {
        Map<Vertex, LocalRotations> newRotations =
            new HashMap<Vertex, LocalRotations>(rotationsList);
        if (rotationsList.containsKey(origin)) {
            newRotations.put(
                origin,
                rotationsList.get(origin).without(direction)
            );
        }
        return
            new PresetGraph(
                newRotations,
                lineThickness,
                circleSegmentCount
            );
    }

    @Override
    public Set<Vertex> vertices() {
        return rotationsList.keySet();
    }
}
