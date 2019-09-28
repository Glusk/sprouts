package com.github.glusk2.sprouts.core;

import java.util.Iterator;

/** An implementation of the Move interface as a sequence of Submoves. */
public final class SubmoveSequence implements Move {
    /** The "head" of {@code this} SubmoveSequence. */
    private final Iterator<Submove> head;

    /**
     * Constructs a new SubmoveSequence by providing the {@code head}.
     * <p>
     * The sequence is thought of as a linked list, "head" being its first
     * element.
     *
     * @param head the "head" of {@code this} SubmoveSequence
     */
    public SubmoveSequence(final Iterator<Submove> head) {
        this.head = head;
    }

    @Override
    public Iterator<Submove> iterator() {
        return head;
    }

    @Override
    public boolean isCompleted() {
        Iterator<Submove> it = iterator();
        while (it.hasNext()) {
            Submove submove = it.next();
            if (!submove.isCompleted()) {
                return false;
            }
            it = submove;
        }
        return true;
    }

    @Override
    public boolean isValid() {
        Iterator<Submove> it = iterator();
        while (it.hasNext()) {
            Submove submove = it.next();
            if (!submove.isValid()) {
                return false;
            }
            it = submove;
        }
        return true;
    }
}
