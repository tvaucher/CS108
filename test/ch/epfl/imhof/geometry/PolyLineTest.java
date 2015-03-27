package ch.epfl.imhof.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public abstract class PolyLineTest {
    protected static double DELTA = 0.000001;
    protected static List<Point> pts2 = Arrays.asList(new Point(10, 0), new Point(0, 10));
    protected static List<Point> pts3 = Arrays.asList(new Point(0, 0), new Point(10, 0), new Point(10, 10));
    protected static List<Point> ptsSinglePoint = Arrays.asList(new Point(2, 3));
    protected static List<Point> ptsSinglePoint2 = Arrays.asList(new Point(-1, -4));
    protected static List<Point> ptsSinglePoint3 = Arrays.asList(new Point(0, -10));
    protected static List<Point> ptsTriangle = Arrays.asList(new Point(36, 54),new Point(36, 55), new Point(37, 55));
    protected static List<Point> ptsTriangle2 = Arrays.asList(new Point(0, 0),new Point(-1, 0), new Point(-1, 1));
    protected static List<Point> ptsUnitSquare = Arrays.asList(new Point(36, 54), new Point(36, 55), new Point(37, 55), new Point(37, 54));
    protected static List<Point> ptsUnitSquare2 = Arrays.asList(new Point(-11, -30), new Point(-10, -30), new Point(-10, -31), new Point(-11, -31));
    protected static List<Point> ptsRectangle = Arrays.asList( new Point(36, 54), new Point(36, 62), new Point(48, 62), new Point(48, 54));
    protected static List<Point> ptsRectangle2 = Arrays.asList( new Point(65, -80), new Point(65,-10), new Point(73, -10), new Point(73, -80));
    protected static List<Point> ptsPolygon = Arrays.asList(new Point(36, 62), new Point(31, 57), new Point(39, 57), new Point(41, 48), new Point(43, 57), new Point(43, 47), new Point(32, 46), new Point(38, 51), new Point(28, 57));
    protected static List<Point> ptsPolygon2 = Arrays.asList(new Point(0, 30), new Point(10, 20), new Point(11, 5), new Point(10, 29), new Point(15, 0), new Point(0, 47), new Point(2,15));

    protected static List<List<Point>> pts = Arrays.asList(pts2, pts3,
            ptsSinglePoint, ptsSinglePoint2, ptsSinglePoint3, ptsTriangle,
            ptsTriangle2, ptsUnitSquare, ptsUnitSquare2, ptsRectangle,
            ptsRectangle2, ptsPolygon, ptsPolygon2);

    protected static void assertEqualsPoint(Point pExpected, Point pActual, double delta) {
        assertEquals(pExpected.x(), pActual.x(), delta);
        assertEquals(pExpected.y(), pActual.y(), delta);
    }

    protected static void assertEqualsPoints(List<Point> psExpected, List<Point> psActual, double delta) {
        assertEquals(psExpected.size(), psActual.size());
        Iterator<Point> iE = psExpected.iterator();
        Iterator<Point> iA = psActual.iterator();
        while (iE.hasNext()) {
            assertEqualsPoint(iE.next(), iA.next(), delta);
        }
    }

    abstract PolyLine newPolyLine(List<Point> ps);

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsOnEmptyPointList() {
        newPolyLine(Collections.emptyList());
    }

    @Test
    public void pointListIsClonedBeforeBeingStored() {
        ArrayList<Point> ptsMut = new ArrayList<>(pts3);
        PolyLine l = newPolyLine(ptsMut);
        ptsMut.clear();
        assertEqualsPoints(pts3, l.points(), 0);
    }

    @Test
    public void pointsGetterReturnsUnmodifiableList() {
        PolyLine l = newPolyLine(new ArrayList<>(pts3));
        try {
            l.points().clear();
            assertEqualsPoints(pts3, l.points(), 0);
        } catch (UnsupportedOperationException e) {
            // do nothing (the list is unmodifiable)
        }
    }

    @Test
    public void firstPointReturnsFirstPoint() {
        assertEqualsPoint(pts3.get(0), newPolyLine(pts3).firstPoint(), 0);
    }

    @Test
    public void addPointAddsCorrectlyAtTheEnd() {
        PolyLine.Builder builder = new PolyLine.Builder();
        builder.addPoint(new Point(10,0));
        builder.addPoint(new Point(0,10));
        assertEqualsPoints(builder.buildClosed().points(),pts2, DELTA);
    }

    @Test
    public void buildOpenCreatesOpenPolyLine() {
        PolyLine.Builder builder = new PolyLine.Builder();
        builder.addPoint(new Point(10,0));
        builder.addPoint(new Point(0,10));
        PolyLine ol = builder.buildOpen();
        assertTrue(!ol.isClosed());
        assertTrue(ol instanceof OpenPolyLine);
        assertEqualsPoints(ol.points(),pts2, DELTA);
    }

    @Test
    public void buildClosedCreatesClosePolyLine() {
        PolyLine.Builder builder = new PolyLine.Builder();
        builder.addPoint(new Point(10,0));
        builder.addPoint(new Point(0,10));
        PolyLine ol = builder.buildClosed();
        assertTrue(ol.isClosed());
        assertTrue(ol instanceof ClosedPolyLine);
        assertEqualsPoints(ol.points(),pts2, DELTA);
    }
}
