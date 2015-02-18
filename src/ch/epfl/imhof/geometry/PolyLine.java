package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PolyLine {
    private final List<Point> points;
    
    public PolyLine(List<Point> points) throws IllegalArgumentException {
        if (points.isEmpty()) {
            throw new IllegalArgumentException("The list of points is empty");
        }
        ArrayList<Point> pointsCopy = new ArrayList<Point>(points);
        this.points = Collections.unmodifiableList(pointsCopy);
    }
    
    public abstract boolean isClosed();
    
    public List<Point> points() {
        return points;
    }
    
    public Point firstPoint() {
        return points.get(0);
    }
    public static final class Builder {
        
    }
}