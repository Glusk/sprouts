package com.github.glusk2.sprouts.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.comb.FaceIntersectionSearch;
import com.github.glusk2.sprouts.core.comb.IsAliveSprout;
import com.github.glusk2.sprouts.core.comb.IsSubmovePossibleInFace;
import com.github.glusk2.sprouts.core.comb.NearestSproutSearch;
import com.github.glusk2.sprouts.core.comb.PolylineIntersectionSearch;
import com.github.glusk2.sprouts.core.comb.SproutsEdge;
import com.github.glusk2.sprouts.core.comb.SproutsFaces;
import com.github.glusk2.sprouts.core.comb.SproutsGameState;
import com.github.glusk2.sprouts.core.comb.SproutsStateAfterSubmove;
import com.github.glusk2.sprouts.core.comb.Vertex;
import com.github.glusk2.sprouts.core.comb.VertexDegree;
import com.github.glusk2.sprouts.core.comb.VoidVertex;
import com.github.glusk2.sprouts.core.geom.IsPointOnLineSegment;
import com.github.glusk2.sprouts.core.geom.Polyline;
import com.github.glusk2.sprouts.core.geom.PolylinePiece;

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
    private final SproutsGameState currentState;
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
        final SproutsGameState currentState,
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
        final SproutsGameState currentState,
        final float vertexGlueRadius,
        final Rectangle gameBounds
    ) {
        this.origin = origin;
        this.stroke = stroke;
        this.currentState = currentState;
        this.vertexGlueRadius = vertexGlueRadius;
        this.gameBounds = gameBounds;
    }

    SproutsEdge cache = null;
    @Override
    public SproutsEdge asEdge() {
        if (cache != null) return cache;
        List<Vector2> strokePoints = stroke.points();
        if (strokePoints.isEmpty()) {
            throw
                new IllegalStateException(
                    "At least 1 sample point is needed to establish a "
                     + "direction!"
                );
        }
        Set<SproutsEdge> moveFace =
            new SproutsFaces(
                currentState.edges()
            ).drawnIn(
                new SproutsEdge(
                    true,
                    new Polyline.WrappedList(strokePoints),
                    origin.color(), // from  
                    Color.BLACK  // to
                )
            );
        for (int i = 0; i < strokePoints.size(); i++) {
            if (
                i >= INVALID_WINDOW
             && !new IsSubmovePossibleInFace(
                    origin.color().equals(Color.BLACK),
                    currentState,
                    moveFace
                ).check()
            ) {
                cache =
                    new SproutsEdge(
                        true,
                        new Polyline.WrappedList(
                            new ArrayList<Vector2>(strokePoints.subList(0, i))
                        ),
                        origin.color(),
                        Color.GRAY
                    );
                return cache;
            }

            Vector2 p1 = strokePoints.get(i);
            if (!gameBounds.contains(p1)) {
                cache =
                    new SproutsEdge(
                        true,
                        new Polyline.WrappedList(
                            new ArrayList<Vector2>(strokePoints.subList(0, i))
                        ),
                        origin.color(),
                        Color.GRAY
                    );
                return cache;
            }

            if (i >= 4) { // left + right hooks, so require 4 stroke points at a minimum
                Vertex v = new NearestSproutSearch(currentState, p1).result();
                if (v.position().dst(p1) < vertexGlueRadius) {
                    List<Vector2> returnPoints =
                        new ArrayList<Vector2>(strokePoints.subList(0, i));
                    returnPoints.add(v.position());
                    cache =
                        new SproutsEdge(
                            true,
                            new Polyline.WrappedList(returnPoints),
                            origin.color(),
                            v.color()
                        );
                    return cache;
                }
            }

            if (i > 0) {
                Vector2 p0 = strokePoints.get(i - 1);

                // Check if too close to a red vertex and abort
                boolean intesectsCobwebVertex = currentState.vertices()
                    .stream()
                    .anyMatch(v->
                        v.color().equals(Color.RED)
                     && new IsPointOnLineSegment(
                            p0, p1, v.position(), vertexGlueRadius
                        ).check()
                    );
                if (intesectsCobwebVertex) {
                    cache =
                        new SproutsEdge(
                            true,
                            new Polyline.WrappedList(strokePoints.subList(0, i)),
                            origin.color(),
                            Color.GRAY
                        );
                    return cache;
                }

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
                    cache =
                        new SproutsEdge(
                            true,
                            new Polyline.WrappedList(returnPoints),
                            origin.color(),
                            Color.GRAY
                        );
                    return cache;
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
                    cache =
                        new SproutsEdge(
                            true,
                            new Polyline.WrappedList(returnPoints),
                            origin.color(),
                            toColor
                        );
                    return cache;
                }
            }
        }
        cache =
            new SproutsEdge(
                true,
                new Polyline.WrappedList(strokePoints),
                origin.color(),
                Color.CLEAR
            );
        return cache;
    }

    @Override
    public boolean isCompleted() {
        Color tipColor = Color.CLEAR;
        if (isReadyToRender()) {
            tipColor = asEdge().to().color();
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

        Vertex from = origin;
        Vertex to = asEdge().to();

        boolean intermediate = true;
        if (from.color().equals(Color.BLACK)) {
            intermediate &= new IsAliveSprout(from, currentState).check();
        }
        if (to.color().equals(Color.BLACK)) {
            intermediate &= new IsAliveSprout(to, currentState).check();
        }
        if (from.equals(to)) {
            intermediate &=
                new VertexDegree(
                    from,
                    currentState,
                    Color.BLACK
                ).intValue() < 2;
        }
        return intermediate && !to.color().equals(Color.GRAY);
    }

    @Override
    public boolean hasNext() {
        return isCompleted() && !asEdge().to().color().equals(Color.BLACK);
    }

    @Override
    public Submove next() {
        if (!hasNext()) {
            throw new IllegalStateException("This is the tail Submove.");
        }
        Vertex tip = asEdge().to();
        return
            new SubmoveElement(
                tip,
                new PolylinePiece(
                    stroke,
                    tip.position()
                ),
                new SproutsStateAfterSubmove(currentState, this),
                vertexGlueRadius,
                gameBounds
            );
    }
}
