package ch.epfl.imhof.geometry;

import java.util.List;

public final class ClosedPolyLine extends PolyLine{
    private final int pointSize;
    public ClosedPolyLine(List<Point> points) {
        super(points);
        pointSize = points.size();
    }
    
    public boolean isClosed() {
        return true;
    }
    
    public double area() {
        double twiceSignedArea = 0.; //2 times the signed area
        for (int i = 0; i < pointSize; ++i) {
            twiceSignedArea += points().get(i).x() *
                    (points().get(getGeneralisedIndex(i+1)).y()
                    - points().get(getGeneralisedIndex(i-1)).y());
        }
        return .5 * Math.abs(twiceSignedArea);
    }
    
    public boolean containsPoint(Point p) {
        int count = 0;
        for (int i = 0; i < pointSize; ++i) {
            Point p1 = points().get(i);
            Point p2 = points().get(getGeneralisedIndex(i+1));
            if (p1.y() <= p.y()) {
                if (p2.y() > p.y() && isLeftFromLine(p, p1, p2)) {
                    ++count;
                }
            }
            else {
                if (p2.y() <= p.y() && isLeftFromLine(p, p1, p2)) {
                    --count;
                }
            }
        }
               
        return (count != 0);
    }
    
    private int getGeneralisedIndex(int index) {
        return Math.floorMod(index, pointSize);
    }
    
    private boolean isLeftFromLine(Point p, Point p1, Point p2) {
        return (p1.x() - p.x()) * (p2.y() - p.y()) > 
               (p2.x() - p.x()) * (p1.y() - p.y());
    }
}
