package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public final class EquirectangularProjection implements Projection{
    
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }
    
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
}
