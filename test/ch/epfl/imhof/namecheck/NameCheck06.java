package ch.epfl.imhof.namecheck;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.Projection;

public class NameCheck06 {
    @Test
    @Ignore
    public void useAllStage06Names() {
        // OSMToGeoTransformer
        OSMMap om = null;
        Projection p = null;
        OSMToGeoTransformer t = new OSMToGeoTransformer(p);
        Map m1 = t.transform(om);

        // Map and Map.Builder
        Map.Builder mb = new Map.Builder();
        Attributed<Polygon> ap = null;
        mb.addPolygon(ap);
        Attributed<PolyLine> al = null;
        mb.addPolyLine(al);
        Map m2 = mb.build();

        List<Attributed<Polygon>> ps = m2.polygons();
        List<Attributed<PolyLine>> ls = m1.polyLines();
        m1 = new Map(ls, ps);
    }
}
