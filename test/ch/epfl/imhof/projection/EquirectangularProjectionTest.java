package ch.epfl.imhof.projection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

public class EquirectangularProjectionTest {
    private static final double DELTA = 0.000001;

    @Test
    public void projectProducesTheRightValues() {
        Projection p = new EquirectangularProjection();
        for (double lon = -3.0; lon <= 3.0; lon += 0.2) {
            for (double lat = -1.5; lat <= 1.5; lat += 0.1) {
                Point pt = p.project(new PointGeo(lon, lat));
                assertEquals(lon, pt.x(), DELTA);
                assertEquals(lat, pt.y(), DELTA);
            }
        }
    }

    @Test
    public void inverseProducesTheRightValues() {
        Projection p = new EquirectangularProjection();
        for (double x = -3.0; x <= 3.0; x += 0.2) {
            for (double y = -1.5; y <= 1.5; y += 0.1) {
                PointGeo pt = p.inverse(new Point(x, y));
                assertEquals(x, pt.longitude(), DELTA);
                assertEquals(y, pt.latitude(), DELTA);
            }
        }
    }
}
