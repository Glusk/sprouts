package com.github.glusk2.sprouts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.glusk2.sprouts.comb.Graph;
import com.github.glusk2.sprouts.comb.InitialCobweb;
import com.github.glusk2.sprouts.comb.MoveTransformation;
import com.github.glusk2.sprouts.comb.TransformedGraph;
import com.github.glusk2.sprouts.comb.Vertex;
import com.github.glusk2.sprouts.geom.BezierCurve;
import com.github.glusk2.sprouts.geom.CurveApproximation;
import com.github.glusk2.sprouts.geom.Polyline;
import com.github.glusk2.sprouts.geom.TrimmedPolyline;

/**
 * Sprouts main application class (renders the game and captures player input).
 */
public final class Sprouts extends InputAdapter implements ApplicationListener {

    /** Application default screen width in pixels. */
    private static final int DEFAULT_WIDTH = 1280;
    /** Application default screen height in pixels.*/
    private static final int DEFAULT_HEIGHT = 720;
    /** Application default line thickness in pixels. */
    private static final float DEFAULT_LINE_THICKNESS = 10;

    /**
     * Perpendicular distance modifier.
     * <p>
     * This is multiplied by {@code lineThickness} to compute the
     * {@code tolerance} in
     * {@link com.github.glusk2.sprouts.geom.PerpDistSimpl#PerpDistSimpl(
     *     java.util.List,
     *     float
     * )}.
     */
    private static final float PERP_DISTANCE_MODIFIER = 3f;

    /**
     * Minimum distance modifier.
     * <p>
     * This is multiplied by {@code lineThickness} to compute the minimum
     * distance between 2 neighbouring points in {@code sample}.
     */
    private static final float MIN_DISTANCE_MODIFIER =
        2f;

    /**
     * Spline segment count.
     * <p>
     * Defines the number of discrete points on interval: {@code 0 <= t <= 1}
     * for spline rendering.
     */
    private static final int SPLINE_SEGMENT_COUNT = 5;

    /**
     * Circle segment count.
     * <p>
     * Defines the number of points that define the border polyline of circles
     * drawn on screen.
     */
    private static final int CIRCLE_SEGMENT_COUNT = 16;

    /**
     * Coordinates and resolution utility object.
     * <p>
     * Needed for:
     * <ul>
     *   <li> resizing the application window -
     *        {@link Viewport#update(int, int, boolean)}</li>
     *   <li> transforming input coordinates to screen coordinates -
     *        {@link Viewport#unproject(Vector2)}</li>
     * </ul>
     */
    private final Viewport viewport;

    /** The thickness of the lines drawn on screen, measured in pixels. */
    private final float lineThickness;

    /**
     * An object that renders graphics primitives on the screen.
     * <p>
     * An instance should be obtained in {@code create()}, used in
     * {@code render()} and disposed of in {@code dispose()}.
     */
    private ShapeRenderer renderer;

    /** A list of stroke points captured by the mouse or touch screen. */
    private LinkedList<Vector2> sample;

    /** The current combinatorial state of the game. */
    private Graph combState;

    /** The origin of the next move. */
    private Vertex origin;

