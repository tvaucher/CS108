package ch.epfl.imhof.osm;

import static org.junit.Assert.*;
import java.lang.reflect.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member;

public class OSMMapTest {
    
    private static boolean  DEEP_COPY_WAYS = false;
    private static boolean IM_COLL_WAYS = false;
    
    private static boolean DEEP_COPY_RELATIONS = false;
    private static boolean IM_COL_RELATIONS = false;
    
    private HashMap<String, String> sampleAttributesValues() {
        HashMap<String, String> testData1 = new HashMap<>();
        testData1.put("testKey 1", "testValue 1");
        testData1.put("testKey 2", "testValue 2");
        testData1.put("testKey 3", "testValue 3");
        return testData1;
    }
    private List<OSMWay> createOSMWayList() {
        //Create Way 1
        HashMap<String, String> testData1 = sampleAttributesValues();
        Attributes testAttributes1 = new Attributes( testData1 );
        PointGeo testPointGeo1 = new PointGeo(0.125621, 0.803253);
        OSMNode testNode1 = new OSMNode(1, testPointGeo1, testAttributes1);
        
        HashMap<String, String> testData2 = sampleAttributesValues();
        Attributes testAttributes2 = new Attributes( testData2 );
        PointGeo testPointGeo2 = new PointGeo(0.110567, 0.801039);
        OSMNode testNode2 = new OSMNode(2, testPointGeo2, testAttributes2);
        
        HashMap<String, String> testData3 = sampleAttributesValues();
        Attributes testAttributes3 = new Attributes( testData3 );
        PointGeo testPointGeo3 = new PointGeo(0.130156, 0.806719);
        OSMNode testNode3 = new OSMNode(3, testPointGeo3, testAttributes3);
        
        List<OSMWay> testWayList = new ArrayList<>();
        
        List<OSMNode> testWay1Nodes = new ArrayList<>();
        testWay1Nodes.add(testNode1);
        testWay1Nodes.add(testNode2);
        
        HashMap<String, String> testData4 = sampleAttributesValues();
        Attributes testAttributes4 = new Attributes( testData4 );
        OSMWay testWay1 = new OSMWay(4, testWay1Nodes, testAttributes4);
        
        testWayList.add(testWay1);
        
        //Create way 2
        List<OSMNode> testWay2Nodes = new ArrayList<>();
        testWay2Nodes.add(testNode2);
        testWay2Nodes.add(testNode3);
        
        HashMap<String, String> testData5 = sampleAttributesValues();
        Attributes testAttributes5 = new Attributes( testData5 );
        OSMWay testWay2 = new OSMWay(5, testWay2Nodes, testAttributes5);
        
        testWayList.add(testWay2);
        
        return testWayList;
    }
    
    private OSMNode createOSMNode() {
        HashMap<String, String> testData3 = sampleAttributesValues();
        Attributes testAttributes3 = new Attributes( testData3 );
        PointGeo testPointGeo3 = new PointGeo(0.130156, 0.806719);
        OSMNode testNode3 = new OSMNode(3, testPointGeo3, testAttributes3);
        
        return testNode3;
    }
    
    private OSMWay createOSMWay() {
        HashMap<String, String> testData = sampleAttributesValues();
        Attributes testAttributes = new Attributes( testData );
        List<OSMNode> testWayNodes = new ArrayList<>();
        testWayNodes.add(createOSMNode());
        testWayNodes.add(createOSMNode());
        
        OSMWay testWay = new OSMWay(6, testWayNodes, testAttributes);
        
        return testWay;
    }
    
