package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * A Filter's goal is to determine whether an entity should be kept or not.
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 */
public final class Filters {
    /**
     * The constructor is private and empty; the class in non-instantiable.
     */
    private Filters() {
    }

    /**
     * A predicate that is true only if the Attributed entity has an attribute
     * with the given name.
     * 
     * @param attributeName
     *            The name of the attribute that should be checked for.
     * @return A boolean stating whether the entity has such an attribute or
     *         not.
     */
    public static Predicate<Attributed<?>> tagged(String attributeName) {
        return t -> t.hasAttribute(attributeName);
    }

    /**
     * A predicate that is true only if the Attributed entity has an attribute
     * with the given name and if the value corresponding to said attribute is a
     * part of the given attribute values.
     * 
     * @param attributeName
     *            The name of the attribute that should be checked for.
     * @param attributeValues
     *            The value that the attribute should have in order for the
     *            Predicate to return true.
     * @return A boolean stating whether the entity has such an attribute with
     *         such a value or not.
     */
    public static Predicate<Attributed<?>> tagged(String attributeName,
            String... attributeValues) {
        return t -> {
            if (t.hasAttribute(attributeName))
                for (String attributeValue : attributeValues)
                    if (t.attributeValue(attributeName).equals(attributeValue))
                        return true;
            return false;
        };
    }

    /**
     * A predicate that is true only if the Attributed entity is on the given
     * layer.
     * <p>
     * If no layer is specified, then it will be assumed that the entity is on
     * layer 0.
     * 
     * @param layer
     *            The layer that we should check for; should be contained in
     *            [-5, 5]
     * @return A boolean stating whether the entity is on said layer or not.
     */
    public static Predicate<Attributed<?>> onLayer(int layer) {
        return t -> t.attributeValue("layer", 0) == layer;
    }
}
