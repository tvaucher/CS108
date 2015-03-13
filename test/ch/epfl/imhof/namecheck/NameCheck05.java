package ch.epfl.imhof.namecheck;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import ch.epfl.imhof.Graph;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMNode;
import ch.epfl.imhof.osm.OSMRelation;
import ch.epfl.imhof.osm.OSMWay;

public class NameCheck05 {
    @Test
    @Ignore
    public void useAllStage05Names() throws SAXException, IOException {
        // OSMMapReader, OSMMap, OSMMap.Builder
        OSMMap m1 = OSMMapReader.readOSMFile("", false);
        Collection<OSMWay> cw = Collections.emptyList();
        Collection<OSMRelation> cr = Collections.emptySet();
        OSMMap m2 = new OSMMap(cw, cr);
        List<OSMWay> lw = m1.ways();
        List<OSMRelation> lr = m2.relations();
        OSMMap.Builder mb = new OSMMap.Builder();
        OSMNode n = mb.nodeForId(0l);
        mb.addNode(n);
        OSMWay w = mb.wayForId(0l);
        mb.addWay(n == mb.nodeForId(12l) ? lw.get(0) : w);
        OSMRelation r = mb.relationForId(0l);
        mb.addRelation((n == mb.nodeForId(12l)) ? lr.get(0) : r);
        m1 = mb.build();

        // Graph, Graph.Builder
        Graph.Builder<Integer> gb = new Graph.Builder<>();
        gb.addNode(1);
        gb.addEdge(2, 3);
        Graph<Integer> g = gb.build();
        Set<Integer> ns = g.nodes();
        ns = g.neighborsOf(ns.iterator().next());
    }
}
