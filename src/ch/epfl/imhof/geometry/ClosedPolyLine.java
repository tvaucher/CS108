package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * A closed polyline is a polyiline where the last point is connected
 * to the first one.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class ClosedPolyLine extends PolyLine{
    private final int pointSize;
    /**
     * Construct a closed polyline from a list of points.
     * 
     * @param points
     *          A list of points (the last one shouldn't necessarily be
     *          the same as the first one -- they will be linked up to
     *          form a closed shape).
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
        pointSize = points.size();
    }
    
    /* (non-Javadoc)
     * @see ch.epfl.imhof.geometry.PolyLine#isClosed()
     */
    @Override
    public boolean isClosed() {
        return true;
    }
    
    /**
     * Return the signed area of the closed polyline.
     * 
     * @return area
     *          The signed area of the closed polyline.
     */
    public double area() {
        double twiceSignedArea = 0.; //2 times the signed area
        for (int i = 0; i < pointSize; ++i) {
            twiceSignedArea += points().get(i).x() *
                    (points().get(getGeneralisedIndex(i+1)).y()
                    - points().get(getGeneralisedIndex(i-1)).y());
        }
        return .5 * Math.abs(twiceSignedArea);
    }
    
    /**
     * Return whether the closed polyline containts a point
     * 
     * @param p
     *          The point to be checked
     * @return true or false
     *          True if the point is in the closed polyline,
     *          false if it isn't in the closed polyline.
     * 
     */
    public boolean containsPoint(Point p) {
        int count = 0;
        for (int i = 0; i < pointSize; ++i) {
            Point p1 = points().get(i);
            Point p2 = points().get(getGeneralisedIndex(i+1));
            if (p1.y() <= p.y()) {
                if (p2.y() > p.y() && isLeftFromLine(p, p1, p2)) {
                    ++count;
                }
            }
            else {
                if (p2.y() <= p.y() && isLeftFromLine(p, p2, p1)) {
                    --count;
                }
            }
        }
        return (count != 0);
    }
    
    /**
     * This private method returns the index that should be used to get a point
     * in order to prevent ArrayOutOfBoundExceptions.
     * 
     * @param index
     * @return
     */
    private int getGeneralisedIndex(int index) {
        return Math.floorMod(index, pointSize);
    }
    
    /**
     * Return whether the Point p is to the left of the line formed by the points
     * p1 or p2.
     * 
     * @param p
     *          The point that we want to know about.
     * @param p1
     *          One point of the line
     * @param p2
     *          Another point of line.
     * @return
     */
    private boolean isLeftFromLine(Point p, Point p1, Point p2) {
        return (p1.x() - p.x()) * (p2.y() - p.y()) >
               (p2.x() - p.x()) * (p1.y() - p.y());
    }
}
