package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.util.Objects.requireNonNull;

/**
 * A Polygon is a closed polyline that may or may not contain holes.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class Polygon {
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;

    /**
     * Constructs a polygon with holes from a closed polyline (the shell) and a
     * list of closed polylines (the holes). Ideally, we'd check that the holes
     * indeed are inside the shell, but this is complicated and not strictly
     * necessary for this project (we're going to assume that the data from
     * OpenStreetMaps is correct).
     * 
     * @param shell
     *            A closed polyline that marks the exterior of the polygon.
     * @param holes
     *            A list of closed polylines contained in the holes (but it
     *            won't be verified -- @see ch.epfl.imhof.Polygon).
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell; // The shell is already immutable, so there's no
                            // need for deep copies.
        this.holes = Collections
                .unmodifiableList(new ArrayList<ClosedPolyLine>(
                        requireNonNull(holes)));
    }

    /**
     * Constructs a polygon without holes from a closed polyline (the shell).
     * 
     * @param shell
     *            A closed polyline that marks the exterior of the polygon.
     */
    public Polygon(ClosedPolyLine shell) {
        this(shell, Collections.emptyList()); // invoke the other constructor
                                              // with an explicit empty List
    }

    /**
     * Returns the shell of the polygon.
     * 
     * @return shell The shell, the exterior of the polygon
     */
    public ClosedPolyLine shell() {
        return shell;
    }

    /**
     * Returns a list of the holes of the polygon
     * 
     * @return holes A list of holes in the polygon.
     */
    public List<ClosedPolyLine> holes() {
        return holes; // holes is unmodifiable, so it's OK to return it like
                      // this.
    }
}
