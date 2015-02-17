package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Implementation of the Equirectangular projection based on the Projection
 * interface
 * 
 * @see {@link ch.epfl.imhof.projection.Projection} for interface doc
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class EquirectangularProjection implements Projection {

    /*
     * (non-Javadoc)
     * 
     * @see ch.epfl.imhof.projection.Projection#project(ch.epfl.imhof.PointGeo)
     */
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.epfl.imhof.projection.Projection#inverse(ch.epfl.imhof.geometry.Point)
     */
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
}
