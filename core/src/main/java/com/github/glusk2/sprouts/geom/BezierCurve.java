package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve implements Curve<Bezier<Vector2>> {

    private final List<Vector2> sample;
    private final Polyline bSplineControlPoints;

    public BezierCurve(List<Vector2> sample) {
        this(
            sample,
            new BSplineControlPoints(sample)
        );
    }

    public BezierCurve(
        List<Vector2> sample,
        Polyline bSplineControlPoints
    ) {
        this.sample = sample;
        this.bSplineControlPoints = bSplineControlPoints;
    }

    @Override
    public List<Bezier<Vector2>> splines() {
        List<Vector2> s = sample;
        List<Vector2> b = bSplineControlPoints.points();

        List<Bezier<Vector2>> splines = new ArrayList<Bezier<Vector2>>();
        for (int i = 1; i < s.size(); i++) {
            Vector2 p0 = s.get(i-1);
            Vector2 p3 = s.get(i);
            Vector2 dir = b.get(i).cpy().sub(b.get(i-1));
            Vector2[] p = new Vector2[4];
            p[0] = p0.cpy();
            p[1] = b.get(i-1).cpy().mulAdd(dir.cpy(), 1/3f);
            p[2] = b.get(i-1).cpy().mulAdd(dir.cpy(), 2/3f);
            p[3] = p3.cpy();
            splines.add(new Bezier<Vector2>(p));
        }
        return splines;
    }
}
