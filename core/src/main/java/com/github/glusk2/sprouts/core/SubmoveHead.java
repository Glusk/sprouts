package com.github.glusk2.sprouts.core;

import java.util.Iterator;

/**
 * A SubmoveHead can be viewed as the <em>head</em> element in a linked list of
 * Submoves that comprise a Move.
 */
public final class SubmoveHead implements Iterator<Submove> {
    /** The head Submove in a sequence of Submoves. */
    private Submove head;

    /**
     * Creates a new SubmoveHead object by specifying the {@code head}.
     *
     * @param head The head Submove in a sequence of Submoves.
     */
    public SubmoveHead(final Submove head) {
        this.head = head;
    }

    /**
     * Always returns {@code true}.
     *
     * @return always returns {@code true}
     */
    @Override
    public boolean hasNext() {
        return true;
    }

    /**
     * The head Submove in a sequence of Submoves.
     *
     * @return the head Submove in a sequence of Submoves
     */
    @Override
    public Submove next() {
        return head;
    }
}
