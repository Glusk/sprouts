package com.github.glusk2.sprouts.geom;

import com.badlogic.gdx.math.Path;

import java.util.List;

public interface Curve<T extends Path> {
    List<T> splines();
}
