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
import com.github.glusk2.sprouts.core.geom.PolylineBatch;

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
    public void render(final ShapeRenderer renderer) {
        Set<CompoundEdge> drawnEdges = new HashSet<CompoundEdge>();
        renderer.begin(ShapeType.Filled);
        for (CompoundEdge edge : edges()) {
            if (!drawnEdges.contains(edge)) {
                new PolylineBatch(
                    new CompoundPolyline(edge),
                    edge.direction().color(),
                    lineThickness,
                    circleSegmentCount,
                    true
                ).render(renderer);
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
            if (isAliveSprout(v)) {
                renderer.setColor(Color.WHITE);
            } else {
                renderer.setColor(Color.GRAY);
            }
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
    public Graph splitEdge(final CompoundEdge edge, final Vertex vertex) {
        // find the first point on the edge, closest to p
        Vector2 closest = null;
        int splitIndex = -1;
        List<Vector2> points = edge.direction().polyline().points();
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).dst(vertex.position()) <= lineThickness) {
                closest = points.get(i);
                splitIndex = i;
                break;
            }
        }
        if (closest == null) {
            return this;
        }

        Graph result = this;
        // remove edge from the graph
        result = result.without(edge.origin(), edge.direction());
        // remove the opposite of edge from the graph
        CompoundEdge opposite = new ReversedCompoundEdge(edge);
        result = result.without(opposite.origin(), opposite.direction());

        Color fromColor = Color.BLACK;
        for (Vertex v : vertices()) {
            if (v.position().equals(points.get(splitIndex + 1))) {
                fromColor = v.color();
            }
        }

        // split edge
        CompoundEdge s1 = new CompoundEdge.Wrapped(
            edge.origin(),
            new PolylineEdge(
                edge.direction().from().color(),
                Color.BLACK,
                points.subList(0, splitIndex + 1)
            )
        );
        CompoundEdge s2 = new CompoundEdge.Wrapped(
            new PresetVertex(Color.BLACK, points.get(splitIndex)),
            new PolylineEdge(
                fromColor,
                edge.direction().to().color(),
                points.subList(splitIndex + 1, points.size())
            )
        );
        // add both ends to the graph
        result = result.with(s1.origin(), s1.direction());
        result = result.with(s2.origin(), s2.direction());
        // add both opposites to the graph
        opposite = new ReversedCompoundEdge(s1);
        result = result.with(opposite.origin(), opposite.direction());
        opposite = new ReversedCompoundEdge(s2);
        result = result.with(opposite.origin(), opposite.direction());
        return result;
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
