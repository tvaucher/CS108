package ch.epfl.imhof;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
            printMapInFile();
            return new Map(polyLines, polygons);
        }
        
        /**
         * A private method for debugging purposes that creates a file in
         * data/debug
         */
        private void printMapInFile() { // FOR DEBUGING PURPOSES
            File debugDirectory = new File("data/debug");
            if (debugDirectory.exists() && debugDirectory.isDirectory()) {
                // Will normally be true only locally
                try (PrintWriter pr = new PrintWriter(new File("data/debug/Map_"
                        + System.currentTimeMillis() + ".txt"))) {
                    long startTime = System.currentTimeMillis();
                    pr.println("START PRINTING MAP\n");
                    pr.println("THERE'S " + polyLines.size() + " POLYLINES");
                    pr.println("THERE'S " + polygons.size() + " POLYGONS");
                    pr.println("START PRINTING POLYGONS");
                    int counter = 1;
                    for (Attributed<Polygon> polygon : polygons) {
                        pr.println("  #"+ counter++ + " POLYGON");
                        //pr.println("  AREA OF " + polygon.value().shell().area());
                        pr.println("    HAS " + polygon.value().shell().points().size() + " POINTS");
                        pr.println("    HAS " + polygon.value().holes().size() + " HOLES");
                    }
                    pr.println("\nWRITTEN IN "
                            + (System.currentTimeMillis() - startTime) / 1000.
                            + " sec");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
