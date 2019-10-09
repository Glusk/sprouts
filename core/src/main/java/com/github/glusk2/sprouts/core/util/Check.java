package com.github.glusk2.sprouts.core.util;

/** An interface to wrap boolean checks. */
public interface Check {
    /**
     * Returns a boolean evaluation of {@code this} Check.
     *
     * @return {@code true} if {@code this} Check is {@code true},
     *         {@code false} otherwise
     */
    boolean check();
}
