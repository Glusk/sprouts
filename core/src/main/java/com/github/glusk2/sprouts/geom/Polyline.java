package com.github.glusk2.sprouts.geom;

import java.awt.geom.Point2D;
import java.util.List;

public interface Polyline<T extends Point2D> {
    List<T> points();
}
