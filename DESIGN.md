# Design Doc

The goal of this project is to implement a program for playing
Sprouts. The program should:
-   support multiple platforms
-   try to smooth out any curve that a player draws
-   detect and prevent invalid moves
-   detect when the game has ended (detect whether there are any valid moves
    left to draw)

## Framework choice

LibGDX framework was chosen because it is a cross-platform game development
framework and because I (@Glusk) was most familiar with the Java programming
language. Alternative frameworks may include: Unity, Unreal Engine,... They
all seemed too feature-rich for a simple 2D drawing game.

## Curve drawing

The program shall sample a touch or a mouse pointer as the user is dragging
to connect two sprouts with a curve. Sampling and smoothing shall occur in
stages:
1.   All points along the drag gesture will be stored into a sample list, such
     that the last point in the sample is always a minimum distance apart from
     the previous one in the list. This can be thought of as some form of a
     [radial distance simplification](http://psimpl.sourceforge.net/radial-distance.html).
2.   Next, the sample will be further simplified using the
     [perpendicular distance simplification](http://psimpl.sourceforge.net/perpendicular-distance.html).
3.   The simplified sample from (3.) will be used to construct B-spline
     *control points* (or *a control polygon*) as described
     [here](https://www.math.ucla.edu/~baker/149.1.02w/handouts/dd_splines.pdf).
4.   Once B-spline control points are computed we can produce smoothly glued
     cubic Bézier splines.
5.   The curve produced in (4.) shall be cut into small pieces and stored
     as a polyline approximation.

The above stages occur every time a drag is detected by the program. Sample
from (1.) is created only once per stroke but all the other stages (2.-5.)
repeat as more stroke points are sampled. This is inefficient. As the curve is
being drawn, smoothening and approximation only needs to occur on a sublist
near the end of the sample. But that leads to other issues, such as merging the
already smoothened and approximated part of the curve with the updated end.

Time complexities of the simplification algorithms are `O(n)` where `n` is
the sample size. Using the Thomas Algorithm, the computation of the B-spline
control points is also linear. Constructing and approximating the Bézier curve
increases the total point count by a factor of `SPLINE_SEGMENT_COUNT` - the
number of segments with which each Bézier spline is approximated. Thus, a
player's stroke can be sampled, smoothened, approximated and drawn onto the
screen in linear time.

This is how a curve produced in this manner looks like. Purple points are the
result of (5.), connected together by straight black line segments.

![Sample Curve](resources/InterpolatedCurve.png)

The test application can be found here:
[`TestCurve.java`](core/src/main/java/com/github/glusk2/sprouts/core/test/TestCurve.java)

## Game modeling

The most natural way to represent a game position is to use a graph, having
sprouts as its vertices and the curves that a player draws as its edges.

We want to have a [*connected*](https://en.wikipedia.org/wiki/Connectivity_(graph_theory))
graph - a graph with a single [*component*](https://en.wikipedia.org/wiki/Component_(graph_theory)).
That way we can differentiate between the combinatorially different moves.

### Cobweb

![Moves With And Without Cobweb](resources/MovesWithAndWithoutCobweb.png)

Consider 2 moves with the same endpoints (A1 and B1). We have no mechanism to
distinguish between the two moves.

Because sprouts are not connected at the start of the game, we introduce
"dummy" edges, called *the cobweb* (A2 and B2). Such edges may be intersected
by the moves that the players draw. By convention, we will mark cobweb edges
and vertices red. We now have a connected structure the entire time and we can
easily discriminate between the moves that start and end in the same sprout.

### Representation of a position

We can consider two different representation of the game position:
**geometric** and **combinatorial**.

A geometric representation deals with geometry - the position of the points,
the list of points that constitute a polyline.

A combinatorial representation is a more abstract view of the game. It deals
with the graph that represents the game position.

#### Vertices
| Label | Position | Color |
|---|---|---|
| `v1` | `(x1, y1)` | black |
| `v2` | `(x2, y2)` | black |
| `v3` | `(x3, y3)` | black |

A simple design like this will not work:
```java
Set<Vertex> vertices;

public final class Vertex {
    public Vertex(String label, Vector2 position, Color color) {
        this.label = label;
        this.position = position;
        this.color = color;
    }
    /* Getters */
    public String label();
    public Vector2 position();
    public Color color();

    public int hashCode() {
        return label().hashCode();
    }
    public boolean equals(Object that) {
        return this.label().equals(that.label());
    }
}
```
If we want to update the color or the position of the vertex we have to either
make `Vertex` mutable or locate and update all occurrences of the old `Vertex`
object with the new `Vertex` object that contains the updated values. Both
options seem hacky. 

A decoupling of a vertex from its attributes is needed:

``` java
Map<String, VertexAttributes> vertices;

public final class VertexAttributes {
    public final Vector2 position;
    public final Color color;

    public VertexAttributes(Vector2 position, Color color) {
        this.position = position;
        this.color = color;
    }
}
```

#### Edges
| Endpoints | Polyline | Color |
|---|---|---|
| `v1,v2` | `(x1, y1), ..., (x2, y2)` | black |
| `v2,v3` | `(x2, y2), ..., (x3, y3)` | black |

Possible design:
``` java
Map<Endpoints, EdgeAttributes> edges;

public final class Endpoints {
    public Endpoints(String v1, String v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
    boolean equals(Object that);
    int hashCode();
}
public final class EdgeAttributes {
    // Should be unmodifiable
    public final LinkedList<Vector2> polyline;
    public final Color color;

    public EdgeAttributes(LinkedList<Vector2> polyline, Color color) {
        this.polyline = polyline;
        this.color = color;
    }
}
```

At some point during the game, we can detect 2 connections with the same
endpoints but different polylines (a cobweb edge and a player-drawn curve).

![EqualEndpoints](resources/EqualEndpoints.png)

As a rule, the red edge always needs to be removed in such situations. This
simplifies the combinatorial representation and the overall design (because
we can ensure that the endpoints point to exactly one edge). To test 2 edges
for equality we only need to compare the endpoint vertices and not the
polylines!

A concept of a *directed edge* is also needed:

``` java
public final class DirectedEdge {
    public DirectedEdge(
        String from,
        String to,
        Map<Endpoints, EdgeAttributes> edges,
        Map<String, VertexAttributes> vertices
    ) {
        this(
            from,
            to,
            edges.get(new Endpoints(from, to)),
            vertices.get(from),
            vertices.get(to)
        );
    }
    public DirectedEdge(
        String from,
        String to,
        EdgeAttributes edge,
        VertexAttributes fromAttr,
        VertexAttributes toAttr
    ) {
        this(from, to, new DirectedPath(fromAttr, toAttr))
    }
    public DirectedEdge(String from, String to, Iterable<Vector2> path) {
        this.from = from;
        this.to = to;
        this.path = path;
    }
    public String from();
    public String to();
    public Iterable<Vector2> path();
    public DirectedEdge rev() {
        return new DirectedEdge(to, from, edges, vertices);
    }
}

public final class DirectedPath implements Iterable<Vector2> {
    public DirectedPath(
        VertexAttributes from,
        VertexAttributes to,
        EdgeAttributes edge
    ) {
       this.from = from;
       this.to = to;
       this.edge = edge;
    }
    public Iterator<Vector2> iterator() {
        if (
            from.position.equals(edge.polyline.getFirst()) &&
            to.position.equals(edge.polyline.getLast())
        ) {
            return edge.polyline.iterator();
        }
        else if (
            to.position.equals(edge.polyline.getFirst()) &&
            from.position.equals(edge.polyline.getLast())
        ) {
            return edge.polyline.descendingIterator();
        }
        throw new IllegalArgumentException();
    }
 }
```

The use of `LinkedList` seemed right because of `getFirst()`, `getLast()` and
`descendingIterator()` methods. These methods come in handy when figuring out
the polyline direction in relation to its endpoints. Besides, we don't need
random access.

#### Local Rotations
```
v1: v2
v2: v1, v3
v3: v2
```

Possible design:

``` java
Map<String, Set<String>> adjacencyList;
Map<Endpoints, EdgeAttributes> edges;
Map<String, VertexAttributes> vertices;
public final class LocalRotations {
    public LocalRotations(adjacencyList, edges, vertices) {}

    public DirectedEdge next(DirectedEdge fi) {
        String v1 = fi.from();
        // generate DirectedEdges for adjacencyList.get(v1);
        List<DirectedEdge> lr = new ArrayList<DirectedEdge>();
        for (String v2 : adjacencyList.get(v1)) {
            lr.add(new DirectedEdge(v1, v2, edges, vertices));
        }
        // add fi to the list (only if not present)
        if (!adjacencyList.get(v1).contains(fi.to())) {
            lr.add(fi);
        }
        // sort the list
        // find the next DirectedEdge after fi and return it
    }
}
```

#### Faces

| Boundary |
|---|
| `v1, v2, v3` |
| `v2, v4` |

A graph face is uniquely defined by the ordered sequence of its boundary vertices.

We need to be able to:
- 1. check whether a cobweb edge is located in two faces
- 2. query a face by on one of its boundary arrows (DirectedEdges)

``` java
public class Face {
    public Face(LinkedList<String> vertexBoundary) {
    }
    public boolean contains(DirectedEdge arrow);
    public boolean contains(Endpoints edge);
}
```

## Reference

- [LibGDX | Guidelines | Performance considerations](https://libgdx.badlogicgames.com/documentation/hacking/Contributing.html#performance-considerations)
