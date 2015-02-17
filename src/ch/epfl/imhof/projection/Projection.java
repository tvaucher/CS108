package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Contains the method signatures for the projection and its inverse
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public interface Projection {

    /**
     * Project a point on a plane. Mean of projection is in the implementation
     * of the interface.
     * 
     * @param point
     *            Point to be projected.
     * @return point that has been projected
     */
    public Point project(PointGeo point);

    /**
     * "Un-Project" a point from a plane, inverse of projection method. Mean of
     * projection is in the implementation of the interface.
     * 
     * @param point
     *            Point to be "un-projected".
     * @return point that has been "un-projected".
     */
    public PointGeo inverse(Point point);
}
