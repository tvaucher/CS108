package ch.epfl.imhof.osm;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
            printMapInFile();
            return new OSMMap(transformToList(ways), transformToList(relations));
        }
        
        private <T> List<T> transformToList (HashMap<Long, T> map) {
            List<T> list = new ArrayList<>();
            for (Entry<Long, T> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
            return list;
        }
        
        private void printMapInFile() { //FOR DEBUGING PURPOSE
            try (FileWriter fr = new FileWriter(new File("data/debug/map" + System.currentTimeMillis() + ".txt"))) {
                long startTime = System.currentTimeMillis();
                fr.write("START PRINTING MAP\n\n");
                fr.write("MAP HAS " + nodes.size() + " NODES\n");
                fr.write("PRINTING " + ways.size() + " WAYS\n");
                for (Entry<Long, OSMWay> way : ways.entrySet()) {
                    fr.write("  Way : id " + way.getKey() + ", contains "
                            + way.getValue().nodesCount() + " nodes\n");
                }
                fr.write("\nPRINTING " + relations.size()
                        + " RELATIONS\n");
                for (Entry<Long, OSMRelation> relation : relations.entrySet()) {
                    fr.write("  Relation : id " + relation.getKey()
                            + "\n    has " + relation.getValue().members().size()
                            + " members\n");
                    /*for (OSMRelation.Member member : relation.getValue()
                            .members()) {
                        fr.write("  Member : role " + member.role()
                                + ", type : " + member.type()+ "\n");
                    }*/
                    fr.write("    Relation attribute \"type\" value " + relation.getValue().attributeValue("type") + "\n");
                }
                fr.write("\nWRITED IN " + (System.currentTimeMillis() - startTime)/1000. + " sec");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
