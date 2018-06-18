package com.github.glusk2.sprouts;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.glusk2.sprouts.geom.BSplineControlPoints;
import com.github.glusk2.sprouts.geom.BezierCurve;
import com.github.glusk2.sprouts.geom.CachedCurve;
import com.github.glusk2.sprouts.geom.Curve;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sprouts extends InputAdapter implements ApplicationListener {

    private final Viewport viewport;

    private ShapeRenderer shapes;
    List<Point2D> sample;
    private Curve<Bezier<Vector2>> curve;

    private List<Vector3> sampledDragPoints;

    public Sprouts() {
        this(1280, 720);
    }

    public Sprouts(int minWidth, int minHeight) {
        this(
            new ExtendViewport(
                minWidth / 32f,
                minHeight / 32f,
                new OrthographicCamera()
            )
        );
    }

    public Sprouts(Viewport viewport) {
        this.viewport = viewport;
    }


    @Override
    public void create() {
        shapes = new ShapeRenderer();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (curve != null) {
            Vector2 nextVal = new Vector2();

            shapes.setProjectionMatrix(viewport.getCamera().combined);
            shapes.begin(ShapeRenderer.ShapeType.Filled);
            for (Bezier<Vector2> spline : curve.splines()) {
                shapes.setColor(Color.BLACK);
                for (int i = 1; i <= 100; i++) {
                    float val = i / 100f;
                    spline.valueAt(nextVal, val);
                    shapes.circle(nextVal.x, nextVal.y, 0.125f, 16);
                }
            }
            shapes.end();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        shapes.dispose();
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        curve = new CachedCurve(curve);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 nextPoint =
            viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
        sample = new ArrayList<Point2D>();
        sample.add(
            new Point2D.Double(
                nextPoint.x,
                nextPoint.y
            )
        );
        curve = new BezierCurve(sample);
        return true;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        Vector3 nextPoint =
            viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
        Point2D p = new Point2D.Double(nextPoint.x, nextPoint.y);

        if (!sample.isEmpty() && p.distance(sample.get(sample.size() - 1)) >= 0.75f) {
            sample.add(p);
        }
        return true;
    }
}
