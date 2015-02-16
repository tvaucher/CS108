package ch.epfl.imhof;

public final class PointGeo {
    private final double longitude;
    private final double latitude;
    public PointGeo (double longitude, double latitude) {
        if (Math.abs(latitude) > Math.PI/2) {
            throw new IllegalArgumentException("Latitude hors intervalle");
        }
        if (Math.abs(longitude) > Math.PI) {
            throw new IllegalArgumentException("Longitude hors intervalle");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    public double latitude() {
        return latitude;
    }
    
    public double longitude() {
        return longitude;
    }
}
