package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.OpenPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
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
    
    /////////////////////////////////
    // TRANSFORMATION OF RELATIONS //
    /////////////////////////////////
    // To get an idea of how relations behave, see https://gist.github.com/MaximeKjaer/ac694beccf722a33853c
    
    private void transformRelation(OSMRelation relation, Map.Builder builder) {
        String type = relation.attributeValue("type"); //Because can be null + can't call equals on null object
        if (type != null && type.equals("multipolygon")) {
            Attributes attributes = relation.attributes().keepOnlyKeys(polygonAttributes);
            if (!attributes.isEmpty()) {
                List<Attributed<Polygon>> polygons = assemblePolygon(relation, attributes);
                for (Attributed<Polygon> polygon : polygons) {
                    builder.addPolygon(polygon);
                }
            }
        }
    }
    
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        // First of all, put the ways of the relation into 2 categories: inner and outer.
        List<OSMWay> ways = new ArrayList<>();
        for (OSMRelation.Member relationMember : relation.members()) {
            if (relationMember.type().equals(OSMRelation.Member.Type.WAY) && relationMember.role().equals(role)) {
                    ways.add((OSMWay) relationMember.member());
            }
        }
        //Then, build the rings for both inner and outer ways.
        Graph.Builder<OSMNode> graphBuilder = new Graph.Builder<>();
        for (int i = 0; i < ways.size(); ++i) {
            OSMWay currentWay = ways.get(i);
            OSMNode previousNode = null;
            for (int j = 0; j < currentWay.nodesCount(); ++j) {
                OSMNode currentNode = currentWay.nodes().get(j);
                graphBuilder.addNode(currentNode);
                if (j != 0) {
                    graphBuilder.addEdge(currentNode, previousNode);
                }
                previousNode = currentNode;
            }
        }
        //Finally, convert the Graph to rings
        Graph<OSMNode> graph = graphBuilder.build();
        List<OSMNode> unvisited = new ArrayList<>(graph.nodes());
        List<ClosedPolyLine> ring = new ArrayList<>();
        while (!unvisited.isEmpty()) {
            PolyLine.Builder polyLineBuilder = new PolyLine.Builder();
            OSMNode first = unvisited.get(0);
            unvisited.remove(0);
            polyLineBuilder.addPoint(nodeToPoint(first));
            Set<OSMNode> neighbors = graph.neighborsOf(first);
            do {
                if (neighbors.size() == 2) { // Assuming that every node has 2 neighbors
                    neighbors.retainAll(unvisited);
                    if (neighbors.size() >= 1) {
                        OSMNode next = new ArrayList<>(neighbors).get(0);
                        unvisited.remove(next);
                        polyLineBuilder.addPoint(nodeToPoint(next));
                        neighbors = graph.neighborsOf(next);                        
                    }
                    
                    else if (neighbors.size() == 0) { // End of the ring
                        ring.add(polyLineBuilder.buildClosed());
                    }
                    
                }
                else { // Error
                    return Collections.emptyList();
                }
            } while (neighbors.size() > 0);
        }
        
        return ring;
    }
    
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes) {
        List<ClosedPolyLine> innerRings = ringsForRole(relation, "inner");
        List<ClosedPolyLine> outerRings = ringsForRole(relation, "outer");
        
        java.util.Map<ClosedPolyLine, List<ClosedPolyLine>> polygonMap = new HashMap<>();
        for (ClosedPolyLine outerRing : outerRings) {
            polygonMap.put(outerRing, new ArrayList<ClosedPolyLine>());
        }
        
        AreaComparator comparator = new AreaComparator();
        outerRings.sort(comparator);
        for (ClosedPolyLine innerRing : innerRings) {
            for (ClosedPolyLine outerRing : outerRings) {
                if (outerRing.containsPoint(innerRing.firstPoint())) {
                    polygonMap.get(outerRing).add(innerRing);
                }
            }
        }
        
        List<Attributed<Polygon>> out = new ArrayList<>();
        for (Entry<ClosedPolyLine, List<ClosedPolyLine>> polygon : polygonMap.entrySet()) {
            out.add(new Attributed<Polygon>(new Polygon(polygon.getKey(), polygon.getValue()), attributes));
        }
        
        return out;
    }
    
    //////////////////////
    // Utlility methods //
    //////////////////////
    
    private List<Point> nodesToPoints(List<OSMNode> nodes) {
        ArrayList<Point> points = new ArrayList<>(nodes.size());
        for (OSMNode node : nodes) {
            points.add(nodeToPoint(node));
        }
        return points;
    }
    
    private Point nodeToPoint(OSMNode node) {
        return projection.project(node.position());        
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
        if (area != null && (area.equals("yes") || area.equals("true") || area.equals("1"))) {
            return true;
        }
        for (String attribute : closedAttributes) {
            if (entityAttributes.contains(attribute)) {
                return true;
            }
        }
        return false;
    }
    
    private class AreaComparator implements Comparator<ClosedPolyLine> {
        private final double DELTA = 1e-5;
        public int compare(ClosedPolyLine p1, ClosedPolyLine p2) {
            double difference = p1.area() - p2.area();
            if (Math.abs(difference) <= DELTA)
                return 0; //surfaces are equals
            else if (difference < 0)
                return -1; //p2 is larger than p1
            else
                return 1; //p1 is larger than p2
        }
    }
    
}
