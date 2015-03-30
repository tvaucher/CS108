package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import static java.util.Objects.requireNonNull;

/**
 * An Open Street Maps (OSM) Entity is an abstract data structure that
 * represents a way, a node or a relation.
 * 
 * @see ch.epfl.imhof.osm.OSMNode
 * @see ch.epfl.imhof.osm.OSMRelation
 * @see ch.epfl.imhof.osm.OSMWay
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public abstract class OSMEntity {

    private final long id;
    private final Attributes attributes;

    /**
     * Constructs a new OSM entity.
     * 
     * @param id
     *            The unique ID of the entity. Every entity is referenced by its
     *            ID; for instance, the EPFL RLC's relation is 331569, and
     *            information about it can be seen at:
     * @see {@link http://www.openstreetmap.org/relation/331569}
     * @param attributes
     *            An Attributes object. @see ch.epfl.imhof.Attributes
     */
    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = requireNonNull(attributes);
    }

    /**
     * A getter for the ID.
     * 
     * @return id The unique ID of the entity.
     */
    public long id() {
        return id;
    }

    /**
     * A getter for the attributes.
     * 
     * @return attributes The attributes object that is tied to this entity. @see
     *         ch.epfl.imhof.Attributes
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * Returns true if and only if the entity's attributes contain a certain
     * key.
     * 
     * @param key
     *            The key that should be checked for.
     * @return true iff the attributes contain the key
     */
    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }

    /**
     * Gets the key's associated value in the attributes, or null if the
     * attributes don't contain the key.
     * 
     * @param key
     *            The key that should be checked for.
     * @return the value to which the specified key is mapped, or null if
     *         there's no mapping for the key
     */
    public String attributeValue(String key) {
        return attributes.get(key);
    }

    /**
     * Abstract builder associated to the OSMEntity object. Allows you to
     * construct an Attributes object step by step by adding couples of (key,
     * value) in the attributes map
     */
    public static abstract class Builder {
        protected long id;
        private boolean incomplete;
        protected Attributes.Builder b = new Attributes.Builder();

        /**
         * Constructor for the Builder. Requires the unique ID of the future
         * Entity
         * 
         * @param id
         *            The unique ID of the entity.
         */
        public Builder(long id) {
            this.id = id;
            incomplete = false;
        }

        /**
         * Adds a (key, value) couple to the the map of attributes that will be
         * built into the Entity.
         * 
         * @see ch.epfl.imhof.Attributes
         * 
         * @param key
         *            The key to be added.
         * @param value
         *            The value associated to the key.
         */
        public void setAttribute(String key, String value) {
            b.put(key, value);
        }

        /**
         * Sets the incompletion to true, which means that the builder won't be
         * able to build.
         */
        public void setIncomplete() {
            incomplete = true;
        }

        /**
         * Returns whether the entity is complete or not.
         * 
         * @return incomplete Whether the entity is complete or not.
         */
        public boolean isIncomplete() {
            return incomplete;
        }

    }
}
