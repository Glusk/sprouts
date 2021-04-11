package com.github.glusk2.sprouts.core.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.glusk2.sprouts.core.Move;
import com.github.glusk2.sprouts.core.RenderedMove;
import com.github.glusk2.sprouts.core.SubmoveElement;
import com.github.glusk2.sprouts.core.SubmoveHead;
import com.github.glusk2.sprouts.core.SubmoveSequence;
import com.github.glusk2.sprouts.core.comb.SproutsGameState;
import com.github.glusk2.sprouts.core.comb.Vertex;
import com.github.glusk2.sprouts.core.geom.BezierCurve;
import com.github.glusk2.sprouts.core.geom.CurveApproximation;

/**
 * This Snapshot represents the game board <em>while</em> a Move is being drawn.
 * <p>
 * A "touch dragged" event detects the current touch position and returns a new
 * {@code MoveDrawing} Snapshot with one additional Move sample point.
 * <p>
 * A "touch up" event designates the end of the current Move. If the Move is
 * valid and completed, it gets added to the {@code currentState} and a new
 * {@code BeforeMove} Snapshot is returned with the updated
 * {@code currentState}, otherwise a new {@code BeforeMove} is returned with the
 * {@code currentState}.
 */
public final class MoveDrawing implements Snapshot {
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


    /** The graph that a Move is being drawn to. */
    private final SproutsGameState gameState;
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
     * Creates a new MoveDrawing Snapshot from the {@code currentState},
     * {@code moveOrigin} and {@code moveSample}.
     *
     * @param gameState the Graph that a Move is being drawn to
     * @param moveThickness the thickness of the Moves drawn
     * @param circleSegmentCount the number of segments used to draw circular
     *                           Vertices
     * @param moveOrigin the origin sprout of the Move that is being drawn
     *                   in {@code this} Snapshot
     * @param moveSample the sample points of the Move that is being drawn
     *                   in {@code this} Snapshot
     * @param gameBounds any Submove that is drawn outside of
     *                   {@code gameBounds} is invalid
     */
    public MoveDrawing(
        final SproutsGameState gameState,
        final float moveThickness,
        final int circleSegmentCount,
        final Vertex moveOrigin,
        final List<Vector2> moveSample,
        final Rectangle gameBounds
    ) {
        this.gameState = gameState;
        this.moveThickness = moveThickness;
        this.circleSegmentCount = circleSegmentCount;
        this.moveOrigin = moveOrigin;
        this.moveSample = moveSample;
        this.gameBounds = gameBounds;
    }

    /**
     * Builds and returns a new Move from {@code moveOrigin} and
     * {@code moveSample}.
     *
     * @return a new Move
     */
    private Move moveFromSampleAndOrigin() {
        return
            new SubmoveSequence(
                new SubmoveHead(
                    new SubmoveElement(
                        moveOrigin,
                        new CurveApproximation(
                            new BezierCurve(
                                new ArrayList<Vector2>(moveSample),
                                PERP_DISTANCE_MODIFIER * moveThickness
                            ),
                            SPLINE_SEGMENT_COUNT
                        ),
                        gameState,
                        moveThickness * 2,
                        gameBounds
                    )
                )
            );
    }

    @Override
    public Snapshot touchDown(final Vector2 position) {
        return this;
    }

    @Override
    public Snapshot touchUp(final Vector2 position) {
        Move nextMove = moveFromSampleAndOrigin();
        if (nextMove.isValid() && nextMove.isCompleted()) {
            return
                new SproutAdd(
                    gameState,
                    moveThickness,
                    circleSegmentCount,
                    moveOrigin,
                    moveSample,
                    gameBounds
                );
        }

        return
            new BeforeMove(
                gameState,
                moveThickness,
                circleSegmentCount,
                gameBounds
            );
    }

    @Override
    public Snapshot touchDragged(final Vector2 position) {
        Vector2 lastElement = moveSample.get(moveSample.size() - 1);
        if (position.dst(lastElement) > 2 * moveThickness) {
            List<Vector2> newSample = new LinkedList<Vector2>(moveSample);
            newSample.add(position);
            return
                new MoveDrawing(
                    gameState,
                    moveThickness,
                    circleSegmentCount,
                    moveOrigin,
                    newSample,
                    gameBounds
                );
        }
        return this;
    }

    @Override
    public void render(final ShapeRenderer renderer) {
        new RenderedMove(
            moveFromSampleAndOrigin(),
            moveThickness,
            circleSegmentCount
        ).render(renderer);
        gameState.render(renderer, moveThickness, circleSegmentCount);
    }

    @Override
    public SproutsGameState gameState() {
        return this.gameState;
    }
}
