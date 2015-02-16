package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public interface Projection {
    public Point project(PointGeo point);
    public PointGeo inverse(Point point);
}
