package ch.epfl.imhof.osm;

import static org.junit.Assert.*;
import static ch.epfl.imhof.osm.OSMRelation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;

public class OSMRelationTest {

    private static List<Member> members = Arrays.asList(
            new Member(Type.NODE, "inner", new OSMNode(123456, new PointGeo(0, 0), new Attributes(Collections.emptyMap()))),
            new Member(Type.NODE, "inner", new OSMNode(123457, new PointGeo(0, 0), new Attributes(Collections.emptyMap()))),
            new Member(Type.NODE, "inner", new OSMNode(123458, new PointGeo(0, 0), new Attributes(Collections.emptyMap())))
    );
    
    @Test (expected= UnsupportedOperationException.class)
    public void memberListIsUnmodifiable() {
        OSMRelation r = new OSMRelation(123, members, new Attributes(Collections.emptyMap()));
        r.members().add(new Member(Type.NODE, "inner", new OSMNode(123459, new PointGeo(0, 0), new Attributes(Collections.emptyMap()))));
    }
    
    @Test
    public void returnsMembersCorrectly() {
        OSMRelation r = new OSMRelation(123, members, new Attributes(Collections.emptyMap()));
        int i = 0;
        for (Member m : r.members()) {
            assertEquals(members.get(i).type(), m.type());
            assertEquals(members.get(i).role(), m.role());
            assertEquals(members.get(i).member().getClass(), m.member().getClass());
            assertEquals(members.get(i).member().id(), m.member().id());
            ++i;
        }        
    }
    
    @Test (expected = IllegalStateException.class)
    public void cannotBuildIncomplete() {
        Builder b = new Builder(123);
        b.setIncomplete();
        b.build();
    }
    
    @Test
    public void buildsCorrectly() {
        Builder b = new Builder(123);
        b.addMember(Type.NODE, "inner", new OSMNode(123456, new PointGeo(0, 0), new Attributes(Collections.emptyMap())));
        b.addMember(Type.NODE, "inner", new OSMNode(123457, new PointGeo(0, 0), new Attributes(Collections.emptyMap())));
        b.addMember(Type.NODE, "inner", new OSMNode(123458, new PointGeo(0, 0), new Attributes(Collections.emptyMap())));
        OSMRelation r = b.build();
        assertEquals(OSMRelation.class, r.getClass());
        int i = 0;
        for (Member m : r.members()) {
            assertEquals(members.get(i).type(), m.type());
            assertEquals(members.get(i).role(), m.role());
            assertEquals(members.get(i).member().getClass(), m.member().getClass());
            assertEquals(members.get(i).member().id(), m.member().id());
            ++i;
        }  
    }
    
}
