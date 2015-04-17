package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

public final class Filters {
    private Filters() {}
    
    public static Predicate<Attributed<?>> tagged(String attributeName) {
        return t -> t.hasAttribute(attributeName);
    }
    
    public static Predicate<Attributed<?>> tagged(String attributeName, String... attributeValues) {
        return t -> {
            if (t.hasAttribute(attributeName))
                for (String attributeValue : attributeValues)
                    if (t.attributeValue(attributeName).equals(attributeValue))
                        return true;
            return false;
        };
    }
    
    public static Predicate<Attributed<?>> onLayer(int layer) {
        return t -> t.attributeValue("layer", 0) == layer;
    }
}
