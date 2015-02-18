package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A polyline is a collection of interconnected points that may either form
 * a closed (ClosedPolyLine) or an open (OpenPolyLine) object.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public abstract class PolyLine {
    private final List<Point> points;
    
    /**
     * Construct a polyline from a list of points.
     * 
     * @param points
     *          A list of points that will form a polyline
     * @throws IllegalArgumentException
     *          If the list of points is empty, the constructor throws an IllegalArgumentException
     */
    public PolyLine(List<Point> points) throws IllegalArgumentException {
        if (points.isEmpty()) {
            throw new IllegalArgumentException("The list of points is empty");
        }
        ArrayList<Point> pointsCopy = new ArrayList<Point>(points);
        this.points = Collections.unmodifiableList(pointsCopy);
    }
    
    /**
     * Return true if the polyline is closed, false if it's open.
     */
    public abstract boolean isClosed();
    
    /**
     * Return the list of points that make up the polyline.
     * 
     * @return points
     *          The list of points that make up the polyline. 
     */
    public List<Point> points() {
        return points;
    }
    
    /**
     * Return the first point of the polyline 
     * 
     * @return points.get(0);
     *          The first point of the polyline
     */
    public Point firstPoint() {
        return points.get(0);
    }
    
    
    /**
     * The Builder class can receive data about a polyline before instantiating it. 
     * 
     * @author Maxime Kjaer (250694)
     * @author Timote Vaucher (246532)
     */
    public static final class Builder {
        private final List<Point> points = new ArrayList<Point>();
        
        /**
         * Adds a point to the list of points that will be built as a polyline.
         * 
         * @param newPoint
         *      A point to add to the list of points of a polyline.
         */
        public void addPoint(Point newPoint) {
            points.add(newPoint);
        }
        
        /**
         * @return new OpenPolyLine object
         *      A new, open polyline object
         */
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(points);
        }
        
        /**
         * @return new ClosedPolyLine object
         *      A new, closed polyline object
         */
        public ClosedPolyLine buildClosed() {
            return new ClosedPolyLine(points);
        }
    }
}