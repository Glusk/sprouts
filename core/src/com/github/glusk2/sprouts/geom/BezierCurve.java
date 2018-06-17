package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BezierCurve implements Curve<Bezier<Vector2>> {

    private final List<Point2D> sample;
    private final Polyline<Point2D> bSplineControlPoints;

    public BezierCurve(List<Point2D> sample) {
        this(
            sample,
            new BSplineControlPoints(sample)
        );
    }

    public BezierCurve(
        List<Point2D> sample,
        Polyline<Point2D> bSplineControlPoints
    ) {
        this.sample = sample;
        this.bSplineControlPoints = bSplineControlPoints;
    }

    @Override
    public List<Bezier<Vector2>> splines() {
        List<Vector2> s = new ArrayList<Vector2>(sample.size());
        List<Vector2> b = new ArrayList<Vector2>(bSplineControlPoints.points().size());
        for (Point2D p : sample) {
            s.add(new Vector2((float) p.getX(), (float) p.getY()));
        }
        for (Point2D p : bSplineControlPoints.points()) {
            b.add(new Vector2((float) p.getX(), (float) p.getY()));
        }

        List<Bezier<Vector2>> splines = new ArrayList<Bezier<Vector2>>();
        for (int i = 1; i < s.size(); i++) {
            Vector2 p0 = s.get(i-1);
            Vector2 p3 = s.get(i);
            Vector2 dir = b.get(i).cpy().sub(b.get(i-1));
            Vector2[] p = new Vector2[4];
            p[0] = p0.cpy();
            p[1] = p0.cpy().mulAdd(dir.cpy(), 1/3f);
            p[2] = p0.cpy().mulAdd(dir.cpy(), 2/3f);
            p[3] = p3.cpy();
            splines.add(new Bezier<Vector2>(p));
        }
        return splines;
    }
}