    @Test
    public void waysListNonModifiable() throws Exception{
        
        List<OSMWay> testWay = createOSMWayList();
        OSMWay wayToAdd = createOSMWay();
        OSMMap testMap = new OSMMap(testWay, new ArrayList<OSMRelation>());
        
        /*
         * There are two ways to achieve immutability of this classs:
         * 1. Use unmodifiable collections
         * 2. Make deepCopy in constructor and in getters
         * */
        
        // use reflection to get private field out
        Field[] fields = OSMMap.class.getDeclaredFields();
        Field field = null;
        
        //Take out the right wayList variable
        int count = 0;
        for (int i=0; i < fields.length; i++) {
            Type t = fields[i].getGenericType();
            
            if (t.toString().equals("java.util.List<ch.epfl.imhof.osm.OSMWay>")) {
                field = fields[i];
                count++;
            }
        }
        assertEquals(1, count);
        assertTrue(field != null);
        
        field.setAccessible(true);
        List<OSMWay> waysList =  (List<OSMWay>)field.get(testMap);
        
        
        //1.Test to see if they use a immutable list
        try {
            waysList.add(wayToAdd);
            
        } catch (UnsupportedOperationException e) {
            IM_COLL_WAYS = true;
            
        }
        
        if (!IM_COLL_WAYS) {
            //2.Test to see if they correctly use the deep copy
            boolean constructorDeepCopy = false;
            boolean getterDeepCopy = false;
            
            OSMMap testMap2 = new OSMMap(testWay, new ArrayList<OSMRelation>());
            
            //Check deep copy was done in constructor
            testWay.add(wayToAdd);
            constructorDeepCopy = testMap2.ways().size() != testWay.size();
            
            //Check deep copy is done in the gettter. We can assume deep copy is done in the constructor. If not test will fail
            OSMMap testMap3 = new OSMMap(testWay, new ArrayList<OSMRelation>());
            getterDeepCopy = testMap3.ways() != testMap3.ways();
            
            DEEP_COPY_WAYS = constructorDeepCopy && getterDeepCopy;
        }
        assertTrue(IM_COLL_WAYS || DEEP_COPY_WAYS);
    }
    
    
    @Test
    public void relationsListNonModifiable() throws Exception{
        
        HashMap<String, String> testData1 = sampleAttributesValues();
        Attributes testAttributes1 = new Attributes( testData1 );
        PointGeo testPointGeo1 = new PointGeo(0.125621, 0.803253);
        OSMNode testNode1 = new OSMNode(1, testPointGeo1, testAttributes1);
        
        HashMap<String, String> testData2 = sampleAttributesValues();
        Attributes testAttributes2 = new Attributes( testData2 );
        PointGeo testPointGeo2 = new PointGeo(0.110567, 0.801039);
        OSMNode testNode2 = new OSMNode(2, testPointGeo2, testAttributes2);
        
        HashMap<String, String> testData3 = sampleAttributesValues();
        Attributes testAttributes3 = new Attributes( testData3 );
        PointGeo testPointGeo3 = new PointGeo(0.130156, 0.806719);
        OSMNode testNode3 = new OSMNode(3, testPointGeo3, testAttributes3);
        
        List<OSMWay> testWayList = new ArrayList<>();
        List<OSMNode> testWay1Nodes = new ArrayList<>();
        testWay1Nodes.add(testNode1);
        testWay1Nodes.add(testNode2);
        
        HashMap<String, String> testData4 = sampleAttributesValues();
        Attributes testAttributes4 = new Attributes( testData4 );
        OSMWay testWay1 = new OSMWay(4, testWay1Nodes, testAttributes4);
        testWayList.add(testWay1);
        List<OSMNode> testWay2Nodes = new ArrayList<>();
        testWay2Nodes.add(testNode2);
        testWay2Nodes.add(testNode3);
        
        HashMap<String, String> testData5 = sampleAttributesValues();
        Attributes testAttributes5 = new Attributes( testData5 );
        OSMWay testWay2 = new OSMWay(5, testWay2Nodes, testAttributes5);
        testWayList.add(testWay2);
        List<OSMRelation> testRelationList = new ArrayList<>();
        List<OSMRelation.Member> testRelation1Members = new ArrayList<>();
        testRelation1Members.add(new OSMRelation.Member(OSMRelation.Member.Type.NODE, "First", testNode1));
        testRelation1Members.add(new OSMRelation.Member(OSMRelation.Member.Type.NODE, "Second", testNode2));
        testRelation1Members.add(new OSMRelation.Member(OSMRelation.Member.Type.WAY, "Total", testWay1));
        
        HashMap<String, String> testData6 = sampleAttributesValues();
        Attributes testAttributes6 = new Attributes( testData6 );
        OSMRelation testRelation1 = new OSMRelation(6, testRelation1Members, testAttributes6);
        testRelationList.add(testRelation1);
        
        List<OSMRelation.Member> testRelation2Members = new ArrayList<>();
        testRelation2Members.add(new OSMRelation.Member(OSMRelation.Member.Type.NODE, "First", testNode2));
        testRelation2Members.add(new OSMRelation.Member(OSMRelation.Member.Type.NODE, "Second", testNode3));
        testRelation2Members.add(new OSMRelation.Member(OSMRelation.Member.Type.WAY, "Total", testWay2));
        
        HashMap<String, String> testData7 = sampleAttributesValues();
        Attributes testAttributes7 = new Attributes( testData7 );
        OSMRelation testRelation2 = new OSMRelation(7, testRelation2Members, testAttributes7);
        testRelationList.add(testRelation2);
        
        OSMMap testMap = new OSMMap(testWayList, testRelationList);
        OSMRelation relationToAdd = new OSMRelation(8, testRelation2Members, testAttributes7);;
        
        /*
         * There are two ways to achieve immutability of this classs:
         * 1. Use unmodifiable collections
         * 2. Make deepCopy in constructor and in getters
         * */
        
        
        
        // use reflection to get private field out
        Field[] fields = OSMMap.class.getDeclaredFields();
        Field field = null;
        
        //Take out the right relationList variable
        int count = 0;
        for (int i=0; i < fields.length; i++) {
            Type t = fields[i].getGenericType();
            
            if (t.toString().equals("java.util.List<ch.epfl.imhof.osm.OSMRelation>")) {
                field = fields[i];
                count++;
            }
        }
        
        assertEquals(1, count);
        assertTrue(field != null);
        
        field.setAccessible(true);
        List<OSMRelation> relationsList =  (List<OSMRelation>)field.get(testMap);
        
        //1.Test to see if they use a immutable list
        try {
            relationsList.add(relationToAdd);
            
        } catch (UnsupportedOperationException e) {
            IM_COL_RELATIONS = true;
        }
        
        if (!IM_COL_RELATIONS) {
            //2.Test to see if they correctly use the deep copy
            boolean constructorDeepCopy = false;
            boolean getterDeepCopy = false;
            
            OSMMap testMap2 = new OSMMap(new ArrayList<>(), testRelationList);
            
            //Check deep copy was done in constructor
            testRelationList.add(relationToAdd);
            constructorDeepCopy = testMap2.relations().size() != testRelationList.size();
            
            //Check deep copy is done in the getter. We can assume deep copy is done in the constructor. If not test will fail
            OSMMap testMap3 = new OSMMap(new ArrayList<>(), testRelationList);
            getterDeepCopy = testMap3.relations() != testMap3.relations();
            
            DEEP_COPY_RELATIONS = constructorDeepCopy && getterDeepCopy;
        }
        assertTrue(IM_COL_RELATIONS || DEEP_COPY_RELATIONS);
    }
    
