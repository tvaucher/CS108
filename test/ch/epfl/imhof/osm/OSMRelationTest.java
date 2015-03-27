package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMEntity.Builder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class OSMRelationTest extends OSMEntityTest{

    private static final OSMNode TEST_NODE_1 = new OSMNode(78, new PointGeo(0.125621, 0.803253), EMPTY_ATTRIBUTES);
    private static final OSMNode TEST_NODE_2 = new OSMNode(91, new PointGeo(0.136710, 0.814253), EMPTY_ATTRIBUTES);

    @Override
    OSMEntity newEntity(long id, Attributes entityAttributes) {
        OSMRelation.Member testMember1  = new OSMRelation.Member(OSMRelation.Member.Type.NODE,"testPoint1", TEST_NODE_1);

        List<OSMRelation.Member> testMemberList = new ArrayList<>();
        testMemberList.add(testMember1);
        return new OSMRelation(id, testMemberList, entityAttributes);
    }

    @Override
    Builder newEntityBuilder() {
        return new OSMRelation.Builder(1);
    }

    @Test
    public void constructorWithNodeWayRelation() {
        OSMRelation.Member testMember1 = new OSMRelation.Member(OSMRelation.Member.Type.NODE, "testPoint1", TEST_NODE_1);
        OSMRelation.Member testMember2 = new OSMRelation.Member(OSMRelation.Member.Type.NODE, "testPoint2", TEST_NODE_2);

        List<OSMNode> nodes = Arrays.asList(TEST_NODE_1, TEST_NODE_2);
        OSMWay way = new OSMWay(45, nodes, EMPTY_ATTRIBUTES);
        OSMRelation.Member testMember3 = new OSMRelation.Member(OSMRelation.Member.Type.WAY, "testWay", way);

        OSMRelation relation = new OSMRelation(67, Collections.emptyList(), EMPTY_ATTRIBUTES);
        OSMRelation.Member testMember4 = new OSMRelation.Member(OSMRelation.Member.Type.RELATION, "testRelation", relation);

        List<OSMRelation.Member> testMemberList = new ArrayList<>();
        testMemberList.add(testMember1);
        testMemberList.add(testMember2);
        testMemberList.add(testMember3);
        testMemberList.add(testMember4);

        OSMRelation testEndRelation = new OSMRelation(5, testMemberList, EMPTY_ATTRIBUTES);
        assertTrue(ch.epfl.imhof.testUtilities.ListNonMutableTestUtility.nonMutableFieldListTest(testMemberList, testEndRelation.members()));
    }

    @Test
    public void builderBuiltStepByStep() {
        List<OSMNode> testWayPointsList = new ArrayList<>();
        testWayPointsList.add(TEST_NODE_1);
        testWayPointsList.add(TEST_NODE_2);
        OSMWay testWay = new OSMWay(4, testWayPointsList, EMPTY_ATTRIBUTES);
        OSMRelation testRelation = new OSMRelation(5, Collections.emptyList(), EMPTY_ATTRIBUTES);

        OSMRelation.Builder testBuild = new OSMRelation.Builder(1);
        testBuild.addMember(OSMRelation.Member.Type.NODE, "testPoint1", TEST_NODE_1);
        testBuild.addMember(OSMRelation.Member.Type.NODE, "testPoint2", TEST_NODE_2);
        testBuild.addMember(OSMRelation.Member.Type.WAY, "testWay", testWay);
        testBuild.addMember(OSMRelation.Member.Type.RELATION, "testRelation", testRelation);
        testBuild.build();
    }

    @Test
    public void memberConstructorAndGetType() {
        OSMRelation.Member testMember = new OSMRelation.Member(OSMRelation.Member.Type.NODE, "test", TEST_NODE_1);
        assertSame(testMember.type(), OSMRelation.Member.Type.NODE);
    }

    @Test
    public void memberConstructorAndGetRole() {
        OSMRelation.Member testMember = new OSMRelation.Member(OSMRelation.Member.Type.NODE, "test", TEST_NODE_1);
        assertEquals(testMember.role(), "test");
    }

    @Test
    public void memberConstructorAndGetMemberReferenceCheck() {
        OSMRelation.Member testMember = new OSMRelation.Member(OSMRelation.Member.Type.NODE, "test", TEST_NODE_1);
        assertSame(testMember.member(), TEST_NODE_1);
    }

}
