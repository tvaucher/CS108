package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

/**
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 *
 */
public abstract class OSMEntity {
    
    private final long id;
    private final Attributes attributes;
    
    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }
    
    public long id() {
        return id;
    }
    
    public Attributes attributes() {
        return attributes;
    }
    
    public boolean hasAttributes(String key) {
        return attributes.contains(key);
    }
    
    public String attributeValue(String key) {
        return attributes.get(key);
    }
    
    public static abstract class Builder {
        protected long id;
        private boolean incomplete;
        protected Attributes.Builder b = new Attributes.Builder();
        public Builder(long id) {
            this.id = id;
            incomplete = false;
        }
        
        public void setAttribute(String key, String value) {
            b.put(key, value);
        }
        
        public void setIncomplete() {
            incomplete = true;
        }
        
        public boolean isIncomplete() {
            return incomplete;
        }
        
    }
}
