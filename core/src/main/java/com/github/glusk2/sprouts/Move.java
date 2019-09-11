package com.github.glusk2.sprouts;

/**
 * A Sprouts Move consists of one or more Submoves.
 * <p>
 * The iteration of Submoves is non-standard, that is it can't be used as a
 * for-each loop construct. The intended iteration procedure is as follows:
 * <pre>
 * // Move move = ...
 * Iterator&lt;Submove&gt; it = move.iterator();
 * while (it.hasNext()) {
 *     Submove next = it.next();
 *     // do work with "next"
 *     it = next;
 * }
 * </pre>
 */
public interface Move extends Iterable<Submove> {
    /**
     * Checks if all the Submoves are completed.
     * <p>
     * See {@link Submove#isCompleted()} for more info.
     *
     * @return {@code true} if all the Submoves are completed
     */
    boolean isCompleted();

    /**
     * Checks if all the Submoves are valid.
     * <p>
     * See {@link Submove#isValid()} for more info.
     *
     * @return {@code true} if all the Submoves are valid
     */
    boolean isValid();
}
