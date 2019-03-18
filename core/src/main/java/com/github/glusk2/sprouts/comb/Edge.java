package com.github.glusk2.sprouts.comb;

/** Read-only, struct-like object representing a graph edge. */
public final class Edge {
    /** Edge source vertex. */
    private Vertex from;
    /** Edge destination vertex. */
    private Vertex to;

    /**
     * Constructs a new directed edge from source and destination vertices.
     *
     * @param from edge source vertex
     * @param to edge destination vertex
     */
    public Edge(final Vertex from, final Vertex to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Getter for source vertex.
     *
     * @return source vertex
     */
    public Vertex from() {
        return from;
    }

    /**
     * Getter for destination vertex.
     *
     * @return destination vertex
     */
    public Vertex to() {
        return to;
    }

    /**
     * Returns a new edge that is directly opposite to {@code this} -
     * the source and destination vertices are swapped.
     * <p>
     * The method returns exactly this:
     * <pre>
     * new Edge(to, from)
     * </pre>
     *
     * @return the reversed edge
     */
    public Edge reversed() {
        return new Edge(to, from);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(from.label());
        sb.append(", ");
        sb.append(to.label());
        sb.append(")");
        return sb.toString();
    }

    /**
     * Glues source and destination labels to a string and returns it's hash.
     * <p>
     * More formally (pseudocode), the method returns:
     * <pre>
     * // | ... string concatenation
     * String arg = &lt;source_label&gt; | "-" | &lt;destination_label&gt;
     * return arg.hashCode();
     * </pre>
     * <p>
     *
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (from.label() + "-" + to.label()).hashCode();
    }

    /**
     * Compares {@code this} and {@code obj} source and destination labels.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Edge)) {
            return false;
        }
        Edge that = (Edge) obj;
        return
            this.from.label() == that.from.label()
         && this.to.label() == that.to.label();
    }
}
