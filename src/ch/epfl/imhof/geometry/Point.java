package ch.epfl.imhof.geometry;

/**
 * A point on Earth's surface, in Cartesian coordinates.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class Point {
    private final double x, y;
    /**
     * Construct a point with its abscissa and ordinate passed as parameters.
     * 
     * @param x
     *          Abscissa of the point.
     * @param y
     *          Ordinate of the point.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Return the abscissa of the point.
     * 
     * @return x
     *          Abscissa of the point.
     */
    public double x() {
        return x;
    }
    
    /**
     * Return the ordinate of the point.
     * 
     * @return y
     *          Ordinate of the point.
     */
    public double y() {
        return y;
    }
}
