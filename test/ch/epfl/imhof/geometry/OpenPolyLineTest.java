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
        assertFalse(new OpenPolyLine(pts3).isClosed());
    }

    @Test
    public void builderCorrectlyBuildsOpenPolyLine(){
        PolyLine.Builder b = new PolyLine.Builder();
        for (Point p: pts3)
            b.addPoint(p);
        PolyLine l = b.buildOpen();
        assertEqualsPoints(pts3, l.points(), 0);
        assertFalse(l.isClosed());
    }
}
