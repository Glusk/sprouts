package com.github.glusk2.sprouts.comb;

import java.util.List;

/**
 * Represents a graph Vertex - {@code v} and a list of DirectedEdges
 * {@code (a, b)}. All DirectedEdges are virtually connected to the center
 * Vertex (virtual DirectedEdges {@code (v, a)}).
 * <p>
 * Virtual DirectedEdges {@code (v, a)} are ordered by the clockwise order of
 * {@code a}s around {@code v}. The order of virtual DirectedEdges is used to
 * establish {@code edges()}, which returns pairs {@code (v, b)}
 * <p>
 * Next DirectedEdge of {@code (a, b)} is a DirectedEdge {@code (c, d)}.
 * {@code c} comes strictly after {@code a} in the clockwise order of virtual
 * DirectedEdges - {@code (v, a) < (v, c)}. Method {@code next()} is defined
 * as: {@code (v, d) = next(a, b)}
 * <h3>Definitions:</h3>
 * <pre>
 * v ... the center vertex of this LocalRotations
 * a ... the source vertex of a DirectedEdge; virtually connected to v
 * b ... the destination vertex of a DirectedEdge; virtually connected to v
 * c ... the source vertex of the first DirectedEdge after (a, b); virtually
 *       connected to v
 * d ... the destination vertex of the first DirectedEdge after (a, b);
 *       virtually connected to v
 * </pre>
 * <div>
 *   <img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzA1IiBoZWlnaHQ9IjIzMy4wMDAwMDAwMDAwMDAwMyIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KIDwhLS0gQ3JlYXRlZCB3aXRoIE1ldGhvZCBEcmF3IC0gaHR0cDovL2dpdGh1Yi5jb20vZHVvcGl4ZWwvTWV0aG9kLURyYXcvIC0tPgogPGc+CiAgPHRpdGxlPmJhY2tncm91bmQ8L3RpdGxlPgogIDxyZWN0IGZpbGw9IiNmZmYiIGlkPSJjYW52YXNfYmFja2dyb3VuZCIgaGVpZ2h0PSIyMzUiIHdpZHRoPSIzMDciIHk9Ii0xIiB4PSItMSIvPgogIDxnIGRpc3BsYXk9Im5vbmUiIG92ZXJmbG93PSJ2aXNpYmxlIiB5PSIwIiB4PSIwIiBoZWlnaHQ9IjEwMCUiIHdpZHRoPSIxMDAlIiBpZD0iY2FudmFzR3JpZCI+CiAgIDxyZWN0IGZpbGw9InVybCgjZ3JpZHBhdHRlcm4pIiBzdHJva2Utd2lkdGg9IjAiIHk9IjAiIHg9IjAiIGhlaWdodD0iMTAwJSIgd2lkdGg9IjEwMCUiLz4KICA8L2c+CiA8L2c+CiA8Zz4KICA8dGl0bGU+TGF5ZXIgMTwvdGl0bGU+CiAgPHBhdGggaWQ9InN2Z184IiBkPSJtMTEwLjU4ODkxMyw2My43NSIgb3BhY2l0eT0iMC41IiBmaWxsLW9wYWNpdHk9Im51bGwiIHN0cm9rZS1vcGFjaXR5PSJudWxsIiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlPSIjMDAwIiBmaWxsPSIjZmZmIi8+CiAgPGxpbmUgc3Ryb2tlPSIjZmY1NjU2IiBzdHJva2UtbGluZWNhcD0ibnVsbCIgc3Ryb2tlLWxpbmVqb2luPSJudWxsIiBpZD0ic3ZnXzEwIiB5Mj0iNjEuMzU2NTUzIiB4Mj0iMTY5LjI5MzgzMiIgeTE9IjEyNi40Mzg1MjUiIHgxPSIxMTUuOTAwMzg5IiBmaWxsLW9wYWNpdHk9Im51bGwiIHN0cm9rZS13aWR0aD0iMS41IiBmaWxsPSJub25lIi8+CiAgPGxpbmUgc3Ryb2tlPSIjZmY1NjU2IiBzdHJva2UtbGluZWNhcD0ibnVsbCIgc3Ryb2tlLWxpbmVqb2luPSJudWxsIiBpZD0ic3ZnXzEzIiB5Mj0iMTgwLjAxNjYxNiIgeDI9IjE4NS4xMzQyMTgiIHkxPSIxMzcuMjAzODQzIiB4MT0iMTE2LjMyMjMwNCIgZmlsbC1vcGFjaXR5PSJudWxsIiBzdHJva2Utd2lkdGg9IjEuNSIgZmlsbD0ibm9uZSIvPgogIDxwYXRoIGlkPSJzdmdfMTYiIGQ9Im0xODAuNzYwOTUyLDUwLjczODdjMzcuMzc3MDQsLTQ3Ljg2ODg0IDM5LjAxNjM4LC00LjU5MDE3IDU4LjM2MDY0LDI0LjkxODAyYzE5LjM0NDI2LDI5LjUwODE5IDc3LjA0OTE3LC0yNy44Njg4NSA0Ni44ODUyMiwtNjMuOTM0NCIgZmlsbC1vcGFjaXR5PSJudWxsIiBzdHJva2Utb3BhY2l0eT0ibnVsbCIgc3Ryb2tlLXdpZHRoPSIxLjUiIHN0cm9rZT0iIzAwYmYwMCIgZmlsbD0ibm9uZSIvPgogIDxlbGxpcHNlIHJ5PSIxMCIgcng9IjEwIiBpZD0ic3ZnXzIiIGN5PSIxMC43NSIgY3g9IjI4Ni4wODg5MTMiIHN0cm9rZS13aWR0aD0iMS41IiBzdHJva2U9IiMwMDAiIGZpbGw9IiNmZmYiLz4KICA8ZWxsaXBzZSByeT0iMTAiIHJ4PSIxMCIgaWQ9InN2Z181IiBjeT0iNTguNzUiIGN4PSIxNzQuMDg4OTEzIiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlPSIjMDAwIiBmaWxsPSIjZmZmIi8+CiAgPHBhdGggc3Ryb2tlPSIjMDBiZjAwIiBpZD0ic3ZnXzE3IiBkPSJtMTg4LjMxODYwMywxODEuMTc0ODljMjYuNzU1NDksMTUuMjY2NDggMzEuNDEzNTQsMzEuNzEyNSA1OC44NDIwOSw0NS45MTU0NmMyNy40Mjg1NSwxNC4yMDI5NiA1NC40ODI4NCwwLjQyMTI3IDQ3LjAyOTIxLC00NS45NjE0MSIgZmlsbC1vcGFjaXR5PSJudWxsIiBzdHJva2Utb3BhY2l0eT0ibnVsbCIgc3Ryb2tlLXdpZHRoPSIxLjUiIGZpbGw9Im5vbmUiLz4KICA8ZWxsaXBzZSByeT0iMTAiIHJ4PSIxMCIgaWQ9InN2Z18xIiBjeT0iMTc2Ljc1IiBjeD0iMjk0LjA4ODkxMyIgc3Ryb2tlLXdpZHRoPSIxLjUiIHN0cm9rZT0iIzAwMCIgZmlsbD0iI2ZmZiIvPgogIDxlbGxpcHNlIHJ5PSIxMCIgcng9IjEwIiBpZD0ic3ZnXzMiIGN5PSIxODEuNzUiIGN4PSIxODkuMDg4OTEzIiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlPSIjMDAwIiBmaWxsPSIjZmZmIi8+CiAgPHRleHQgeG1sOnNwYWNlPSJwcmVzZXJ2ZSIgdGV4dC1hbmNob3I9InN0YXJ0IiBmb250LWZhbWlseT0iJ0NvdXJpZXIgTmV3JywgQ291cmllciwgbW9ub3NwYWNlIiBmb250LXNpemU9IjEwIiBpZD0ic3ZnXzE4IiB5PSIxMzcuNjIzOTE5IiB4PSIxMDYuOTc5Mzc1IiBmaWxsLW9wYWNpdHk9Im51bGwiIHN0cm9rZS1vcGFjaXR5PSJudWxsIiBzdHJva2Utd2lkdGg9IjAiIHN0cm9rZT0iIzAwYmYwMCIgZmlsbD0iIzAwMDAwMCI+djwvdGV4dD4KICA8dGV4dCB4bWw6c3BhY2U9InByZXNlcnZlIiB0ZXh0LWFuY2hvcj0ic3RhcnQiIGZvbnQtZmFtaWx5PSInQ291cmllciBOZXcnLCBDb3VyaWVyLCBtb25vc3BhY2UiIGZvbnQtc2l6ZT0iMTAiIGlkPSJzdmdfMTkiIHk9IjYwLjcxOTMzNCIgeD0iMTcwLjU4MDU5MyIgZmlsbC1vcGFjaXR5PSJudWxsIiBzdHJva2Utb3BhY2l0eT0ibnVsbCIgc3Ryb2tlLXdpZHRoPSIwIiBzdHJva2U9IiMwMDAiIGZpbGw9IiMwMDAwMDAiPmE8L3RleHQ+CiAgPHRleHQgeG1sOnNwYWNlPSJwcmVzZXJ2ZSIgdGV4dC1hbmNob3I9InN0YXJ0IiBmb250LWZhbWlseT0iJ0NvdXJpZXIgTmV3JywgQ291cmllciwgbW9ub3NwYWNlIiBmb250LXNpemU9IjEwIiBpZD0ic3ZnXzIwIiB5PSIxMy4yNjE2ODIiIHg9IjI4My40NjIwMDgiIGZpbGwtb3BhY2l0eT0ibnVsbCIgc3Ryb2tlLW9wYWNpdHk9Im51bGwiIHN0cm9rZS13aWR0aD0iMCIgc3Ryb2tlPSIjMDAwIiBmaWxsPSIjMDAwMDAwIj5iPC90ZXh0PgogIDx0ZXh0IHhtbDpzcGFjZT0icHJlc2VydmUiIHRleHQtYW5jaG9yPSJzdGFydCIgZm9udC1mYW1pbHk9IidDb3VyaWVyIE5ldycsIENvdXJpZXIsIG1vbm9zcGFjZSIgZm9udC1zaXplPSIxMCIgaWQ9InN2Z18yMSIgeT0iMTg0LjEwOTIyOCIgeD0iMTg1LjgzNDgzOCIgZmlsbC1vcGFjaXR5PSJudWxsIiBzdHJva2Utb3BhY2l0eT0ibnVsbCIgc3Ryb2tlLXdpZHRoPSIwIiBzdHJva2U9IiMwMDAiIGZpbGw9IiMwMDAwMDAiPmM8L3RleHQ+CiAgPHRleHQgeG1sOnNwYWNlPSJwcmVzZXJ2ZSIgdGV4dC1hbmNob3I9InN0YXJ0IiBmb250LWZhbWlseT0iJ0NvdXJpZXIgTmV3JywgQ291cmllciwgbW9ub3NwYWNlIiBmb250LXNpemU9IjEwIiBpZD0ic3ZnXzIyIiB5PSIxNzkuNzAyNDQ2IiB4PSIyOTEuMjU4NjIyIiBmaWxsLW9wYWNpdHk9Im51bGwiIHN0cm9rZS1vcGFjaXR5PSJudWxsIiBzdHJva2Utd2lkdGg9IjAiIHN0cm9rZT0iIzAwMCIgZmlsbD0iIzAwMDAwMCI+ZDwvdGV4dD4KICA8bGluZSBzdHJva2UtZGFzaGFycmF5PSIyLDIiIHN0cm9rZS1saW5lY2FwPSJudWxsIiBzdHJva2UtbGluZWpvaW49Im51bGwiIGlkPSJzdmdfMjYiIHkyPSI3Mi45MjI3MyIgeDI9IjAuNzQ5OTk3IiB5MT0iMTMxLjIyNzg0NSIgeDE9IjEwMy40NjE5MTUiIGZpbGwtb3BhY2l0eT0ibnVsbCIgc3Ryb2tlLXdpZHRoPSIxLjUiIHN0cm9rZT0iI2ZmMDAwMCIgZmlsbD0ibm9uZSIvPgogIDxsaW5lIHN0cm9rZS1kYXNoYXJyYXk9IjIsMiIgc3Ryb2tlLWxpbmVjYXA9Im51bGwiIHN0cm9rZS1saW5lam9pbj0ibnVsbCIgaWQ9InN2Z18yNyIgeTI9IjE5Ni45OTA1OTEiIHgyPSI1MS45MzY0NjQiIHkxPSIxNDEuMDU4MzU4IiB4MT0iMTA1LjgzNDc5NyIgZmlsbC1vcGFjaXR5PSJudWxsIiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlPSIjZmYwMDAwIiBmaWxsPSJub25lIi8+CiAgPGVsbGlwc2Ugcnk9IjEwIiByeD0iMTAiIGlkPSJzdmdfNiIgY3k9IjEzNC40MjIxMzEiIGN4PSIxMTAuMDg4OTEzIiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlPSIjMDAwIiBmaWxsPSIjZmZmIi8+CiAgPHRleHQgc3Ryb2tlPSIjMDAwIiB4bWw6c3BhY2U9InByZXNlcnZlIiB0ZXh0LWFuY2hvcj0ic3RhcnQiIGZvbnQtZmFtaWx5PSInQ291cmllciBOZXcnLCBDb3VyaWVyLCBtb25vc3BhY2UiIGZvbnQtc2l6ZT0iMTAiIGlkPSJzdmdfMjgiIHk9IjEzNi45OTA1NTQiIHg9IjEwNi41MTI3NjYiIGZpbGwtb3BhY2l0eT0ibnVsbCIgc3Ryb2tlLW9wYWNpdHk9Im51bGwiIHN0cm9rZS13aWR0aD0iMCIgZmlsbD0iIzAwMDAwMCI+djwvdGV4dD4KIDwvZz4KPC9zdmc+"
 *        alt="Local Rotations"
 *        style="display:block;margin:auto;max-width:800px;width:100%">
 * </div>
 */
