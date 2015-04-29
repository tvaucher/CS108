package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

public interface DigitalElevationModel extends AutoCloseable {
    public Vector3 normalAt(PointGeo p) throws IllegalArgumentException;
}
