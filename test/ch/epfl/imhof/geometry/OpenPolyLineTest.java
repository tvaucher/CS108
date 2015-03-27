package ch.epfl.imhof.geometry;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

public final class OpenPolyLineTest extends PolyLineTest {
    @Override
    PolyLine newPolyLine(List<Point> ps) {
        return new OpenPolyLine(ps);
    }

    @Test
    public void openPolyLineIsOpen() {
        for (List<Point> lpt : pts) {
            assertFalse(new OpenPolyLine(lpt).isClosed());
        }
    }

    @Test
    public void builderCorrectlyBuildsOpenPolyLine() {
        for (List<Point> lpt : pts) {
            PolyLine.Builder b = new PolyLine.Builder();
            for (Point p : lpt)
                b.addPoint(p);
            PolyLine l = b.buildOpen();
            assertEqualsPoints(lpt, l.points(), 0);
            assertFalse(l.isClosed());
        }
    }
}
