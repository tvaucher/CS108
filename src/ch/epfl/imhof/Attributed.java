package ch.epfl.imhof;

import static java.util.Objects.requireNonNull;

/**
 * Immutable generic class.
 * Represents an entity of type T associated to an Attributes object
 * e.g. a Polygon with a list of attributes.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 *
 * @param <T> The type of entity of the generic object
 */
public final class Attributed<T> {
    private final T value;
    private final Attributes attributes;
    /**
     * Construct the object of a specified type and a list of attributes
     * 
     * @param value
     * Of the specified type
     * @param attributes
     * Associated list of attributes
     */
    public Attributed(T value, Attributes attributes) {
        this.value = requireNonNull(value);
        this.attributes = requireNonNull(attributes);
    }
    
    /**
     * Return the value attribute of type T
     * 
     * @return value
     */
    public T value() {
        return value;
    }
    
    /**
     * Return the list of attributes associated to the current instance
     * 
     * @return attributes
     */
    public Attributes attributes() {
        return attributes;
    }
    
    /**
     * @see ch.epfl.imhof.Attributes#contains(String)
     * Check whether the map attributes contains the given key.
     * 
     * @param key
     *            key to be checked if contained
     * @return true if key is contained, false otherwise.
     */
    public boolean hasAttribute(String attributeName) {
        return attributes.contains(attributeName);
    }
    
    /**
     * @see ch.epfl.imhof.Attributes#get(String)
     * Get the associated value to the key in the map attributes, or null if
     * this map contains no mapping for the key.
     * 
     * @param key
     *            key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this
     *         map contains no mapping for the key
     */
    public String attributeValue(String attributeName) {
        return attributes.get(attributeName);
    }
    
    /**
     * @see ch.epfl.imhof.Attributes#get(String, String)
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
    public String attributeValue(String attributeName, String defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }
    
    /**
     * @see ch.epfl.imhof.Attributes#get(String, int)
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
    public int attributeValue(String attributeName, int defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }
}
