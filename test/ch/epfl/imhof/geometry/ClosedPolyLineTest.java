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
        assertTrue(new ClosedPolyLine(pts3).isClosed());
    }

    @Test
    public void builderCorrectlyBuildsClosedPolyLine(){
        PolyLine.Builder b = new PolyLine.Builder();
        for (Point p: pts3)
            b.addPoint(p);
        PolyLine l = b.buildClosed();
        assertEqualsPoints(pts3, l.points(), 0);
        assertTrue(l.isClosed());
    }

    @Test
    public void areaWorksWithSinglePoint() {
        ClosedPolyLine l = new ClosedPolyLine(Arrays.asList(new Point(2, 3)));
        assertEquals(0, l.area(), DELTA);
    }

    @Test
    public void areaWorksWithTriangle(){
        ClosedPolyLine l = new ClosedPolyLine(
                Arrays.asList(
                        new Point(36, 54),
                        new Point(36, 55),
                        new Point(37, 55)));
        assertEquals(0.5, l.area(), DELTA);
    }

    @Test
    public void areaWorksWithUnitSquare(){
        ClosedPolyLine l = new ClosedPolyLine(
                Arrays.asList(
                        new Point(36, 54),
                        new Point(36, 55),
                        new Point(37, 55),
                        new Point(37, 54)));
        assertEquals(1, l.area(), DELTA);
    }

    @Test
    public void areaWorksWithPolygon(){
        ClosedPolyLine l = new ClosedPolyLine(
                Arrays.asList(
                        new Point(36, 62),
                        new Point(31, 57),
                        new Point(39, 57),
                        new Point(41, 48),
                        new Point(43, 57),
                        new Point(43, 47),
                        new Point(32, 46),
                        new Point(38, 51),
                        new Point(28, 57)));
        assertEquals(84, l.area(), DELTA);
    }

    @Test
    public void containsPointWorksWithSinglePoint() {
        Point p = new Point(2, 3);
        ClosedPolyLine l = new ClosedPolyLine(Arrays.asList(p));
        assertFalse(l.containsPoint(p));
    }

    @Test
    public void containsPointWorksWithTriangle(){
        ClosedPolyLine l = new ClosedPolyLine(
                Arrays.asList(
                        new Point(36, 54),
                        new Point(36, 55),
                        new Point(37, 55)));
        assertTrue(l.containsPoint(new Point(36.2, 54.3)));
        assertFalse(l.containsPoint(new Point(33, 54.3)));
    }

    @Test
    public void containsPointWorksWithRectangle(){
        ClosedPolyLine l = new ClosedPolyLine(
                Arrays.asList(
                        new Point(36, 54),
                        new Point(36, 62),
                        new Point(48, 62),
                        new Point(48, 54)));
        assertTrue(l.containsPoint(new Point(42, 58)));
        assertFalse(l.containsPoint(new Point(0, 54)));
    }

    @Test
    public void containsPointWorksWithPolygon(){
        ClosedPolyLine l = new ClosedPolyLine(
                Arrays.asList(
                        new Point(36, 62),
                        new Point(31, 57),
                        new Point(39, 57),
                        new Point(41, 48),
                        new Point(43, 57),
                        new Point(43, 47),
                        new Point(32, 46),
                        new Point(38, 51),
                        new Point(28, 57)));
        assertTrue(l.containsPoint(new Point(35, 56)));
        assertFalse(l.containsPoint(new Point(33, 51)));
    }
}
