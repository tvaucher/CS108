package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Implementation of the CH1903 projection based on the Projection interface
 * 
 * @see {@link ch.epfl.imhof.projection.Projection} for interface doc
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class CH1903Projection implements Projection{
    
    /* (non-Javadoc)
     * @see ch.epfl.imhof.projection.Projection#project(ch.epfl.imhof.PointGeo)
     */
    public Point project(PointGeo point) {
        double longitudeDegrees = Math.toDegrees(point.longitude());
        double latitudeDegrees = Math.toDegrees(point.latitude());
        double longitude1 = (1. / 10000) * (longitudeDegrees * 3600 - 26782.5);
        double latitude1 = (1. / 10000) * (latitudeDegrees * 3600 - 169028.66);
        double x = 600072.37
                + 211455.93 * longitude1 
                - 10938.51 * longitude1 * latitude1 
                - .36 * longitude1 * latitude1 * latitude1 
                - 44.54 * Math.pow(longitude1, 3);
        double y = 200147.07 
                + 308807.95 * latitude1 
                + 3745.25 * longitude1 * longitude1 
                + 76.63 * latitude1 * latitude1 
                - 194.56 * longitude1 * longitude1 * latitude1 
                + 119.79 * Math.pow(latitude1, 3);

        return new Point(x, y);
    }

    /* (non-Javadoc)
     * @see ch.epfl.imhof.projection.Projection#inverse(ch.epfl.imhof.geometry.Point)
     */
    public PointGeo inverse(Point point) {
        double x1 = (point.x() - 6e5) / 1e6;
        double y1 = (point.y() - 2e5) / 1e6;
        double longitude0 = 2.6779094 
                + 4.728982 * x1 
                + 0.791484 * x1 * y1
                + 0.1306 * x1 * y1 * y1 
                - 0.0436 * Math.pow(x1, 3);
        double latitude0 = 16.9023892 
                + 3.238272 * y1 
                - 0.270978 * x1 * x1
                - 0.002528 * y1 * y1 
                - 0.0447 * x1 * x1 * y1 
                - 0.014 * Math.pow(y1, 3);
        double longitudeRadians = Math.toRadians(longitude0 * 100 / 36);
        double latitudeRadians = Math.toRadians(latitude0 * 100 / 36);

        return new PointGeo(longitudeRadians, latitudeRadians);
    }
}
