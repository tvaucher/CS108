package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
     * Construct an attribute map based on a given map.
     * 
     * @param attributes
     *            Set of couple (attributes, values)
     */
    public Attributes(Map<String, String> attributes) {
        this.attributes = Collections
                .unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * Check whether the map attributes is empty or not.
     * 
     * @return true if attributes is empty, false otherwise.
     */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    /**
     * Check whether the map attributes contains the given key.
     * 
     * @param key
     *            key to be checked if contained
     * @return true if key is contained, false otherwise.
     */
    public boolean contains(String key) {
        return attributes.containsKey(key);
    }

    /**
     * Get the associated value to the key in the map attributes, or null if
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
            return Integer.parseInt(get(key));//, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Construct a new instance trough the associated builder with a specified
     * list of key to keep. Other keys and associated value are not copied.
     * 
     * @param keysToKeep
     *            Set of keys that need to be keeped in the new object.
     * @return the newly built Attributes
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Attributes.Builder builder = new Attributes.Builder(); // Using a builder to avoid code redundancy
        for (String key : keysToKeep) {
            if (contains(key)) {
                builder.put(key, get(key));
            }
        }
        return builder.build();
    }

    /**
     * Builder associated to the Attributes class. Allow to construct an
     * Attributes objet step by step by adding couple (key, value) in the
     * attributes map
     * 
     */
    public static final class Builder {
        private Map<String, String> attributes = new HashMap<String, String>();

        /**
         * put the couple (key, value) in the attributes map. if the key already
         * exists in the map, the value is replaced.
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
         * Build a new Attributes object based on the builder.
         * 
         * @return newly built Attributes
         */
        public Attributes build() {
            return new Attributes(attributes);
        }
    }

}
