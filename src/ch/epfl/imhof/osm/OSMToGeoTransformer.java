package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.OpenPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.projection.Projection;

public final class OSMToGeoTransformer {
    private final Projection projection;
    private final HashSet<String> closedAttributes = new HashSet<>(
            (Arrays.asList("aeroway", "amenity", "building", "harbour",
                    "historic", "landuse", "leisure", "man_made", "military",
                    "natural", "office", "place", "power", "public_transport",
                    "shop", "sport", "tourism", "water", "waterway", "wetland")));
    private final HashSet<String> polyLineAttributes = new HashSet<>(
            (Arrays.asList("bridge", "highway", "layer", "man_made", "railway",
                    "tunnel", "waterway")));
    private final HashSet<String> polygonAttributes = new HashSet<>(
            (Arrays.asList("building", "landuse", "layer", "leisure",
                    "natural", "waterway")));
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }
    
    public Map transform(OSMMap map) {
        //Declaration of vars for this method
        final Map.Builder builder = new Map.Builder();
        final AreaComparator comparator = new AreaComparator();
        final List<OSMWay> ways = map.ways();
        final List<OSMRelation> relations = map.relations();
        
        for (OSMWay way : ways) {
            transformWay(way, builder);
        }
        
        for (OSMRelation relation : relations) {
            transformRelation(relation, builder);
        }
        
        return builder.build();
    }
    
    ////////////////////////////       
    // TRANSFORMATION OF WAYS //
    ////////////////////////////
    private List<Point> nodesToPoints(List<OSMNode> nodes) {
        ArrayList<Point> points = new ArrayList<>(nodes.size());
        for (OSMNode node : nodes) {
            points.add(projection.project(node.position()));
        }
        return points;
    }
    
    private void transformWay(OSMWay way, Map.Builder builder) {
        Attributes currentAttributes = way.attributes();
        if (way.isClosed() && isArea(currentAttributes)) {
            ClosedPolyLine closedLine = new ClosedPolyLine(nodesToPoints(way.nonRepeatingNodes()));
            currentAttributes = currentAttributes.keepOnlyKeys(polygonAttributes);
            if (!currentAttributes.isEmpty()) {
                builder.addPolygon(new Attributed<>(new Polygon(closedLine), currentAttributes));
            }
        }
        else {
            currentAttributes = currentAttributes.keepOnlyKeys(polyLineAttributes);
            if (!currentAttributes.isEmpty()) {
                if (way.isClosed()) {
                    builder.addPolyLine(new Attributed<>(new ClosedPolyLine(nodesToPoints(way.nodes())), currentAttributes));
                }
                else {
                    builder.addPolyLine(new Attributed<>(new OpenPolyLine(nodesToPoints(way.nodes())), currentAttributes));
                }
            }
        }
    }
    
    /**
     * Returns whether a way is closed or not, based on the two criteria described in the project documentation:
     * - has the attribute "area" with a value of "yes", "1" or "true"
     * - has one of the attributes from closedAttributes
     * 
     * @param entityAttributes
     *          The attributes of the entity that will be checked.
     * @return Whether the entity is closed or or not.
     */
    private boolean isArea(Attributes entityAttributes) {
        String area = entityAttributes.get("area");
        if (area.equals("yes") || area.equals("true") || area.equals("1")) {
            return true;
        }
        for (String attribute : closedAttributes) {
            if (entityAttributes.contains(attribute)) {
                return true;
            }
        }
        return false;
    }
    
    
    /////////////////////////////////
    // TRANSFORMATION OF RELATIONS //
    /////////////////////////////////
    private void transformRelation(OSMRelation relation, Map.Builder builder) {
        // First of all, put the ways into 2 categories: inner and outer.
        List<OSMEntity> inner = new ArrayList<>();
        List<OSMEntity> outer = new ArrayList<>();
        for (OSMRelation.Member relationMember : relation.members()) {
            if (relationMember.type().equals(OSMRelation.Member.Type.WAY)) {
                if (relationMember.role().equals("inner"))
                    inner.add(relationMember.member());
                else if (relationMember.role().equals("outer"))
                    outer.add(relationMember.member());
            }
        }
        
        //Then, build the rings for both inner and outer ways.
        Graph.Builder<OSMWay> outerRingBuilder = new Graph.Builder<>();
        for (int i = 0; i < outer.size(); ++i) {
            OSMWay currentWay = (OSMWay) outer.get(i);
            for (int j = 0; j < currentWay.nodes().size(); ++i) {
                // TODO add to outerRingBuilder
            }
            // TODO add last node of this to first node of next
        }
        // TODO add last node of the relation to the first node.
        
    }
    
    
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        //TODO this method does step 1 and 2
        return null;
    }
    
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes) {
        //TODO this method does step 3
        return null;
    }
    
    private class AreaComparator implements Comparator<ClosedPolyLine> {
        private final double DELTA = 1e-5;
        public int compare(ClosedPolyLine p1, ClosedPolyLine p2) {
            double difference = p1.area() - p2.area();
            if (Math.abs(difference) <= DELTA)
                return 0; //surfaces are equals
            else if (difference < 0)
                return -1; //p2 is bigger than p1
            else
                return 1; //p1 is bigger than p2
        }
    }
    
}