    @Test
    public void testGetterWays() {
        OSMMap map = new OSMMap(new ArrayList<>(),new ArrayList<>());
        
        //GETTER IS CORRECT IF SOLUTION OF IMUTABILITY IS PERSISTENT
        if (DEEP_COPY_WAYS) {
            //deep copy in getter should be done when not using immutable collections
            assertFalse(map.ways() == map.ways());
        } else {
            if (IM_COLL_WAYS) {
                //deep copy in getter should not be done when using immutable collections
                assertTrue(map.ways() == map.ways());
            } else {
                // getter not correct
                fail();
            }
        }
    }
    
    @Test
    public void testGetterRelations() {
        OSMMap map = new OSMMap(new ArrayList<>(),new ArrayList<>());
        
        //GETTER IS CORRECT IF SOLUTION OF IMUTABILITY IS PERSISTENT
        if (DEEP_COPY_RELATIONS) {
            //deep copy in getter should be done when not using immutable collections
            assertFalse(map.relations() == map.relations());
        } else {
            if (IM_COL_RELATIONS) {
                
                //deep copy in getter should not be done when using immutable collections
                assertTrue(map.relations() == map.relations());
            } else {
                // getter not correct
                fail("Getter implementation not correct");
            }
        }
    }
    
    @Test
    public void testEqualsNotRedefined() {
        OSMMap map1 = new OSMMap(new ArrayList<>(),new ArrayList<>());
        OSMMap map2 = new OSMMap(new ArrayList<>(),new ArrayList<>());
        assertTrue(!map1.equals(map2));
    }
    
    @Test
    public void testHashCodeNotRedefined() {
        OSMMap map1 = new OSMMap(new ArrayList<>(),new ArrayList<>());
        OSMMap map2 = new OSMMap(new ArrayList<>(),new ArrayList<>());
        assertTrue(map1.hashCode() != map2.hashCode());
        
    }
    
    /*******************Builder Tests ********************************/
    @Test
    public void testAddNode() {
        OSMMap.Builder builder = new OSMMap.Builder();
        OSMNode node = createOSMNode();
        
        try {
            builder.addNode(node);
        } catch (Exception e){
            fail("Exception thrown when node added");
        }
        
    }
    
    public void testAddNodeNull() {
        OSMMap.Builder builder = new OSMMap.Builder();
        
        try {
            builder.addNode(null);
            fail("Exception should have been thrown for null argument");
        } catch (NullPointerException e){
            
        }
    }
    
    @Test
    public void testNodeForId() {
        OSMMap.Builder builder = new OSMMap.Builder();
        OSMNode node = createOSMNode();
        long id = 3;
        
        builder.addNode(node);
        
        try {
            OSMNode resultNode= builder.nodeForId(id);
            assertEquals(node, resultNode);
            
        } catch (Exception e){
            fail("Exception thrown when looking for valid id");
        }
        
    }
    
