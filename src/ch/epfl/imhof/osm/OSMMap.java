package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import static java.util.Objects.requireNonNull;

/**
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 *
 */
public final class OSMMap {
    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList<>(requireNonNull(ways)));
        this.relations = Collections.unmodifiableList(new ArrayList<>(requireNonNull(relations)));
    }
    
    public List<OSMWay> ways() {
        return ways;
    }
    
    public List<OSMRelation> relations() {
        return relations;
    }
    
    public static final class Builder {
        private HashMap<Long, OSMNode> nodes = new HashMap<>();
        private HashMap<Long, OSMWay> ways = new HashMap<>();
        private HashMap<Long, OSMRelation> relations = new HashMap<>();
        
        public void addNode(OSMNode newNode) {
            nodes.put(newNode.id(), newNode);
        }
        
        public OSMNode nodeForId(long id) {
            return nodes.get(id);
            //TODO test because long / Long shouldn't cause problem though
        }
        
        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }
        
        public OSMWay wayForId(long id) {
            return ways.get(id);
            //TODO same as before
        }
        
        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }
        
        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }
        
        public OSMMap build() {
            return new OSMMap(transformToList(ways), transformToList(relations));
        }
        
        private <T> List<T> transformToList (HashMap<Long, T> map) {
            List<T> list = new ArrayList<>();
            for (Entry<Long, T> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            return list;
        }
    }
}
