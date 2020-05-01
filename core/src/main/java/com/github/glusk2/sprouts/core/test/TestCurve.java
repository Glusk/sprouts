package com.github.glusk2.sprouts.core.test;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.glusk2.sprouts.core.comb.PolylineIntersectionSearch;
import com.github.glusk2.sprouts.core.comb.Vertex;
import com.github.glusk2.sprouts.core.geom.BezierCurve;
import com.github.glusk2.sprouts.core.geom.CurveApproximation;
import com.github.glusk2.sprouts.core.geom.Polyline;

/** A standalone application to test curve sampling and smoothing. */
public final class TestCurve extends InputAdapter
                             implements ApplicationListener {
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
     * This is multiplied by {@code CURVE_THICKNESS} to compute the
     * {@code tolerance} in
     * {@link com.github.glusk2.sprouts.core.geom.PerpDistSimpl#PerpDistSimpl(
     *     java.util.List,
     *     float
     * )}.
     */
    private static final float PERP_DISTANCE_MODIFIER = 3f;
    /** The thickness of the curve drawn. */
    private static final float CURVE_THICKNESS = 10f;

    /** Application window width. */
    private final int windowWidth;
    /** Application window height. */
    private final int windowHeight;

    /** Touch or mouse drag sample points. */
    private ArrayList<Vector2> sample = new ArrayList<Vector2>();
    /** Render object to draw with. */
    private ShapeRenderer renderer;
    /** An object used to project touch coordinates to screen coordinates. */
    private Camera camera;

    /**
     * Creates a new TestCurve application with specified screen resolution.
     *
     * @param windowWidth application window width
     * @param windowHeight application window width
     */
    public TestCurve(final int windowWidth, final int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    @Override
    public void create() {
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera(windowWidth, windowHeight);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(final int width, final int height) {
    }

    /**
     * Projects {@code x} and {@code y} input coordinates and returns
     * the position in screen coordinates.
     *
     * @param x touch or mouse {@code x} coordinate
     * @param y touch or mouse {@code y} coordinate
     * @return a new {@code Vector2} with {@code x} and {@code y}
     *         projected to screen coordinates
     */
    private Vector2 unproject(final int x, final int y) {
        Vector3 tmp = camera.unproject(new Vector3(x, y, 0));
        return new Vector2(tmp.x, tmp.y);
    }

    @Override
    public boolean touchDown(
        final int screenX,
        final int screenY,
        final int pointer,
        final int button
    ) {
        sample.add(unproject(screenX, screenY));
        return true;
    }

    @Override
    public boolean touchDragged(
        final int screenX,
        final int screenY,
        final int pointer
    ) {
        Vector2 candidate = unproject(screenX, screenY);
        if (
            candidate.dst(sample.get(sample.size() - 1)) > 2 * CURVE_THICKNESS
        ) {
            sample.add(candidate);
        }
        return true;
    }

    @Override
    public boolean touchUp(
        final int screenX,
        final int screenY,
        final int pointer,
        final int button
    ) {
        sample = new ArrayList<Vector2>();
        return true;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        List<Vector2> points =
            new CurveApproximation(
                new BezierCurve(
                    new ArrayList<Vector2>(sample),
                    PERP_DISTANCE_MODIFIER * CURVE_THICKNESS
                ),
                SPLINE_SEGMENT_COUNT
            ).points();

        for(int j = 0; j < 4; j++) {
            for (int i = 1; i < points.size(); i++) {
                Vertex crossPoint =
                    new PolylineIntersectionSearch(
                        points.get(i - 1),
                        points.get(i),
                        new Polyline.WrappedList(points.subList(0, i)),
                        Color.BLACK
                    ).result();
            }
        }

        renderer.begin(ShapeType.Filled);
        renderer.setProjectionMatrix(camera.combined);
        for (int i = 1; i < points.size(); i++) {
            Vector2 p0 = points.get(i - 1);
            Vector2 p1 = points.get(i);

            renderer.setColor(Color.BLACK);
            renderer.rectLine(p0, p1, CURVE_THICKNESS);
            renderer.setColor(Color.PURPLE);
            renderer.circle(p0.x, p0.y, CURVE_THICKNESS / 2);
            renderer.circle(p1.x, p1.y, CURVE_THICKNESS / 2);
        }
        renderer.end();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
