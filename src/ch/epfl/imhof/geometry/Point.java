package ch.epfl.imhof.geometry;

/**
 * Un point à la surface de la Terre, en coordonnées cartésiennes.
 * 
 * @author Maxime Kjaer (250694)
 */
public final class Point {
    private final double x, y;
    /**
     * Construit un point avec l'abscisse et l'ordonnée en données.
     * 
     * @param x
     *          L'abscisse du point.
     * @param y
     *          L'ordonnée du point.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Retourne l'abscisse du point.
     * 
     * @return x
     *          L'abscisse du point
     */
    public double x() {
        return x;
    }
    
    /**
     * Retourne l'ordonnée du point.
     * 
     * @return y
     *          L'ordonnée du point
     */
    public double y() {
        return y;
    }
}
