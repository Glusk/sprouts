package com.github.glusk2.sprouts.core.comb;

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

    /**
     * If a sprout has this degree (the number of Submove edges connected to
     * it), it is dead.
     */
    private static final int DEAD_SPROUT_DEGREE = 3;

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
                for (int i = 0; i < points.size(); i++) {
                    Vector2 p1 = null;
                    if (i == 0) {
                        p1 = edge.origin().position();
                    } else {
                        p1 = points.get(i - 1);
                    }
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

    @Override
    public Graph with(final Vertex additionalVertex) {
        Map<Vertex, LocalRotations> newRotations =
        new HashMap<Vertex, LocalRotations>(rotationsList);
    if (rotationsList.containsKey(additionalVertex)) {
        return this;
    }
    newRotations.put(
        additionalVertex,
        new PresetRotations(additionalVertex)
    );
    return
        new PresetGraph(
            newRotations,
            lineThickness,
            circleSegmentCount
        );
    }

    @Override
    public Set<CompoundEdge> edgeFace(final CompoundEdge edge) {
        CompoundEdge next =
            new CachedCompoundEdge(
                new NextCompoundEdge(
                    rotationsList,
                    edge
                )
            );
        for (Set<CompoundEdge> face : faces()) {
            if (face.contains(next)) {
                return face;
            }
        }
        throw
            new IllegalArgumentException(
                "Edge is not connected to this graph"
            );
    }

    @Override
    public Graph simplified() {
        for (Set<CompoundEdge> f1 : faces()) {
            for (Set<CompoundEdge> f2 : faces()) {
                if (f1.equals(f2)) {
                    continue;
                }
                for (CompoundEdge e1 : f1) {
                    for (CompoundEdge e2 : f2) {
                        if (
                            // checks not complete
                            e1.origin().equals(e2.direction().to())
                            && e1.direction().to().equals(e2.origin())
                            && e2.origin().equals(e1.direction().to())
                            && e2.direction().to().equals(e1.origin())
                            && e1.direction().color() == Color.RED
                            && e2.direction().color() == Color.RED
                        ) {
                            return this.without(e1.origin(), e1.direction())
                                       .without(e2.origin(), e2.direction());
                        }
                    }
                }
            }
        }
        return this;
    }

    @Override
    public boolean isAliveSprout(final Vertex vertex) {
        // Check if it is a sprout
        if (!vertex.color().equals(Color.BLACK)) {
            return false;
        }
        // Check if the sprout is alive
        int degree = vertexDegree(vertex, Color.BLACK);
        return degree >= 0 && degree < DEAD_SPROUT_DEGREE;
    }

    @Override
    public int vertexDegree(final Vertex vertex, final Color edgeColor) {
        // Check if it is connected to this Graph
        if (!vertices().contains(vertex)) {
            return -1;
        }
        // Compute the degree
        int degree = 0;
        for (CompoundEdge edge : rotationsList.get(vertex).edges()) {
            if (edge.direction().color().equals(edgeColor)) {
                degree++;
            }
        }
        return degree;
    }
}