public interface LocalRotations {
    /**
     * Returns DirectedEdges {@code (v, b)}.
     * <p>
     * Refer to <strong>Definitions</strong> in the interface definition Doc
     * for more info.
     *
     * @return a list of DirectedEdges {@code (v, b)}
     */
    List<DirectedEdge> edges();

    /**
     * Returns the first DirectedEdge after {@code current} in {@code this}
     * LocalRotations.
     * <p>
     * {@code current} need not be a part of {@code this} LocalRotations.
     * <p>
     * Refer to <strong>Definitions</strong> in the interface definition Doc
     * for more info.
     *
     * @param current DirectedEdge {@code (a, b)}
     * @return a new DirectedEdge {@code (v, d)}
     */
    DirectedEdge next(DirectedEdge current);

    /**
     * Returns new LocalRotations with an {@code additionalEdge}.
     * <p>
     * Refer to <strong>Definitions</strong> in the interface definition Doc
     * for more info.
     *
     * @param additionalEdge the DirectedEdge {@code (a, b)} to add
     * @return new LocalRotations with an {@code additionalEdge}
     */
    LocalRotations with(DirectedEdge additionalEdge);

    /**
     * Returns new LocalRotations without the {@code surplusEdge}.
     * <p>
     * Refer to <strong>Definitions</strong> in the interface definition Doc
     * for more info.
     *
     * @param surplusEdge the DirectedEdge {@code (a, b)} to remove
     * @return new LocalRotations without the {@code surplusEdge}
     */
    LocalRotations without(DirectedEdge surplusEdge);
}
