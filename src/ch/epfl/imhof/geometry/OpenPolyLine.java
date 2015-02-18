package ch.epfl.imhof.geometry;

import java.util.List;

public final class OpenPolyLine extends PolyLine {
    public OpenPolyLine(List<Point> points) {
        super(points);
    }
    
    public boolean isClosed() {
        return false;
    }
}
