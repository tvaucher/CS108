package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * A way is a list of nodes, the nodes are in spherical coordinates (as opposed
 * to Cartesian coordinates). A Way is considered closed if its last node is the
 * same as the first one.
 * 
 * @see ch.epfl.imhof.osm.OSMNode
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class OSMWay extends OSMEntity {
    private final List<OSMNode> nodes;

    /**
     * Construct a new OSM Way. Its nodes are in spherical coordinates instead
     * of Cartesian coordinates.
     * 
     * @param id
     *            The unique ID of the entity. Every entity is referenced by its
     *            ID; for instance, the Allee de Savoie has an ID of 51740696,
     *            and information about it can be seen at
     * @see {@link http://www.openstreetmap.org/way/51740696}
     * @param nodes
     *            A list of nodes that will make up the way
     * @param attributes
     *            The attributes that are tied to the way.
     * @throws IllegalArgumentException
     *             A way isn't a valid object if there are less than two nodes,
     *             since the last should be the same as the first. Therefore, an
     *             exception will be thrown if there are less than 2 nodes in
     *             the way, and it won't be constructed.
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes)
            throws IllegalArgumentException {
        super(id, attributes);
        if (nodes.size() < 2)
            throw new IllegalArgumentException(
                    "List of node must contain at least 2 elements");
        this.nodes = Collections
                .unmodifiableList(new ArrayList<OSMNode>(nodes));
    }

    /**
     * Getter for the number of nodes in the way
     * 
     * @return nodes.size() The number of nodes in the way
     */
    public int nodesCount() {
        return nodes.size();
    }

    /**
     * Getter for the list of nodes (including the repeated first / last node)
     * 
     * @return nodes The list of nodes in the way (including the repeated first
     *         / last node)
     */
    public List<OSMNode> nodes() {
        return nodes;
    }

    /**
     * Getter for the list of nodes (excluding the last one, if it is the same
     * as the first one)
     * 
     * @return the list of nodes (excluding the last one, which is the same as
     *         the first one).
     */
    public List<OSMNode> nonRepeatingNodes() {
        if (isClosed()) {
            return Collections.unmodifiableList(new ArrayList<>(nodes.subList(
                    0, nodes.size() - 1)));
            // TODO JUnit absolutely on this one
        }
        return nodes;
    }

    /**
     * Getter for the first node of the way
     * 
     * @return the first node of the way
     */
    public OSMNode firstNode() {
        return nodes.get(0);
    }

    /**
     * Getter for the last node of the way
     * 
     * @return the last node of the way
     */
    public OSMNode lastNode() {
        return nodes.get(nodes.size() - 1);
    }

    /**
     * Returns true if the first node is the same as the last one, false
     * otherwise.
     * 
     * @return true iff the first node is the same as the last one.
     */
    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }

    /**
     * Builder associated to the OSMWay object. Allows you to construct an
     * OSMWay object step by step by adding one node at a time
     */
    public static final class Builder extends OSMEntity.Builder {
        private List<OSMNode> nodes;

        /**
         * Constructor for the Builder. Requires the ID that will be assigned to
         * the Way
         * 
         * @param id
         *            The unique ID that will be assigned to the way
         */
        public Builder(long id) {
            super(id);
            nodes = new ArrayList<>();
        }

        /**
         * Add a node to the way that will be constructed by the Builder.
         * 
         * @param newNode
         *            An OSMNode object that will be added to the way.
         */
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }

        /**
         * Build a new OSMWay object from the data that has been inputted into
         * the Builder
         * 
         * @return The newly created OSMWay object.
         * @throws IllegalStateException
         *             If the way is incomplete
         * @see ch.epfl.imhof.osm.OSMWay.Builder#isIncomplete()
         */
        public OSMWay build() throws IllegalStateException {
            if (isIncomplete())
                throw new IllegalStateException("Cannot build Incomplete Way");
            return new OSMWay(id, nodes, b.build());
        }

        /*
         * (non-Javadoc)
         * 
         * @see ch.epfl.imhof.osm.OSMEntity.Builder#isIncomplete() Return true
         * if the way has 2 nodes or fewer.
         */
        @Override
        public boolean isIncomplete() {
            if (nodes.size() < 2)
                return true;
            return super.isIncomplete();
        }
    }
}
