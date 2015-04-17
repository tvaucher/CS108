package ch.epfl.imhof.geometry;

import java.util.function.Function;

/**
 * A point on Earth's surface, in Cartesian coordinates.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class Point {
    private final double x, y;

    /**
     * Constructs a point with its abscissa and ordinate passed as parameters.
     * 
     * @param x
     *            Abscissa of the point.
     * @param y
     *            Ordinate of the point.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the abscissa of the point.
     * 
     * @return x Abscissa of the point.
     */
    public double x() {
        return x;
    }

    /**
     * Returns the ordinate of the point.
     * 
     * @return y Ordinate of the point.
     */
    public double y() {
        return y;
    }

    public static Function<Point, Point> alignedCoordinateChange(
            Point oldPoint1, Point newPoint1, Point oldPoint2, Point newPoint2) {
        if (oldPoint1.x == oldPoint2.x || oldPoint1.y == oldPoint2.y)
            throw new IllegalArgumentException(
                    "Please give two different, valid points the initial coordinates (they should not have the same x or y)");

        // We need to find two equations of the type (ax)*X+(bx) and
        // (ay)*Y+(by), which will handle both translation and dilation.
        double ax = (newPoint1.x - newPoint2.x) / (oldPoint1.x - oldPoint2.x);
        double bx = newPoint2.x + Math.signum(oldPoint2.x) * ax
                * (oldPoint2.x / oldPoint1.x);

        double ay = (newPoint1.y - newPoint2.y) / (oldPoint1.y - oldPoint2.y);
        double by = newPoint2.y + Math.signum(oldPoint2.y) * ay
                * (oldPoint2.y / oldPoint1.y);

        return point -> new Point(ax * point.x + bx, ay * point.y + by);
    }
}
