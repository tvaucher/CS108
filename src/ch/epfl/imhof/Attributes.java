package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Attributes {
    private final Map<String, String> attributes;
    public Attributes(Map<String, String> attributes) {
        this.attributes = Collections.unmodifiableMap(new HashMap<String, String>(attributes));
    }
    
    public boolean isEmpty() {
        return attributes.isEmpty();
    }
    
    public boolean contains(String key) {
        return attributes.containsKey(key);
    }
    
    public String get(String key) {
        return attributes.get(key);
    }
    
    public String get(String key, String defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }
    
    public int get(String key, int defaultValue) {
        try {
            return Integer.parseInt(this.get(key));
        }
        catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Attributes.Builder builder = new Attributes.Builder(); //Using a builder to avoid code redundancy
        for (String key : keysToKeep) {
            if (keysToKeep.contains(key)) {
                builder.put(key, attributes.get(key));
            }
        }
        return builder.build();
    }
    
    public static final class Builder {
        private Map<String, String> attributes = new HashMap<String, String>();
        
        public void put(String key, String value) {
            attributes.put(key, value);            
        }
        
        public Attributes build() {
            return new Attributes(attributes);
        }
    }
    
}
