package ch.epfl.imhof;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PointGeoTest {
    @Test  (expected = IllegalArgumentException.class)
    public void constructorFailsOnInvalidLongitude() {
        new PointGeo(5.986310, 0);
    }
    
    @Test
    public void constructorSucceedsOnLimitLongitude() {
        new PointGeo(-Math.PI, 0);
        new PointGeo(Math.PI, 0);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void constructorFailsOnInvalidLatitude() {
        new PointGeo(0, 1.962314);
    }
    
    @Test
    public void constructorSucceedsOnLimitLatitude() {
    	new PointGeo(0, -Math.PI/2);
    	new PointGeo(0, Math.PI/2);
    }

    @Test
    public void longitudeGetterReturnsLongitude() {
        for (double l = -Math.PI; l <= Math.PI; l += Math.PI / 6.0)
            assertEquals(l, new PointGeo(l, 0).longitude(), 0);
    }

    @Test
    public void latitudeGetterReturnsLatitude() {
        for (double l = -Math.PI / 2.0; l <= Math.PI / 2.0; l += Math.PI / 3.0)
            assertEquals(l, new PointGeo(0, l).latitude(), 0);
    }
}
