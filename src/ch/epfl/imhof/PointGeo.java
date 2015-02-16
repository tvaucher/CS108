package ch.epfl.imhof;

/**
 * Un point à la surface de la Terre, en coordonnées sphériques.
 * 
 * @author Maxime Kjaer (250694)
 */
public final class PointGeo {
    private final double longitude;
    private final double latitude;
    /**
     * Construit un point avec la latitude et la longitude en données.
     * 
     * @param longitude
     *          La longitude du point, en radians.
     * @param latitude
     *          La latitude du point, en radians.
     * @throws IllegalArgumentException
     *          Si la longitude est invalide, c'est-à-dire hors de l'intervalle [-π; π]
     * @throws IllegalArgumentException
     *          Si la latitude est invalide, c'est-à-dire hors de l'intervalle [-π/2; π/2]
     */
    public PointGeo (double longitude, double latitude) throws IllegalArgumentException {
        if (Math.abs(latitude) > Math.PI/2) {
            throw new IllegalArgumentException("Latitude out of allowed range");
        }
        if (Math.abs(longitude) > Math.PI) {
            throw new IllegalArgumentException("Longitude out of allowed range");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    /**
     * Retourne la latitude du point, en radians.
     * 
     * @return latitude
     *          La latitude du point, en radians.
     */
    public double latitude() {
        return latitude;
    }
    
    /**
     * Retourne la longitude du point, en radians.
     * 
     * @return longitude
     *          La longitude du point, en radians.
     */
    public double longitude() {
        return longitude;
    }
}
