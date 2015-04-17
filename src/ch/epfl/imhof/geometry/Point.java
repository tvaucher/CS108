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

    /**
     * Changes the frame of a vector space by calculating the translation and
     * dilation between two frames. It does so by essentially solving a 2x2
     * matrix (though instead of implementing the Gauss reduction algorithm, it
     * uses a formula specifically designed for 2x2 matrices).
     * <p>
     * It does not support rotation of the frame.
     * <p>
     * Giving it "bad" values (i.e. two initial points with the same x or y, or
     * just two identical points) will result in an IllegalArgumentException
     * since no information can be gathered from such data.
     * 
     * @param oldPoint1
     *            A Point in the old frame
     * @param newPoint1
     *            The same point in the new frame
     * @param oldPoint2
     *            Another Point in the old frame
     * @param newPoint2
     *            The same point in the new frame
     * 
     * @return A Function that translates a Point from one frame to another.
     */
    public static Function<Point, Point> alignedCoordinateChange(
            Point oldPoint1, Point newPoint1, Point oldPoint2, Point newPoint2) {
        if (oldPoint1.x == oldPoint2.x || oldPoint1.y == oldPoint2.y)
            throw new IllegalArgumentException(
                    "Please give two different, valid points the initial coordinates (they should not have the same x or y)");

        // We need to find two equations of the type (ax)*X+(bx) and
        // (ay)*Y+(by), which will handle both translation and dilation.
        double ax = (newPoint1.x - newPoint2.x) / (oldPoint1.x - oldPoint2.x);
        double bx = newPoint2.x - ax * oldPoint2.x;

        double ay = (newPoint1.y - newPoint2.y) / (oldPoint1.y - oldPoint2.y);
        double by = newPoint2.y - ay * oldPoint2.y;
        
        // Check the results
        if (newPoint1.x != ax * oldPoint1.x + bx || newPoint2.x != ax * oldPoint2.x + bx) {
            System.out.println("There is an error in the equation for x!!!!");
            System.out.println("Theory: (" + oldPoint1.x + ", " + oldPoint1.y + ") => (" + newPoint1.x + ", " + newPoint1.y + ")");
            System.out.println("Theory: (" + oldPoint2.x + ", " + oldPoint2.y + ") => (" + newPoint2.x + ", " + newPoint2.y + ")");
            System.out.println("Practice: (" + oldPoint1.x + ", " + oldPoint1.y + ") => (" + (ax*oldPoint1.x+bx) + ", " + (ax*newPoint1.y+bx) + ")");
            System.out.println("The equation is: " + ax + "x + " + bx);
            System.out.println();
        }
        else System.out.println("Equation for x is correct!");
        if (newPoint1.y != ay * oldPoint1.y + by || newPoint2.y != ay * oldPoint2.y + by) {
            System.out.println("There is an error in the equation for y!!!!");
            System.out.println("(" + oldPoint2.x + ", " + oldPoint2.y + ") => (" + newPoint2.x + ", " + newPoint2.y + ")");
            System.out.println("The equation is: " + ay + "y + " + by);
            System.out.println();
        }
        
        
        
        return point -> new Point(ax * point.x() + bx, ay * point.y() + by);
    }
}
