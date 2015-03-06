package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * In OSM, a node is a point on the surface of the Earth (in WGS 84
 * coordinates). Each node has its unique ID; for instance, node 35295150 is the
 * center of Lausanne. More information can be seen about it on OSM's website:
 * {@link http://www.openstreetmap.org/node/35295150}
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class OSMNode extends OSMEntity {
    private final PointGeo position;

    /**
     * Construct a new OSM node.
     * 
     * @param id
     *            The unique ID of the node. Every node is referenced by its ID;
     *            for instance, the center of Lausanne has an ID of 35295150,
     *            and information about it can be seen at {@link http
     *            ://www.openstreetmap.org/node/35295150}
     * @param position
     *            The position of the node in WGS 84 coordinates (in other
     *            words, a WGS 84 coordinates)
     * @param attributes
     *            The attributes that will be tied to the node object.
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        this.position = position;
    }

    /**
     * A getter for the node's position
     * 
     * @return position. The PointGeo object that describes the node's position
     *         using WGS 84 coordinates.
     */
    public PointGeo position() {
        return position;
    }

    /**
     * Builder associated to the OSMNode object. Allows you to construct an
     * OSMNode object safely and easily, since it manages illegal values.
     */
    public final static class Builder extends OSMEntity.Builder {
        private PointGeo position;

        /**
         * Constructor of the OSMNode Builder. Requires the ID that will be
         * assigned to the node and the node's position.
         * 
         * @param id
         *            The unique ID that will be assigned to the node.
         * @param position
         *            The node's position in WGS 84 coordinates.
         */
        public Builder(long id, PointGeo position) {
            super(id);
            this.position = position;
        }

        /**
         * Build a new OSMNode object from the data that has been inputted into
         * the Builder.
         * 
         * @return The newly created OSMNode object.
         * @throws IllegalStateException
         *             if the node is incomplete (if the setIncomplete method
         *             has been called on the Builder)
         */
        public OSMNode build() throws IllegalStateException {
            if (isIncomplete())
                throw new IllegalStateException("Cannot build Incomplete Node");
            return new OSMNode(id, position, b.build());
        }
    }
}