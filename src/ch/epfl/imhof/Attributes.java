package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static java.util.Objects.requireNonNull;

/**
 * Immutable class that consists of a map of attributes and their associated
 * value These attributes describe the non-geometrical features of the entities
 * e.g. the color for forests, the name of a lake, ...
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 *
 */
public final class Attributes {
    private final Map<String, String> attributes;

    /**
     * Constructs an attribute map based on a given map.
     * 
     * @param attributes
     *            Set of couple (attributes, values)
     */
    public Attributes(Map<String, String> attributes) {
        this.attributes = Collections.unmodifiableMap(new HashMap<>(
                requireNonNull(attributes)));
    }

    /**
     * Checks whether the map attributes is empty or not.
     * 
     * @return true if attributes is empty, false otherwise.
     */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    /**
     * Checks whether the map attributes contains the given key.
     * 
     * @param key
     *            key to be checked if contained
     * @return true if key is contained, false otherwise.
     */
    public boolean contains(String key) {
        return attributes.containsKey(key);
    }

    /**
     * Gets the associated value to the key in the map attributes, or null if
     * this map contains no mapping for the key.
     * 
     * @param key
     *            key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this
     *         map contains no mapping for the key
     */
    public String get(String key) {
        return attributes.get(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or defaultValue
     * if this map contains no mapping for the key.
     * 
     * @param key
     *            key whose associated value is to be returned
     * @param defaultValue
     *            default mapping of the key
     * @return value to which the specified key is mapped, or defaultValue if
     *         this map contains no mapping for the key
     */
    public String get(String key, String defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }

    /**
     * Returns the value to which the specified key is mapped, or defaultValue
     * if the map contains no mapping or when the retrieved value can't be
     * parsed.
     * 
     * @param key
     *            key whose associated value is to be returned
     * @param defaultValue
     *            default mapping of the key
     * @return integer to which the key is mapped, or defaultValue if there's no
     *         corresponding map or when the parsing fails
     */
    public int get(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key)); // Throw a NFE if "get" returns
                                               // null
        } catch (NumberFormatException e) { // Thus if key doesn't exist we get
                                            // default result
            return defaultValue;
        }
    }

    /**
     * Constructs a new instance trough the associated builder with a specified
     * list of keys to keep. Other keys and associated value are not copied.
     * 
     * @param keysToKeep
     *            the set of keys that need to be kept in the new object.
     * @return a newly built Attributes object containing only the keys to keep.
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Attributes.Builder builder = new Attributes.Builder(); // Using a
                                                               // builder to
                                                               // avoid code
                                                               // redundancy
        for (String key : keysToKeep) {
            if (contains(key)) {
                builder.put(key, get(key));
            }
        }
        return builder.build();
    }

    /**
     * Builder associated to the Attributes class. Allows us to construct an
     * Attributes object step by step by adding (key, value) couples in the
     * attributes map.
     * 
     */
    public static final class Builder {
        private Map<String, String> attributes = new HashMap<String, String>();

        /**
         * Adds a (key, value) couple in the map of attributes that will be
         * built. If the key already exists in the map, the value is replaced.
         * 
         * @param key
         *            key to be inserted in the map
         * @param value
         *            value to be inserted in the map
         */
        public void put(String key, String value) {
            attributes.put(key, value);
        }

        /**
         * Build a new Attributes object based on the data that was inputted
         * into this builder.
         * 
         * @return the newly built Attributes object
         */
        public Attributes build() {
            return new Attributes(attributes);
        }
    }

}
