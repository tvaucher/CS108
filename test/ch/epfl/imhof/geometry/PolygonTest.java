package ch.epfl.imhof.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PolygonTest {
    @Test
    public void shellGetterReturnsShell() {
        ClosedPolyLine s = new ClosedPolyLine(
                Arrays.asList(
                        new Point(36, 54),
                        new Point(193, -61),
                        new Point(-70, -231),
                        new Point(-634, 96),
                        new Point(36, 54)));
        Polygon p = new Polygon(s);
        assertSame(s, p.shell());
    }

    @Test
    public void constructorWithoutHolesDoesNotCreateHoles() {
        ClosedPolyLine s = new ClosedPolyLine(
                Arrays.asList(
                        new Point(0, 0), new Point(1, 0), new Point(1, 1)));
        Polygon p = new Polygon(s);
        assertTrue(p.holes().isEmpty());
    }

    @Test
    public void holeListIsClonedBeforeBeingStored() {
        ClosedPolyLine s = new ClosedPolyLine(
                Arrays.asList(
                        new Point(36, 54),
                        new Point(193, -61),
                        new Point(-70, -231),
                        new Point(-634, 96),
                        new Point(36, 54)));
        ClosedPolyLine h1 = new ClosedPolyLine(
                Arrays.asList(
                        new Point(20, 34),
                        new Point(0, -31),
                        new Point(-40, -51),
                        new Point(20, 34)));
        ClosedPolyLine h2 = new ClosedPolyLine(
                Arrays.asList(
                        new Point(27, 44),
                        new Point(63, -41),
                        new Point(-5, 0),
                        new Point(27, 44)));
        List<ClosedPolyLine> hs = Arrays.asList(h1, h2);
        List<ClosedPolyLine> hsMut = new ArrayList<>(hs);
        Polygon p = new Polygon(s, hsMut);
        hsMut.clear();
        assertEquals(2, p.holes().size());
        assertTrue(p.holes().containsAll(hs));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void holesGetterReturnsUnmodifiableList() {
        ClosedPolyLine s = new ClosedPolyLine(
                Arrays.asList(new Point(0,0), new Point(0,10), new Point(10,10)));
        ClosedPolyLine l1 = new ClosedPolyLine(
                Arrays.asList(new Point(1,1), new Point(1,2), new Point(2,2)));
        ClosedPolyLine l2 = new ClosedPolyLine(
                Arrays.asList(new Point(5,5), new Point(5,6), new Point(6,6)));
        Polygon p = new Polygon(s, new ArrayList<>(Arrays.asList(l1, l2)));
        p.holes().clear();
    }
}
