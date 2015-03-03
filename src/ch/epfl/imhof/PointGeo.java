package ch.epfl.imhof;

/**
 * A point on Earth's surface, in spherical coordinates.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class PointGeo {
    private final double longitude;
    private final double latitude;

    /**
     * Construct a point with its longitude and latitude passed as parameters.
     * 
     * @param longitude
     *            Longitude of the point, in radians.
     * @param latitude
     *            Latitude of the point, in radians.
     * @throws IllegalArgumentException
     *             If the longitude is invalid, when out of allowed range [-π;
     *             π]
     * @throws IllegalArgumentException
     *             If the latitude is invalid, when out of allowed range [-π/2;
     *             π/2]
     */
    public PointGeo(double longitude, double latitude)
            throws IllegalArgumentException {
        if (Math.abs(latitude) > Math.PI / 2) {
            throw new IllegalArgumentException("Latitude out of allowed range");
        }
        if (Math.abs(longitude) > Math.PI) {
            throw new IllegalArgumentException("Longitude out of allowed range");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Return the longitude of the point, in radians.
     * 
     * @return longitude Longitude of the point, in radians.
     */
    public double longitude() {
        return longitude;
    }

    /**
     * Return the latitude of the point, in radians.
     * 
     * @return latitude Latitude of the point, in radians.
     */
    public double latitude() {
        return latitude;
    }
}