    /**
     * Creates a new {@code Sprouts} application object with default settings.
     * <p>
     * Equivalent to:
     * <pre>
     * new Sprouts(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_LINE_THICKNESS)
     * </pre>
     *
     * @see #Sprouts(int, int, float)
     */
    public Sprouts() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_LINE_THICKNESS);
    }

    /**
     * Creates a new {@code Sprouts} application object with prefered settings.
     * <p>
     * Equivalent to:
     * <pre>
     * new Sprouts(
     *     new FitViewport(
     *         initialWidth,
     *         initialHeight
     *     ),
     *     lineThickness
     * )
     * </pre>
     *
     * @see #Sprouts(Viewport, float)
     * @param initialWidth the initial screen width of the application,
     *                     measured in pixels
     * @param initialHeight the initial screen height of the application,
     *                      measured in pixels
     * @param lineThickness the thickness of the lines drawn on screen,
     *                      measured in pixels
     */
    public Sprouts(
        final int initialWidth,
        final int initialHeight,
        final float lineThickness
    ) {
        this(
            new FitViewport(
                initialWidth,
                initialHeight
            ),
            lineThickness
        );
    }

    /**
     * Creates a new {@code Sprouts} application object with the specified
     * {@code viewport} and {@code lineThickness}.
     *
     * @param viewport stores screen resolution and provides the utilities to
     *                 convert between screen and input coordinates
     * @param lineThickness the thickness of the lines drawn on screen,
     *                      measured in pixels
     */
    public Sprouts(final Viewport viewport, final float lineThickness) {
        this.viewport = viewport;
        this.lineThickness = lineThickness;
    }


    /** {@inheritDoc} */
    @Override
    public void create() {
        renderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(this);

        combState = new InitialCobweb(
            lineThickness,
            CIRCLE_SEGMENT_COUNT
        );
    }

    /** {@inheritDoc} */
    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height, true);
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setProjectionMatrix(viewport.getCamera().combined);

        if (sample != null) {
            List<Vector2> points =
                new TrimmedPolyline(
                    new CurveApproximation(
                        new BezierCurve(
                            new ArrayList<Vector2>(sample),
                            PERP_DISTANCE_MODIFIER * lineThickness
                        ),
                        SPLINE_SEGMENT_COUNT
                    ),
                    MIN_DISTANCE_MODIFIER * lineThickness
                ).points();

            if (!points.isEmpty()) {
                new RenderedMove(
                    new SubmoveSequence(
                        new SubmoveHead(
                            new SubmoveElement(
                                origin,
                                new Polyline.WrappedList(points),
                                combState,
                                lineThickness * 2
                            )
                        )
                    ),
                    lineThickness,
                    CIRCLE_SEGMENT_COUNT
                ).renderTo(renderer);
            }
        }
        if (combState != null) {
            combState.renderTo(renderer);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void pause() {
    }

    /** {@inheritDoc} */
    @Override
    public void resume() {
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        renderer.dispose();
    }

    /** {@inheritDoc} */
    @Override
    public boolean touchUp(
        final int screenX,
        final int screenY,
        final int pointer,
        final int button
    ) {
        if (sample != null) {
            List<Vector2> points =
                new TrimmedPolyline(
                    new CurveApproximation(
                        new BezierCurve(
                            new ArrayList<Vector2>(sample),
                            PERP_DISTANCE_MODIFIER * lineThickness
                        ),
                        SPLINE_SEGMENT_COUNT
                    ),
                    MIN_DISTANCE_MODIFIER * lineThickness
                ).points();

            if (!points.isEmpty()) {
                Move nextMove =
                    new SubmoveSequence(
                        new SubmoveHead(
                            new SubmoveElement(
                                origin,
                                new Polyline.WrappedList(points),
                                combState,
                                lineThickness * 2
                            )
                        )
                    );
                if (nextMove.isValid() && nextMove.isCompleted()) {
                    combState =
                        new TransformedGraph(
                            new MoveTransformation(
                                nextMove,
                                combState
                            )
                        );
                }
            }
        }
        origin = null;
        sample = null;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean touchDown(
        final int screenX,
        final int screenY,
        final int pointer,
        final int button
    ) {
        Vector2 p =
            viewport.unproject(new Vector2(screenX, screenY));
        for (Vertex v : combState.vertices()) {
            if (v.position().dst(p) < lineThickness) {
                origin = v;
                sample = new LinkedList<Vector2>();
                sample.add(p);
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean touchDragged(
        final int screenX,
        final int screenY,
        final int pointer
    ) {
        Vector2 p = viewport.unproject(new Vector2(screenX, screenY));

        if (
            sample != null
            && !sample.isEmpty()
            && p.dst(sample.get(sample.size() - 1))
            >= MIN_DISTANCE_MODIFIER * lineThickness
        ) {
            sample.add(p);
        }
        return true;
    }
}
