package com.github.glusk2.sprouts.core.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.Move;
import com.github.glusk2.sprouts.core.RenderedMove;
import com.github.glusk2.sprouts.core.SubmoveElement;
import com.github.glusk2.sprouts.core.SubmoveHead;
import com.github.glusk2.sprouts.core.SubmoveSequence;
import com.github.glusk2.sprouts.core.comb.Graph;
import com.github.glusk2.sprouts.core.comb.MoveTransformation;
import com.github.glusk2.sprouts.core.comb.PresetVertex;
import com.github.glusk2.sprouts.core.comb.TransformedGraph;
import com.github.glusk2.sprouts.core.comb.Vertex;
import com.github.glusk2.sprouts.core.geom.BezierCurve;
import com.github.glusk2.sprouts.core.geom.CurveApproximation;
import com.github.glusk2.sprouts.core.geom.Polyline;
import com.github.glusk2.sprouts.core.geom.PolylinePiece;
import com.github.glusk2.sprouts.core.geom.TrimmedPolyline;

/**
 * This Snapshot represents the game board <em>after</em> a Move is drawn,
 * indicating that a sprout can be added anywhere on the Move.
 * <p>
 * A "touch up" event detects the position of a new sprout and adds it to the
 * last drawn Move.
 */
public final class SproutAdd implements Snapshot {
    /**
     * Spline segment count.
     * <p>
     * Defines the number of discrete points on interval: {@code 0 <= t <= 1}
     * for spline rendering.
     */
    private static final int SPLINE_SEGMENT_COUNT = 5;
    /**
     * Perpendicular distance modifier.
     * <p>
     * This is multiplied by {@code lineThickness} to compute the
     * {@code tolerance} in
     * {@link com.github.glusk2.sprouts.core.geom.PerpDistSimpl#PerpDistSimpl(
     *     java.util.List,
     *     float
     * )}.
     */
    private static final float PERP_DISTANCE_MODIFIER = 3f;

    /** The Graph that a Move is being drawn to. */
    private final Graph currentState;
    /** The thickness of the Moves drawn. */
    private final float moveThickness;
    /** The number of segments used to draw circular Vertices. */
    private final int circleSegmentCount;
    /**
     * The origin sprout of the Move that is being drawn in {@code this}
     * Snapshot.
     */
    private final Vertex moveOrigin;
    /**
     * The sample points of the Move that is being drawn in {@code this}
     * Snapshot.
     */
    private final List<Vector2> moveSample;
    /** Any Submove that is drawn outside of {@code gameBounds} is invalid. */
    private final Rectangle gameBounds;

    /**
     * Creates a new SproutAdd Snapshot from the {@code currentState},
     * {@code moveOrigin} and {@code moveSample}.
     *
     * @param currentState the Graph that a Move is being drawn to
     * @param moveThickness  the thickness of the Moves drawn
     * @param circleSegmentCount the number of segments used to draw circular
     *                           Vertices
     * @param moveOrigin the origin sprout of the Move that is being drawn in
     *                   {@code this} Snapshot
     * @param moveSample the sample points of the Move that is being drawn in
     *                   {@code this} Snapshot
     * @param gameBounds any Submove that is drawn outside of
     *                   {@code gameBounds} is invalid
     */
    public SproutAdd(
        final Graph currentState,
        final float moveThickness,
        final int circleSegmentCount,
        final Vertex moveOrigin,
        final List<Vector2> moveSample,
        final Rectangle gameBounds
    ) {
        this.currentState = currentState;
        this.moveThickness = moveThickness;
        this.circleSegmentCount = circleSegmentCount;
        this.moveOrigin = moveOrigin;
        this.moveSample = moveSample;
        this.gameBounds = gameBounds;
    }

    /**
     * Builds and returns a new Move from {@code moveOrigin} and {@code stroke}
     * im a given {@code state}.
     *
     * @param state the state to which the returned Move is to be added
     * @param origin the origin of the returned Move
     * @param stroke the polyline approximation of the Move curve
     * @return a new Move
     */
    private Move moveFromOriginAndStroke(
        final Graph state,
        final Vertex origin,
        final Polyline stroke
    ) {
        return
            new SubmoveSequence(
                new SubmoveHead(
                    new SubmoveElement(
                        origin,
                        stroke,
                        state,
                        moveThickness * 2,
                        gameBounds
                    )
                )
            );
    }

    /**
     * Creates and return a Bezier interpolated and approximated curve from
     * {@code moveSample} points.
     *
     * @return a Bezier interpolated and approximated curve
     */
    private Polyline stroke() {
        return
            new CurveApproximation(
                new BezierCurve(
                    new ArrayList<Vector2>(moveSample),
                    PERP_DISTANCE_MODIFIER * moveThickness
                ),
                SPLINE_SEGMENT_COUNT
            );
    }

    @Override
    public Snapshot touchDown(final Vector2 position) {
        return this;
    }

    @Override
    public Snapshot touchUp(final Vector2 position) {
        Polyline firstHalf =
            new PolylinePiece(
                stroke(),
                position,
                true,
                moveThickness
            );
        Polyline secondHalf =
            new TrimmedPolyline(
                new PolylinePiece(
                    stroke(),
                    position,
                    false,
                    moveThickness
                ),
                2 * moveThickness
            );

        if (!firstHalf.points().isEmpty() && !secondHalf.points().isEmpty()) {
            Vertex sproutToAdd =
                new PresetVertex(
                    Color.BLACK,
                    position
                );
            Graph stateWithSprout = currentState.with(sproutToAdd);

            Move first =
                moveFromOriginAndStroke(
                    stateWithSprout,
                    moveOrigin,
                    firstHalf
                );
            Graph stateAfterFirstMove =
               new TransformedGraph(
                   new MoveTransformation(
                       first,
                       stateWithSprout
                   )
               );

            Move second =
                moveFromOriginAndStroke(
                    stateAfterFirstMove,
                    sproutToAdd,
                    secondHalf
                );
            Graph stateAfterSecondMove =
               new TransformedGraph(
                   new MoveTransformation(
                       second,
                       stateAfterFirstMove
                   )
               );

            return
                new BeforeMove(
                    stateAfterSecondMove,
                    moveThickness,
                    circleSegmentCount,
                    gameBounds
                );
        }
        return
            new BeforeMove(
                currentState,
                moveThickness,
                circleSegmentCount,
                gameBounds
            );
    }

    @Override
    public Snapshot touchDragged(final Vector2 position) {
        return this;
    }

    @Override
    public void render(final ShapeRenderer renderer) {
        new RenderedMove(
            moveFromOriginAndStroke(currentState, moveOrigin, stroke()),
            moveThickness,
            circleSegmentCount
        ).render(renderer);

        currentState.render(renderer);
    }
}
