package com.github.glusk2.sprouts.comb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Infinite collection iterator that starts at the beginning of the collection
 * once it reaches the end.
 *
 * @param <T> type of the collection elements
 */
public final class CircularIterator<T> implements Iterator<T> {
    /** The collection to iterate. */
    private final Collection<T> collection;
    /** Iteration position. */
    private int nextIndex;
    /**
     * If set to {@code true}, {@code nextIndex} will increment before
     * fetching the next element; if {@code false} after.
     */
    private boolean incrementBeforeFetch;

    /**
     * Constructs a new circular iterator from a collection of
     * elements and a given starting position.
     * <p>
     * The position is incremented <em>before</em> every element fetch!
     * <p>
     * This constructor is equivalent to:
     * <pre>
     * new CircularIterator(collection, startIndex, true)
     * </pre>
     *
     * @param collection the collection to iterate
     * @param startIndex iteration starting position
     */
    public CircularIterator(
        final Collection<T> collection,
        final int startIndex
    ) {
        this(collection, startIndex, true);
    }

    /**
     * Constructs a new circular iterator from a collection of
     * elements, a given starting position and specific iteration
     * index increment rule.
     * <p>
     * If {@code incrementBeforeFetch == true}, iteration index will
     * increment <em>before</em> the next element is fetched.
     * <p>
     * If {@code incrementBeforeFetch == false}, iteration index will
     * increment <em>after</em> the next element is fetched.
     *
     * @param collection the collection to iterate
     * @param startIndex iteration starting position
     * @param incrementBeforeFetch if set to {@code true}, the iteration index
     *                             will increment before fetching the next
     *                             element; if {@code false} after
     */
    public CircularIterator(
        final Collection<T> collection,
        final int startIndex,
        final boolean incrementBeforeFetch
    ) {
        this.collection = collection;
        this.nextIndex = startIndex;
        this.incrementBeforeFetch = incrementBeforeFetch;
    }

    /**
     * Returns {@code true} if and only if the collection is not empty.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return !collection.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public T next() {
        List<T> listView = new ArrayList<T>(collection);
        if (incrementBeforeFetch) {
            nextIndex = (nextIndex + 1) % listView.size();
            return listView.get(nextIndex);
        }
        T result = listView.get(nextIndex);
        nextIndex = (nextIndex + 1) % listView.size();
        return result;
    }
}
