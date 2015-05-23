package ch.epfl.imhof;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.Point;
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
    private final List<Attributed<Point>> places;
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
    public Map(List<Attributed<Point>> places,
            List<Attributed<PolyLine>> polyLines,
            List<Attributed<Polygon>> polygons) {
        this.places = Collections.unmodifiableList(new ArrayList<>(
                requireNonNull(places)));
        this.polyLines = Collections.unmodifiableList(new ArrayList<>(
                requireNonNull(polyLines)));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(
                requireNonNull(polygons)));
    }

    /**
     * A getter for the places of the map.
     * 
     * @return a list of all the attributed places in the map
     */
    public List<Attributed<Point>> places() {
        return places;
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
        private final List<Attributed<Point>> places = new ArrayList<>();
        private final List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        private final List<Attributed<Polygon>> polygons = new ArrayList<>();

        /**
         * Adds an attributed PointGeo to the list of places that will be a part
         * of the map.
         * 
         * @param newPlace
         *            a PointGeo with attributes.
         */
        public void addPlace(Attributed<Point> newPlace) {
            if (isNew(newPlace.attributeValue("name")))
                places.add(newPlace);
        }

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
            return new Map(places, polyLines, polygons);
        }

        private boolean isNew(String name) {
            for (Attributed<Point> p : places) {
                if (p.attributeValue("name").equals(name))
                    return false;
            }
            return true;
        }
    }
}
