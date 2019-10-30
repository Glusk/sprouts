package com.github.glusk2.sprouts.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.comb.CachedCompoundEdge;
import com.github.glusk2.sprouts.core.comb.CompoundEdge;
import com.github.glusk2.sprouts.core.comb.DirectedEdge;
import com.github.glusk2.sprouts.core.comb.FaceIntersectionSearch;
import com.github.glusk2.sprouts.core.comb.Graph;
import com.github.glusk2.sprouts.core.comb.IsMovePossibleInFace;
import com.github.glusk2.sprouts.core.comb.NearestSproutSearch;
import com.github.glusk2.sprouts.core.comb.PolylineEdge;
import com.github.glusk2.sprouts.core.comb.PolylineIntersectionSearch;
import com.github.glusk2.sprouts.core.comb.PresetVertex;
import com.github.glusk2.sprouts.core.comb.StraightLineEdge;
import com.github.glusk2.sprouts.core.comb.SubmoveTransformation;
import com.github.glusk2.sprouts.core.comb.TransformedGraph;
import com.github.glusk2.sprouts.core.comb.Vertex;
import com.github.glusk2.sprouts.core.comb.VoidVertex;
import com.github.glusk2.sprouts.core.geom.Polyline;
import com.github.glusk2.sprouts.core.geom.PolylinePiece;
import com.github.glusk2.sprouts.core.geom.TrimmedPolyline;

/**
 * A SubmoveElement is a Submove in a sequence of Submoves that comprise a Move.
 * <p>
 * The first element of any such sequence is always the {@link SubmoveHead}.
 */
public final class SubmoveElement implements Submove {
    /**
     * The maximum Submove length (in line segments) allowed to draw when
     * drawing in a face that has a less than 2 sprout lives.
     */
    private static final int INVALID_WINDOW = 7;
    /** The Graph Vertex in which {@code this} Submove begins. */
    private final Vertex origin;
    /** The polyline approximation of the move stroke. */
    private final Polyline stroke;
    /** The game state before {@code this} Submove. */
    private final Graph currentState;
    /**
     * The Vertex glue radius, used to auto-complete {@code this} Submove
     * when near a sprout.
     */
    private final float vertexGlueRadius;

    /** Any Submove that is drawn outside of {@code gameBounds} is invalid. */
    private final Rectangle gameBounds;

    /**
     * Creates a new Submove.
     * <p>
     * This constructor uses the default bounding box rectangle:
     * <pre>
     * new Rectangle(
     *     0,
     *     0,
     *     Float.POSITIVE_INFINITY,
     *     Float.POSITIVE_INFINITY
     * )
     * </pre>
     *
     * @param origin the Graph Vertex in which {@code this} Submove begins
     * @param stroke the polyline approximation of the move stroke
     * @param currentState the game state before {@code this} Submove
     * @param vertexGlueRadius the Vertex glue radius, used to auto-complete
     *                         {@code this} Submove when near a sprout
     */
    public SubmoveElement(
        final Vertex origin,
        final Polyline stroke,
        final Graph currentState,
        final float vertexGlueRadius
    ) {
        this(
            origin,
            stroke,
            currentState,
            vertexGlueRadius,
            new Rectangle(
                0,
                0,
                Float.POSITIVE_INFINITY,
                Float.POSITIVE_INFINITY
            )
        );
    }

    /**
     * Creates a new Submove.
     *
     * @param origin the Graph Vertex in which {@code this} Submove begins
     * @param stroke the polyline approximation of the move stroke
     * @param currentState the game state before {@code this} Submove
     * @param vertexGlueRadius the Vertex glue radius, used to auto-complete
     *                         {@code this} Submove when near a sprout
     * @param gameBounds any Submove that is drawn outside of
     *                   {@code gameBounds} is invalid
     */
    public SubmoveElement(
        final Vertex origin,
        final Polyline stroke,
        final Graph currentState,
        final float vertexGlueRadius,
        final Rectangle gameBounds
    ) {
        this.origin = origin;
        this.stroke = stroke;
        this.currentState = currentState;
        this.vertexGlueRadius = vertexGlueRadius;
        this.gameBounds = gameBounds;
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
                            strokePoints.get(0)
                        )
                    )
                )
            );
        for (int i = 0; i < strokePoints.size(); i++) {
            if (
                i >= INVALID_WINDOW
             && !new IsMovePossibleInFace(
                    currentState,
                    moveFace
                ).check()
            ) {
                return
                    new PolylineEdge(
                        origin().color(),
                        Color.GRAY,
                        new ArrayList<Vector2>(strokePoints.subList(0, i))
                    );
            }
            Vector2 p1 = strokePoints.get(i);
            if (!gameBounds.contains(p1)) {
                return
                    new PolylineEdge(
                        origin().color(),
                        Color.GRAY,
                        new ArrayList<Vector2>(strokePoints.subList(0, i))
                    );
            }
            // Check if close to a sprout and finnish
            Vertex v = new NearestSproutSearch(currentState, p1).result();
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
            if (i > 0) {
                Vector2 p0 = strokePoints.get(i - 1);
                // Check if crosses itself
                Vertex crossPoint =
                    new PolylineIntersectionSearch(
                        p0,
                        p1,
                        new Polyline.WrappedList(strokePoints.subList(0, i)),
                        Color.BLACK
                    ).result();
                if (crossPoint.color().equals(Color.BLACK)) {
                    List<Vector2> returnPoints =
                        new ArrayList<Vector2>(strokePoints.subList(0, i));
                    returnPoints.add(crossPoint.position());
                    return
                        new PolylineEdge(
                            origin().color(),
                            Color.GRAY,
                            returnPoints
                        );
                }
                // Check if crosses the face
                crossPoint =
                    new FaceIntersectionSearch(moveFace, p0, p1).result();
                if (!crossPoint.equals(new VoidVertex())) {
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
        if (!isReadyToRender()) {
            return false;
        }

        Vertex from = origin();
        Vertex to = direction().to();

        boolean intermediate = true;
        if (from.color().equals(Color.BLACK)) {
            intermediate &= currentState.isAliveSprout(from);
        }
        if (to.color().equals(Color.BLACK)) {
            intermediate &= currentState.isAliveSprout(to);
        }
        if (from.equals(to)) {
            intermediate &= currentState.vertexDegree(from, Color.BLACK) < 2;
        }
        return intermediate && !to.color().equals(Color.GRAY);
    }

    @Override
    public boolean hasNext() {
        return isCompleted() && !direction().to().color().equals(Color.BLACK);
    }

    @Override
    public Submove next() {
        if (!hasNext()) {
            throw new IllegalStateException("This is the tail Submove.");
        }
        float minDistance = 0;
        Vertex tip = direction().to();
        if (!tip.color().equals(Color.RED)) {
            minDistance = vertexGlueRadius;
        }
        return
            new SubmoveElement(
                tip,
                new TrimmedPolyline(
                    new PolylinePiece(
                        stroke,
                        tip.position()
                    ),
                    minDistance
                ),
                new TransformedGraph(
                    new SubmoveTransformation(
                        new CachedCompoundEdge(this),
                        currentState
                    )
                ),
                vertexGlueRadius,
                gameBounds
            );
    }
}
