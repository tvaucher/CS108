package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * An open polyline is a polyiline where the last point is not connected
 * to the first one.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class OpenPolyLine extends PolyLine {
    /**
     * Construct an open polyline from a list of points.
     * 
     * @param points
     *          A list of points.
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }
    
    /* (non-Javadoc)
     * @see ch.epfl.imhof.geometry.PolyLine#isClosed()
     */
    public boolean isClosed() {
        return false;
    }
}
