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

/**
 * A map of geometrical objects (polylines, polygons and their attributes), as
 * opposed to the rather abstract representation of objects that OSM uses.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 * 
 */
public final class Map {
    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;

    /**
     * Constructs a new Map of polylines and polygons
     * 
     * @param polyLines
     *            a list of attributed PolyLine objects
     * @param polygons
     *            a list of attributed Polygon objects
     */
    public Map(List<Attributed<PolyLine>> polyLines,
            List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections.unmodifiableList(new ArrayList<>(
                requireNonNull(polyLines)));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(
                requireNonNull(polygons)));
    }

    /**
     * A getter for the polylines of the map.
     * 
     * @return a list of all the attributed polylines in the map
     */
    public List<Attributed<PolyLine>> polyLines() {
        return polyLines;
    }

    /**
     * A getter for the polygons of the map
     * 
     * @return a list of all the attributed polygons in the map
     */
    public List<Attributed<Polygon>> polygons() {
        return polygons;
    }

    /**
     * A Builder for the Map, which will assist in the creation of a such object
     * by allowing us to add polylines and polygons one at a time.
     */
    public final static class Builder {
        private final List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        private final List<Attributed<Polygon>> polygons = new ArrayList<>();

        /**
         * Adds an attributed polyline to the list of polylines that will be a
         * part of the map.
         * 
         * @param newPolyLine
         *            a polyline with attributes.
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            polyLines.add(newPolyLine);
        }

        /**
         * Adds an attributed polygon to the list of polygons that will be a
         * part of the map.
         * 
         * @param newPolygon
         *            a polygon with attributes.
         */
        public void addPolygon(Attributed<Polygon> newPolygon) {
            polygons.add(newPolygon);
        }

        /**
         * Create a new Map object with the data that has been inputted into the
         * Builder. Currently also prints a debugging file using the @see
         * ch.epfl.imhof.Map#printMapInFile method.
         * 
         * @return the newly constructed Map
         */
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
                try (PrintWriter pr = new PrintWriter(
                        new File("data/debug/Map_" + System.currentTimeMillis()
                                + ".txt"))) {
                    long startTime = System.currentTimeMillis();
                    pr.println("START PRINTING MAP\n");
                    pr.println("THERE'S " + polyLines.size() + " POLYLINES");
                    pr.println("THERE'S " + polygons.size() + " POLYGONS");
                    pr.println("START PRINTING POLYGONS");
                    int counter = 1;
                    for (Attributed<Polygon> polygon : polygons) {
                        pr.println("  #" + counter++ + " POLYGON");
                        // pr.println("  AREA OF " +
                        // polygon.value().shell().area());
                        pr.println("    HAS "
                                + polygon.value().shell().points().size()
                                + " POINTS");
                        pr.println("    HAS " + polygon.value().holes().size()
                                + " HOLES");
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
