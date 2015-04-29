package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * Interface that describes the DigitalElevationModel extends AutoCloseable
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 *
 */
public interface DigitalElevationModel extends AutoCloseable {
    /**
     * Returns a normal vector of point p, based on the 4 closest point of the
     * DEM map. Uses a formula that takes the average of 2 normal vector based
     * on the 4 previous points. Method can be seen here 
     * {@link http://cs108.epfl.ch/p10_dem.html#sec-1-3}
     * 
     * @param p
     *            point from which we look for the normal vector
     * @return a normalized normal vector of point p
     * @throws IllegalArgumentException
     *             if p isn't contained in the current DEM map
     */
    public Vector3 normalAt(PointGeo p) throws IllegalArgumentException;
}
