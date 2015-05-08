package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * Implementation of the CH1903 projection based on the Projection interface
 * Source : {@link http://tinyurl.com/k3zxbbn}
 * 
 * {@inheritDoc}
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class CH1903Projection implements Projection {

    /*
     * (non-Javadoc)
     * 
     * @see ch.epfl.imhof.projection.Projection#project(ch.epfl.imhof.PointGeo)
     */
    @Override
    // @formatter:off
    public Point project(PointGeo point) {
        double longitudeDegrees = Math.toDegrees(point.longitude());
        double latitudeDegrees = Math.toDegrees(point.latitude());
        double oneTenthousands = (1. / 10000);
        double longitude1 = oneTenthousands * (longitudeDegrees * 3600 - 26782.5);
        double latitude1 = oneTenthousands * (latitudeDegrees * 3600 - 169028.66);
        double long1TimesLat1 = longitude1 * latitude1;
        double long1Squared = longitude1 * longitude1;
        double x = 600072.37
                + 211455.93 * longitude1 
                - 10938.51 * long1TimesLat1
                - .36 * long1TimesLat1 * latitude1 
                - 44.54 * Math.pow(longitude1, 3);
        double y = 200147.07 
                + 308807.95 * latitude1 
                + 3745.25 * long1Squared 
                + 76.63 * latitude1 * latitude1 
                - 194.56 * long1Squared * latitude1 
                + 119.79 * Math.pow(latitude1, 3);
        return new Point(x, y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.epfl.imhof.projection.Projection#inverse(ch.epfl.imhof.geometry.Point)
     */
    @Override
    public PointGeo inverse(Point point) {
        double x1 = (point.x() - 6e5) / 1e6;
        double y1 = (point.y() - 2e5) / 1e6;
        double x1TimesY1 = x1 * y1;
        double x1Squared = x1 * x1;
        double longitude0 = 2.6779094 
                + 4.728982 * x1
                + 0.791484 * x1TimesY1
                + 0.1306 * x1TimesY1 * y1 
                - 0.0436 * Math.pow(x1, 3);
        double latitude0 = 16.9023892 
                + 3.238272 * y1 
                - 0.270978 * x1Squared
                - 0.002528 * y1 * y1 
                - 0.0447 * x1Squared * y1 
                - 0.014 * Math.pow(y1, 3);
        double conversionFraction = 100d / 36d;
        double longitudeRadians = Math.toRadians(longitude0 * conversionFraction);
        double latitudeRadians = Math.toRadians(latitude0 * conversionFraction);

        return new PointGeo(longitudeRadians, latitudeRadians);
    }
    // @formatter:on
}