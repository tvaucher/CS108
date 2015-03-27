package ch.epfl.imhof.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ClosedPolyLineTest extends PolyLineTest {

    @Override
    PolyLine newPolyLine(List<Point> ps) {
        return new ClosedPolyLine(ps);
    }

    @Test
    public void closedPolyLineIsClosed() {
        for (List<Point> lpt : pts) {
            assertTrue(new ClosedPolyLine(lpt).isClosed());
        }
    }

    @Test
    public void builderCorrectlyBuildsClosedPolyLine() {
        for (List<Point> lpt : pts) {
            PolyLine.Builder b = new PolyLine.Builder();
            for (Point p : lpt)
                b.addPoint(p);
            PolyLine l = b.buildClosed();
            assertEqualsPoints(lpt, l.points(), 0);
            assertTrue(l.isClosed());
        }
    }

    @Test
    public void areaWorksWithSinglePoint() {
        ClosedPolyLine l = new ClosedPolyLine(Arrays.asList(new Point(2, 3)));
        assertEquals(0, l.area(), DELTA);
        
        ClosedPolyLine l2 = new ClosedPolyLine(Arrays.asList(new Point(-1, -4)));
        assertEquals(0, l2.area(), DELTA);
        
        ClosedPolyLine l3 = new ClosedPolyLine(Arrays.asList(new Point(0, -10)));
        assertEquals(0, l3.area(), DELTA);
    }

    @Test
    public void areaWorksWithTriangle() {
        ClosedPolyLine l = new ClosedPolyLine(ptsTriangle);
        assertEquals(0.5, l.area(), DELTA);
        
        ClosedPolyLine l2 = new ClosedPolyLine(ptsTriangle2);
        assertEquals(0.5, l2.area(), DELTA);
    }

    @Test
    public void areaWorksWithUnitSquare() {
        ClosedPolyLine l = new ClosedPolyLine(ptsUnitSquare);
        assertEquals(1, l.area(), DELTA);
        
        ClosedPolyLine l2 = new ClosedPolyLine(ptsUnitSquare2);
        assertEquals(1, l2.area(), DELTA);
    }

    @Test
    public void areaWorksWithPolygon() {
        ClosedPolyLine l = new ClosedPolyLine(ptsPolygon);
        assertEquals(84, l.area(), DELTA);

        ClosedPolyLine l2 = new ClosedPolyLine(ptsPolygon2);
        assertEquals(17.5, l2.area(), DELTA);
    }

    @Test
    public void containsPointWorksWithSinglePoint() {
        Point p = new Point(2, 3);
        ClosedPolyLine l = new ClosedPolyLine(Arrays.asList(p));
        assertFalse(l.containsPoint(p));
        
        Point p2 = new Point(-1, -4);
        ClosedPolyLine l2 = new ClosedPolyLine(Arrays.asList(p2));
        assertFalse(l2.containsPoint(p2));
        
        Point p3 = new Point(0, -10);
        ClosedPolyLine l3 = new ClosedPolyLine(Arrays.asList(p3));
        assertFalse(l3.containsPoint(p3));
    }

    @Test
    public void containsPointWorksWithTriangle() {
        ClosedPolyLine l = new ClosedPolyLine(ptsTriangle);
        assertTrue(l.containsPoint(new Point(36.2, 54.3)));
        assertFalse(l.containsPoint(new Point(33, 54.3)));
        
        ClosedPolyLine l2 = new ClosedPolyLine(ptsTriangle2);
        assertTrue(l2.containsPoint(new Point(-0.5,0.1)));
        assertFalse(l2.containsPoint(new Point(0, 1)));
    }

    @Test
    public void containsPointWorksWithRectangle() {
        ClosedPolyLine l = new ClosedPolyLine(ptsRectangle);
        assertTrue(l.containsPoint(new Point(42, 58)));
        assertFalse(l.containsPoint(new Point(0, 54)));
        
        ClosedPolyLine l2 = new ClosedPolyLine(ptsRectangle2);
        assertTrue(l2.containsPoint(new Point(68, -20)));
        assertFalse(l2.containsPoint(new Point(77, 80)));
    }

    @Test
    public void containsPointWorksWithPolygon() {
        ClosedPolyLine l = new ClosedPolyLine(ptsPolygon);
        assertTrue(l.containsPoint(new Point(35, 56)));
        assertFalse(l.containsPoint(new Point(33, 51)));
        
        ClosedPolyLine l2 = new ClosedPolyLine(ptsPolygon2);
        assertTrue(l2.containsPoint(new Point(3, 30)));
        assertFalse(l2.containsPoint(new Point(33, 51)));
        
    }
}
