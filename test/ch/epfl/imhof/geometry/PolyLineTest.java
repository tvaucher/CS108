package ch.epfl.imhof.geometry;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public abstract class PolyLineTest {
    protected static double DELTA = 0.000001;
    protected static List<Point> pts3 = Arrays.asList(
            new Point(0, 0), new Point(10, 0), new Point(10, 10));

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

    @Test(expected = UnsupportedOperationException.class)
    public void pointsGetterReturnsUnmodifiableList() {
        PolyLine l = newPolyLine(new ArrayList<>(pts3));
        l.points().clear();
    }

    @Test
    public void firstPointReturnsFirstPoint() {
        assertEqualsPoint(pts3.get(0), newPolyLine(pts3).firstPoint(), 0);
    }
}
