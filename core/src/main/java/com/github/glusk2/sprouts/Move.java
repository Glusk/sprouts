package com.github.glusk2.sprouts;

import java.util.List;

import com.github.glusk2.sprouts.comb.Graph;

/** A Sprouts Move consists of one or more Submoves. */
public interface Move extends Drawable {
    /**
     * Returns an ordered list ov {@code this} Move's Submoves.
     *
     * @return an ordered list ov {@code this} Move's Submoves
     */
    List<Submove> submoves();

    /**
     * Returns a new state after {@code this} Move is drawn.
     *
     * @return a new state after {@code this} Move is drawn
     */
    Graph updatedState();

    /**
     * Checks if the last Submove ends in a sprout.
     *
     * @return {@code true} if the last Submove ends in a sprout
     */
    boolean isCompleted();

    /**
     * Checks if all the Submoves are valid.
     *
     * @return {@code true} if all the Submoves are valid
     */
    boolean isValid();
}
