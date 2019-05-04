package com.github.glusk2.sprouts.comb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Initial Cobweb is a starting combinatorial position in a game of Sprouts.
 */
@SuppressWarnings("checkstyle:magicnumber")
public final class InitialCobweb implements Graph {

    /** Preset rotations list. */
    private static final Map<Vertex, LocalRotations> ROTATIONS_LIST;

    static {
        Vertex v1 = new PresetVertex(new Vector2(600, 100), 1);
        Vertex v2 = new PresetVertex(new Vector2(400, 400), 2);
        Vertex v3 = new PresetVertex(new Vector2(600, 600), 3);
        Vertex v4 = new PresetVertex(new Vector2(800, 400), 4);

        ROTATIONS_LIST = new HashMap<Vertex, LocalRotations>();

        ROTATIONS_LIST.put(
            v1,
            new PresetRotations(v1).with(
                new StraightLineEdge(v4),
                new StraightLineEdge(v2)
            )
        );
        ROTATIONS_LIST.put(
            v2,
            new PresetRotations(v2).with(
                new StraightLineEdge(v1)
            )
        );
        ROTATIONS_LIST.put(
            v3,
            new PresetRotations(v3).with(
                new StraightLineEdge(v4)
            )
        );
        ROTATIONS_LIST.put(
            v4,
            new PresetRotations(v4).with(
                new StraightLineEdge(v3),
                new StraightLineEdge(v1)
            )
        );
    }

    /** The wrapped Graph. */
    private final Graph graph;

    /**
     * Constructs the {@code InitialCobweb} from preset points.
     *
     * @param lineThickness the thickness of the lines drawn on screen,
     *                      measured in pixels
     * @param circleSegmentCount the number of points that define the border
     *                           polyline when rendering circles
     */
    public InitialCobweb(
        final float lineThickness,
        final int circleSegmentCount
    ) {
        this(
            new PresetGraph(
                ROTATIONS_LIST,
                lineThickness,
                circleSegmentCount
            )
        );
    }

    /**
     * Constructs a new Graph by wrapping {@code graph}.
     *
     * @param graph the Graph to wrap
     */
    public InitialCobweb(final Graph graph) {
        this.graph = graph;
    }

    @Override
    public void renderTo(final ShapeRenderer renderer) {
        graph.renderTo(renderer);
    }

    @Override
    public List<Set<CompoundEdge>> faces() {
        return graph.faces();
    }

    @Override
    public Graph with(final Vertex origin, final DirectedEdge direction) {
        return graph.with(origin, direction);
    }

    @Override
    public Graph without(final Vertex origin, final DirectedEdge direction) {
        return graph.without(origin, direction);
    }

    @Override
    public Set<Vertex> vertices() {
        return graph.vertices();
    }

    @Override
    public Set<CompoundEdge> edges() {
        return graph.edges();
    }

    @Override
    public Set<CompoundEdge> edgeFace(final CompoundEdge edge) {
        return graph.edgeFace(edge);
    }

    @Override
    public Graph simplified() {
        return graph.simplified();
    }
}
