package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * A Perpendicular Distance Polyline Simplification.
 * */
public class PerpDistSimpl implements Polyline {

    private static final double DEFAULT_DELTA = .5;

    private final List<Vector2> original;
    private final double delta;

    public PerpDistSimpl(List<Vector2> original) {
        this(original, DEFAULT_DELTA);
    }

    public PerpDistSimpl(List<Vector2> original, double delta) {
        this.original = original;
        this.delta = delta;
    }

    /**
     *          * x
     *         /|
     *        / |
     *   a   /  | e
     *      /   |
     *     /    |
     *    /_____|_____
     *   *   p        *
     *          b
     *   p0           p1
     */
    private static double perpDistance(Vector2 p0, Vector2 p1, Vector2 x) {
        Vector2 a = x.cpy().sub(p0);
        Vector2 b = p1.cpy().sub(p0);
        Vector2 p = b.scl(a.dot(b) / b.dot(b));
        Vector2 e = a.sub(p);
        return e.len();
    }

    @Override
    public List<Vector2> points() {
        if (original.size() < 3) {
            return original;
        }
        List<Vector2> simplified = new ArrayList<Vector2>();
        for (int i = 0; i < original.size() - 2; i++) {
            simplified.add(original.get(i));

            Vector2 p0 = original.get(i);
            Vector2 p1 = original.get(i+2);
            Vector2 x = original.get(i+1);
            if (perpDistance(p0, p1, x) < delta) {
                i++;
            }
        }
        simplified.add(original.get(original.size() - 1));
        System.out.println(simplified.size() + " " + original.size());
        return simplified;
    }
}
