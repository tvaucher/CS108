package ch.epfl.imhof.namecheck;

import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMEntity;
import ch.epfl.imhof.osm.OSMNode;
import ch.epfl.imhof.osm.OSMRelation;
import ch.epfl.imhof.osm.OSMWay;

public class NameCheck04 {
    @Test
    @Ignore
    public void useAllStage04Names() {
        // OSMEntity and OSMEntity.Builder
        OSMEntity e = Collections.<OSMEntity>emptyList().get(0);
        long l = e.id();
        Attributes as = e.attributes();
        boolean b = e.hasAttribute("");
        String s = e.attributeValue("");

        OSMEntity.Builder eb = Collections.<OSMEntity.Builder>emptyList().get(0);
        eb.setAttribute(s, s);
        eb.setIncomplete();
        b = eb.isIncomplete();

        // OSMNode and OSMNode.Builder
        PointGeo pg = new PointGeo(0,0);
        e = new OSMNode(l, pg, as);
        OSMNode n = (OSMNode)e;
        pg = n.position();

        eb = new OSMNode.Builder(l, pg);
        OSMNode.Builder nb = (OSMNode.Builder)eb;
        n = nb.build();

        // OSMWay and OSMWay.Builder
        e = new OSMWay(l, Collections.singletonList(n), as);
        OSMWay w = (OSMWay)e;
        int i = w.nodesCount();
        List<OSMNode> ln = w.nodes();
        ln = w.nonRepeatingNodes();
        n = w.firstNode();
        n = w.lastNode();
        b = w.isClosed();

        eb = new OSMWay.Builder(l);
        OSMWay.Builder wb = (OSMWay.Builder)eb;
        wb.addNode(n);
        w = wb.build();

        // OSMRelation.Member and OSMRelation.Member.Type
        OSMRelation.Member.Type t = OSMRelation.Member.Type.NODE;
        t = OSMRelation.Member.Type.WAY;
        t = OSMRelation.Member.Type.RELATION;
        OSMRelation.Member m = new OSMRelation.Member(t, s, e);

        // OSMRelation and OSMRelation.Builder
        e = new OSMRelation(l, Collections.singletonList(m), as);
        OSMRelation r = (OSMRelation)e;
        List<OSMRelation.Member> lm = r.members();

        eb = new OSMRelation.Builder(l);
        OSMRelation.Builder rb = (OSMRelation.Builder)eb;
        rb.addMember(t, s, r);
        r = rb.build();

        // Use all otherwise-unused variables, to prevent warnings
        System.out.println("" + b + i + ln + lm);
    }
}
