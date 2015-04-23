package ch.epfl.imhof;

/**
 * A class representing a three-dimensional vector.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public class Vector3 {
    private double x, y, z;

    /**
     * Constructs a new Vector3 object.
     * 
     * @param x
     *            The x-component of the vector
     * @param y
     *            The y-component of the vector
     * @param z
     *            The z-component of the vector
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the norm of the vector.
     * 
     * @return The norm of the vector.
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Returns a normalized version of the vector.
     * 
     * @return A new Vector3 object, parallel, with the same direction, but with
     *         a norm of 1.
     */
    public Vector3 normalized() {
        double norm = norm();
        return new Vector3(x / norm, y / norm, z / norm);
    }

    /**
     * Returns the scalar product of the Vector with the vector given as
     * argument.
     * 
     * @param that
     *            Another Vector3 with which the scalar product will be
     *            calculated.
     * @return The scalar product of the Vector with the vector given as
     *         argument.
     */
    public double scalarProduct(Vector3 that) {
        return x * that.x + y * that.y + z * that.z;
    }
}
