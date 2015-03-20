package ch.epfl.imhof.osm;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * The OSMMap (OpenStreetMaps Map) represents the actual map, a collection of
 * ways and relations.
 * 
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class OSMMap {
    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    /**
     * Construct a new OSMMap object.
     * 
     * @param ways
     *            A collection of OSMWay objects
     * @param relations
     *            A collection of OSMRelation objects
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList<>(
                requireNonNull(ways)));
        this.relations = Collections.unmodifiableList(new ArrayList<>(
                requireNonNull(relations)));
    }

    /**
     * Getter for the list of ways.
     * 
     * @return ways The list of ways in the map.
     */
    public List<OSMWay> ways() {
        return ways;
    }

    /**
     * Getter for the list of relations.
     * 
     * @return relations The list of relations in the map.
     */
    public List<OSMRelation> relations() {
        return relations;
    }

    /**
     * A Builder that allows you to gradually build the OSMMap object by adding
     * nodes and relations one at a time.
     */
    public static final class Builder {
        private HashMap<Long, OSMNode> nodes = new HashMap<>();
        private HashMap<Long, OSMWay> ways = new HashMap<>();
        private HashMap<Long, OSMRelation> relations = new HashMap<>();

        /**
         * Add an OSMNode to the list of nodes that will be included in the map.
         * 
         * @param newNode
         *            The node that will be added to the map.
         */
        public void addNode(OSMNode newNode) {
            nodes.put(newNode.id(), newNode);
        }

        /**
         * Getter for the node with the id given as parameter.
         * 
         * @param id
         *            The unique ID of the node that should be returned
         * @return The node corresponding to the ID; null if there is no node
         *         with that ID in the map builder yet.
         */
        public OSMNode nodeForId(long id) {
            return nodes.get(id);
            // TODO test because long / Long shouldn't cause problem though
        }

        /**
         * Add an OSMWay to the list of ways that will be included in the map.
         * 
         * @param newWay
         *            The way that will be added to the map.
         */
        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }

        /**
         * Getter for the way with the id given as parameter.
         * 
         * @param id
         *            The unique ID of the way that should be returned
         * @return The node corresponding to the ID; null if there is no node
         *         with that ID in the map builder yet.
         */
        public OSMWay wayForId(long id) {
            return ways.get(id);
            // TODO test because long / Long shouldn't cause problem though
        }

        /**
         * Add an OSMRelation to the list of relations that will be included in
         * the map.
         * 
         * @param newRelation
         *            The relation that will be included in the map
         */
        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }

        /**
         * Getter for the relation with the id given as parameter.
         * 
         * @param id
         *            The unique ID of the relation that should be returned
         * @return The relation corresponding to the ID; null if there is no
         *         relation with that ID in the map builder yet.
         */
        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }

        /**
         * Build a new OSMMap object with the data that has been inputted into
         * this builder.
         * 
         * @return A new OSMMap object containing the data that has been
         *         inputted into this builder.
         */
        public OSMMap build() {
            printMapInFile();
            return new OSMMap(ways.values(), relations.values());
        }


        /**
         * A private method for debugging purposes that creates a file in
         * data/debug
         */
        private void printMapInFile() { // FOR DEBUGING PURPOSES
            File debugDirectory = new File("data/debug");
            if (debugDirectory.exists() && debugDirectory.isDirectory()) {
                // Will normally be true only locally
                try (PrintWriter pr = new PrintWriter(new File("data/debug/OSMMap_"
                        + System.currentTimeMillis() + ".txt"))) {
                    long startTime = System.currentTimeMillis();
                    pr.println("START PRINTING OSMMAP\n");
                    pr.println("MAP HAS " + nodes.size() + " NODES");
                    pr.println("PRINTING " + ways.size() + " WAYS");
                    /*for (Entry<Long, OSMWay> way : ways.entrySet()) {
                        pr.println("  Way : id " + way.getKey() + ", contains "
                                + way.getValue().nodesCount() + " nodes");
                    }*/
                    pr.println("\nPRINTING " + relations.size() + " RELATIONS");
                    int polygonCounter = 0;
                    for (Entry<Long, OSMRelation> relation : relations.entrySet()) {
                        String type = relation.getValue().attributeValue("type");
                        String multi = "multipolygon";
                        if (type != null && type.equals(multi)) {
                            polygonCounter++;
                        }
                    }
                    pr.println("THERE'S " + polygonCounter + " MULTYPOLIGON IN OSMMAP");
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
