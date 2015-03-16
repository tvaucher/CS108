package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.util.Objects.requireNonNull;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public final class Map {
    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;
    
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections.unmodifiableList(new ArrayList<>(requireNonNull(polyLines)));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(requireNonNull(polygons)));
    }
    
    public List<Attributed<PolyLine>> polyLines() {
        return polyLines;
    }
    
    public List<Attributed<Polygon>> polygons() {
        return polygons;
    }
    
    public static class Builder {
        private List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        private List<Attributed<Polygon>> polygons = new ArrayList<>();
        
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            polyLines.add(newPolyLine);
        }
        
        public void addPolygon(Attributed<Polygon> newPolygon) {
            polygons.add(newPolygon);
        }
        
        public Map build() {
            return new Map(polyLines, polygons);
        }
    }
}
