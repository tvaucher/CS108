package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMEntity.Builder;
import ch.epfl.imhof.testUtilities.ListNonMutableTestUtility;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class OSMWayTest extends OSMEntityTest{

    private static final OSMNode TEST_NODE_1 = new OSMNode(12, new PointGeo(0.125621, 0.803253), EMPTY_ATTRIBUTES);
    private static final OSMNode TEST_NODE_2 = new OSMNode(34, new PointGeo(0.136710, 0.814253), EMPTY_ATTRIBUTES);
    private static final OSMNode TEST_NODE_3 = new OSMNode(56, new PointGeo(0.107312, 0.805619), EMPTY_ATTRIBUTES);

    @Override
    OSMEntity newEntity(long id, Attributes entityAttributes) {
        ArrayList<OSMNode> testWayList = new ArrayList<>();
        testWayList.add(TEST_NODE_1);
        testWayList.add(TEST_NODE_2);
        testWayList.add(TEST_NODE_3);
        return new OSMWay(id, testWayList, entityAttributes);
    }

    @Override
    Builder newEntityBuilder() {
        return new OSMWay.Builder(1);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void constructorLessThanTwoNodes() {
        ArrayList<OSMNode> testWayList = new ArrayList<>();
        testWayList.add(TEST_NODE_1);
        new OSMWay(4, testWayList, EMPTY_ATTRIBUTES);
    }
    
    @Test
    public void constructorNodeListNonMutable() {
        ArrayList<OSMNode> testWayList = new ArrayList<>();
        testWayList.add(TEST_NODE_1);
        testWayList.add(TEST_NODE_2);
        testWayList.add(TEST_NODE_3);
        OSMWay testWay = new OSMWay(4, testWayList, EMPTY_ATTRIBUTES);
        assertTrue(ListNonMutableTestUtility.nonMutableFieldListTest(testWayList, testWay.nodes()));
    }

    @Test
    public void notClosed() {
        ArrayList<OSMNode> testWayList = new ArrayList<>();
        testWayList.add(TEST_NODE_1);
        testWayList.add(TEST_NODE_2);
        testWayList.add(TEST_NODE_3);
        OSMWay testWay = new OSMWay(4, testWayList, EMPTY_ATTRIBUTES);
        assertFalse(testWay.isClosed());
        assertSame(testWay.firstNode(), TEST_NODE_1);
        assertSame(testWay.lastNode(), TEST_NODE_3);
        assertEquals(testWay.nodesCount(), 3);
        assertEquals(testWay.nodes(), testWayList);
        assertEquals(testWay.nonRepeatingNodes(), testWayList);
    }

    @Test
    public void closed() {
        ArrayList<OSMNode> testWayList = new ArrayList<>();
        testWayList.add(TEST_NODE_1);
        testWayList.add(TEST_NODE_2);
        testWayList.add(TEST_NODE_1);
        OSMWay testWay = new OSMWay(4, testWayList, EMPTY_ATTRIBUTES);
        assertTrue(testWay.isClosed());
        assertSame(testWay.firstNode(), TEST_NODE_1);
        assertSame(testWay.lastNode(), TEST_NODE_1);
        assertEquals(testWay.nodesCount(), 3);
        assertEquals(testWay.nodes(), testWayList);
        assertEquals(testWay.nonRepeatingNodes(), testWayList.subList(0, testWayList.size() - 1));
    }

    @Test
    @Override
    public void builderBuiltSetAttribute() throws Throwable {
        OSMWay.Builder testBuild = new OSMWay.Builder(1);
        testBuild.addNode(TEST_NODE_1);
        testBuild.addNode(TEST_NODE_2);
        testBuild.setAttribute("testKey 1", "testValue 1");
        // Test if setAttribute overwrites old value
        testBuild.setAttribute("testKey 1", "testValue 3");
        testBuild.setAttribute("testKey 2", "testValue 2");
        OSMEntity entity = testBuild.build();
        assertTrue(entity.hasAttribute("testKey 1") && entity.attributeValue("testKey 1").equals( "testValue 3")
                && entity.hasAttribute("testKey 2") && entity.attributeValue("testKey 2").equals("testValue 2"));
    }

    @Test
    @Override
    public void builderIsIncomplete() {
        OSMWay.Builder builder = new OSMWay.Builder(1);
        assertTrue(builder.isIncomplete());
        builder.addNode(TEST_NODE_1);
        builder.addNode(TEST_NODE_2);
        assertFalse(builder.isIncomplete());
        builder.setIncomplete();
        assertTrue(builder.isIncomplete());
    }

    @Test(expected = IllegalStateException.class)
    public void builderWitNoNode() {
            OSMWay.Builder wayBuilder = new OSMWay.Builder(14);
            wayBuilder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void builderWithLessThanTwoNode() {
            OSMWay.Builder wayBuilder = new OSMWay.Builder(14);
            wayBuilder.addNode(TEST_NODE_1);
            wayBuilder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void buildWithMoreThanTwoNodesAndIncomplete() {
            OSMWay.Builder wayBuilder = new OSMWay.Builder(14);
            wayBuilder.addNode(TEST_NODE_1);
            wayBuilder.addNode(TEST_NODE_2);
            wayBuilder.addNode(TEST_NODE_3);
            wayBuilder.setIncomplete();
            wayBuilder.build();
    }

}
