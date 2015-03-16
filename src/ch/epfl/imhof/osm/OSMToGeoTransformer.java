package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.OpenPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.projection.Projection;

public final class OSMToGeoTransformer {
    private final Projection projection;
    private final HashSet<String> wayAttributes = new HashSet<>(
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
        
        //TODO shitty method
        return builder.build();
    }
       
    //TRANSFORMATION OF WAYS
    private List<Point> nodesToPoints(List<OSMNode> nodes) {
        ArrayList<Point> points = new ArrayList<>(nodes.size());
        for (OSMNode node : nodes) {
            points.add(projection.project(node.position()));
        }
        return points;
    }
    
    private void transformWay(OSMWay way, Map.Builder builder) {
        Attributes currentAttributes = way.attributes();
        if (way.isClosed()) {
            ClosedPolyLine closedLine = new ClosedPolyLine(nodesToPoints(way.nonRepeatingNodes()));
            String areaValue = way.attributeValue("area");
            if (areaValue.equals("1") || areaValue.equals("yes") || areaValue.equals("true")) {
                currentAttributes = currentAttributes.keepOnlyKeys(polygonAttributes);
                if (!currentAttributes.isEmpty())
                    builder.addPolygon(new Attributed<>(new Polygon(closedLine), currentAttributes));
            }
            else {
                if (isArea(currentAttributes)) {
                    currentAttributes = currentAttributes.keepOnlyKeys(polygonAttributes);
                    if (!currentAttributes.isEmpty())
                        builder.addPolygon(new Attributed<>(new Polygon(closedLine), currentAttributes));
                }
                else {
                    currentAttributes = currentAttributes.keepOnlyKeys(polyLineAttributes);
                    if (!currentAttributes.isEmpty())
                        builder.addPolyLine(new Attributed<>(closedLine, currentAttributes));
                }
            }
        }
        else {
            currentAttributes = currentAttributes.keepOnlyKeys(polyLineAttributes);
            if (!currentAttributes.isEmpty())
                builder.addPolyLine(new Attributed<>(new OpenPolyLine(nodesToPoints(way.nodes())), currentAttributes));
        }
    }
    
    private boolean isArea(Attributes attributes) {
        for (String attribute : wayAttributes)
            if (attributes.contains(attribute))
                return true;
        return false;
    }
    
    //TRANSFORMATION OF RELATION
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
                return -1; //p2 is wider than p1
            else
                return 1; //p1 is wider than p2
        }
    }
    
}
