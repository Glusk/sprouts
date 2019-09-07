package com.github.glusk2.sprouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.comb.CachedCompoundEdge;
import com.github.glusk2.sprouts.comb.CompoundEdge;
import com.github.glusk2.sprouts.comb.DirectedEdge;
import com.github.glusk2.sprouts.comb.Graph;
import com.github.glusk2.sprouts.comb.IntersectionSegmentFace;
import com.github.glusk2.sprouts.comb.NearestSprout;
import com.github.glusk2.sprouts.comb.PolylineEdge;
import com.github.glusk2.sprouts.comb.PresetVertex;
import com.github.glusk2.sprouts.comb.ReversedCompoundEdge;
import com.github.glusk2.sprouts.comb.StraightLineEdge;
import com.github.glusk2.sprouts.comb.Vertex;
import com.github.glusk2.sprouts.comb.VoidVertex;
import com.github.glusk2.sprouts.geom.IsPointOnLineSegment;
import com.github.glusk2.sprouts.geom.Polyline;
import com.github.glusk2.sprouts.geom.PolylinePiece;
import com.github.glusk2.sprouts.geom.TrimmedPolyline;

/** The Submove reference implementation. */
public final class PresetSubmove implements Submove {
    /**
     * Maximum error margin for detection of intersection between a line
     * segment and a point.
     */
    private static final float LINE_INTERSECT_ERROR = .5f;
    /** The Graph Vertex in which {@code this} Submove begins. */
    private final Vertex origin;
    /** The polyline approximation of the move stroke. */
    private final Polyline stroke;
    /** The game state before {@code this} Submove. */
    private final Graph currentState;
    /**
     * The Vertex glue radius, used to auto-complete {@code this} Submove
     * when near a {@code currentState} Vertex.
     */
    private final float vertexGlueRadius;

    /**
     * Creates a new Submove.
     *
     * @param origin the Graph Vertex in which {@code this} Submove begins
     * @param stroke the polyline approximation of the move stroke
     * @param currentState the game state before {@code this} Submove
     * @param vertexGlueRadius the Vertex glue radius, used to auto-complete
     *                         {@code this} Submove when near a
     *                         {@code currentState} Vertex
     */
    public PresetSubmove(
        final Vertex origin,
        final Polyline stroke,
        final Graph currentState,
        final float vertexGlueRadius
    ) {
        this.origin = origin;
        this.stroke = stroke;
        this.currentState = currentState;
        this.vertexGlueRadius = vertexGlueRadius;
    }

    @Override
    public Vertex origin() {
        return origin;
    }

    @Override
    public DirectedEdge direction() {
        List<Vector2> strokePoints = stroke.points();
        if (strokePoints.isEmpty()) {
            throw
                new IllegalStateException(
                    "At least 1 sample point is needed to establish a "
                     + "direction!"
                );
        }
        Set<CompoundEdge> moveFace =
            currentState.edgeFace(
                new CachedCompoundEdge(
                    origin,
                    new StraightLineEdge(
                        new PresetVertex(
                            strokePoints.get(0),
                            (String) null
                        )
                    )
                )
            );
        for (int i = 0; i < strokePoints.size(); i++) {
            Vector2 p1 = strokePoints.get(i);
            if (i > 0) {
                Vector2 p0 = strokePoints.get(i - 1);
                // Check if crosses cobweb
                Vertex crossPoint =
                    new IntersectionSegmentFace(moveFace, p0, p1).result();
                if (!crossPoint.equals(new VoidVertex(null))) {
                    List<Vector2> returnPoints =
                        new ArrayList<Vector2>(strokePoints.subList(0, i));
                    returnPoints.add(crossPoint.position());
                    Color toColor = crossPoint.color();
                    if (toColor.equals(Color.BLACK)) {
                        toColor = Color.GRAY;
                    }
                    return
                        new PolylineEdge(
                            origin().color(),
                            toColor,
                            returnPoints
                        );
                }
            }

            // Check if close to a Graph Vertex and finnish
            Vertex v = new NearestSprout(currentState, p1).result();
            if (v.position().dst(p1) < vertexGlueRadius) {
                List<Vector2> returnPoints =
                    new ArrayList<Vector2>(strokePoints.subList(0, i));
                returnPoints.add(v.position());
                return
                    new PolylineEdge(
                        origin().color(),
                        v.color(),
                        returnPoints
                    );
            }
        }
        return
            new PolylineEdge(
                origin().color(),
                Color.CLEAR,
                strokePoints
            );
    }

    @Override
    public boolean isCompleted() {
        Color tipColor = Color.CLEAR;
        if (isReadyToRender()) {
            tipColor = direction().to().color();
        }
        return tipColor.equals(Color.BLACK) || tipColor.equals(Color.RED);
    }

    @Override
    public boolean isReadyToRender() {
        return !stroke.points().isEmpty();
    }

    @Override
    public boolean isValid() {
        return !direction().to().color().equals(Color.GRAY);
    }

    @Override
    public Graph updatedState() {
        if (!isCompleted()) {
            throw new IllegalStateException("Submove not yet completed!");
        }
        Graph result = currentState;
        DirectedEdge currentDirection = direction();
        Vertex tip = currentDirection.to();
        if (tip.color().equals(Color.RED)) {
            for (CompoundEdge edge : currentState.edges()) {
                if (edge.direction().color().equals(Color.RED)) {
                    Vector2 p0 = edge.origin().position();
                    Vector2 p1 = edge.direction().to().position();
                    Vector2 point = tip.position();
                    if (
                        new IsPointOnLineSegment(
                            p0,
                            p1,
                            point,
                            LINE_INTERSECT_ERROR
                        ).check()
                    ) {
                        CompoundEdge twin = new ReversedCompoundEdge(edge);
                        CompoundEdge fromRed =
                            new CompoundEdge.Wrapped(
                                edge.origin(),
                                new StraightLineEdge(tip)
                            );
                        CompoundEdge fromRedRev =
                            new ReversedCompoundEdge(fromRed);
                        CompoundEdge redTo =
                            new CompoundEdge.Wrapped(
                                tip,
                                new StraightLineEdge(edge.direction().to())
                            );
                        CompoundEdge redToRev = new ReversedCompoundEdge(redTo);
                        result = result
                            .without(edge.origin(), edge.direction())
                            .without(twin.origin(), twin.direction())
                            .with(fromRed.origin(), fromRed.direction())
                            .with(fromRedRev.origin(), fromRedRev.direction())
                            .with(redTo.origin(), redTo.direction())
                            .with(redToRev.origin(), redToRev.direction());
                        break;
                    }
                }
            }
        }
        CompoundEdge reversed =
            new ReversedCompoundEdge(origin(), currentDirection);
        return
            result.with(origin(), currentDirection)
                  .with(reversed.origin(), reversed.direction())
                  .simplified();
    }

    @Override
    public boolean hasNext() {
        return isCompleted() && !direction().to().color().equals(Color.BLACK);
    }

    @Override
    public Submove next() {
        if (!isCompleted()) {
            throw new IllegalStateException("Submove not yet completed!");
        }
        float minDistance = 0;
        Vertex tip = direction().to();
        if (!tip.color().equals(Color.RED)) {
            minDistance = vertexGlueRadius;
        }
        return
            new PresetSubmove(
                tip,
                new TrimmedPolyline(
                    new PolylinePiece(
                        stroke,
                        tip.position()
                    ),
                    minDistance
                ),
                updatedState(),
                vertexGlueRadius
            );
    }
}