    @Test
    public void testNodeForInvalidId() {
        OSMMap.Builder builder = new OSMMap.Builder();
        OSMNode node = createOSMNode();
        
        builder.addNode(node);
        
        try {
            assertEquals(null, builder.nodeForId(9999));
            assertEquals(null, builder.nodeForId(-22));
            
        } catch (Exception e){
            fail("Exception dont have to be thrown instead of returning null value");
        }
        
    }
    
    @Test
    public void testAddWay() {
        OSMMap.Builder builder = new OSMMap.Builder();
        OSMWay way = createOSMWayList().get(0);
        
        try {
            builder.addWay(way);
        } catch (Exception e){
            fail("Exception thrown when way added");
        }
        
    }
    
    @Test
    public void testAddWayNull() {
        OSMMap.Builder builder = new OSMMap.Builder();
        
        try {
            builder.addWay(null);
            fail("Exception should have been thrown when null argument");
        } catch (NullPointerException e){
            
        }
    }
    
    @Test
    public void testwayForId() {
        OSMMap.Builder builder = new OSMMap.Builder();
        OSMWay way = createOSMWayList().get(0);
        builder.addWay(way);
        
        try {
            OSMWay resultWay= builder.wayForId(4);
            assertEquals(way, resultWay);
            
        } catch (Exception e){
            fail("Exception thrown when looking for valid id");
        }
    }
    
    @Test
    public void testwayForIdInvalid() {
        OSMMap.Builder builder = new OSMMap.Builder();
        
        try {
            assertEquals(null, builder.wayForId(9999));
            assertEquals(null, builder.wayForId(-22));
            
        } catch (Exception e){
            fail("Exception should not have been thrown instead returning null");
        }
    }
    
    @Test
    public void testAddRelation() {
        OSMMap.Builder builder = new OSMMap.Builder();
        long id = 5;
        OSMRelation relation = new OSMRelation(id, new ArrayList<Member>(), new Attributes(sampleAttributesValues()));
        
        try {
            builder.addRelation(relation);
        } catch (Exception e) {
            fail("Exception thrown when relation added");
        }
    }
    
    @Test
    public void testAddRelationNull() {
        OSMMap.Builder builder = new OSMMap.Builder();
        
        try {
            builder.addRelation(null);
            fail("Exception should be thrown when null relation added");
        } catch (NullPointerException e) {
            
        }
    }
    
    @Test
    public void testRelationForId() {
        OSMMap.Builder builder = new OSMMap.Builder();
        
        long id = 5;
        OSMRelation relation = new OSMRelation(id, new ArrayList<Member>(), new Attributes(sampleAttributesValues()));
        builder.addRelation(relation);
        
        try{
            assertEquals(relation, builder.relationForId(id));
            
        } catch (Exception e) {
            fail("Exception should not be thrown here");
        }
    }
    
    
    @Test
    public void testRelationForIdInvalid() {
        OSMMap.Builder builder = new OSMMap.Builder();
        
        try{
            assertEquals(null, builder.wayForId(9999));
            assertEquals(null, builder.wayForId(-22));
            
        } catch (Exception e) {
            fail("Exception should not be thrown here");
        }
    }
    
    @Test
    public void testBuild() {
        OSMMap.Builder builder = new OSMMap.Builder();
        
        long idRelation = 5;
        OSMRelation relation = new OSMRelation(idRelation, new ArrayList<Member>(), new Attributes(sampleAttributesValues()));
        builder.addRelation(relation);
        
        long idWay = 4;
        OSMWay way = createOSMWayList().get(0);
        builder.addWay(way);
        
        OSMMap map = null;
        try {
            map = builder.build();
            
        } catch (Exception e) {
            fail("Exception thrown when building");
        }
        
        assertTrue(map != null);
        assertEquals(idWay, map.ways().get(0).id());
        assertEquals(idRelation, map.relations().get(0).id());
    }
    
    @Test 
    public void testBuilderEqualsNotRedefined() {
        OSMMap.Builder builder1 = new OSMMap.Builder();
        OSMMap.Builder builder2 = new OSMMap.Builder();
        assertTrue(!builder1.equals(builder2));
    }
    
    @Test
    public void testBuilerHashCodeNotRedefined() {
        OSMMap.Builder builder1 = new OSMMap.Builder();
        OSMMap.Builder builder2 = new OSMMap.Builder();
        assertTrue(builder1.hashCode() != builder2.hashCode());
        
    }
    
}
