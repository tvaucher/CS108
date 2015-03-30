package ch.epfl.imhof.osm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Test;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.projection.CH1903Projection;

public class OSMToGeoTransformerTest {

    private final OSMToGeoTransformer testTransformer = new OSMToGeoTransformer(new CH1903Projection());

    private HashMap<Integer, Integer> polygonHoleCounter(Map testMap){
        HashMap<Integer, Integer> resultMap = new HashMap<>();
        for (Attributed<Polygon> k : testMap.polygons()){
            int holesNumber = k.value().holes().size();
            if (resultMap.containsKey(holesNumber)){
                resultMap.put(holesNumber, (resultMap.get(holesNumber) + 1));
            } else {
                resultMap.put(holesNumber, 1);
            }
        }
        return resultMap;
    }

    @Test
    public void simpleBuilding() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testSimpleBuilding.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(1, testMap.polygons().size());
        Attributed<Polygon> polygon = testMap.polygons().get(0);
        assertTrue(polygon.hasAttribute("building"));
        assertEquals("yes", polygon.attributeValue("building"));
    }

    @Test
    public void closedWayWithoutAttributeIsFiltered() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testWaysNoAttribute.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(0, testMap.polygons().size());
    }

    @Test
    public void closedWayWithAreaAttribute() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testWaysWithAreaAttribute.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(1, testMap.polyLines().size());
        assertEquals(3, testMap.polygons().size());
        for (Attributed<Polygon> polygon : testMap.polygons()) {
            assertTrue(polygon.hasAttribute("layer"));
            assertEquals("1", polygon.attributeValue("layer"));
        }
    }

    @Test
    public void closedWayWithAttributes() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testWaysWithAttributes.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(1, testMap.polyLines().size());
        assertEquals(20, testMap.polygons().size());
        for (Attributed<Polygon> polygon : testMap.polygons()) {
            assertFalse(polygon.attributes().isEmpty());
        }
    }




    @Test
    public void severalWallsTwoHoles() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testSeveralWalls.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(1, testMap.polygons().size());
        // Attributes Error
        Attributed<Polygon> polygon = testMap.polygons().get(0);
        assertTrue(polygon.hasAttribute("building"));
        assertEquals(polygon.attributeValue("building"), "yes");
        assertEquals(polygon.value().holes().size(), 2);
        assertEquals(polygon.value().shell().points().size(), 5);
        ClosedPolyLine firstHole = polygon.value().holes().get(0);
        ClosedPolyLine secondHole = polygon.value().holes().get(1);
        assertTrue((firstHole.points().size() == 10 && secondHole.points().size() == 4)
                || (firstHole.points().size() == 4 && secondHole.points().size() == 10));
    }

    @Test
    public void roadWhole() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testRoadWhole.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(testMap.polyLines().size(), 1);
        assertEquals(testMap.polygons().size(), 0);
        Attributed<PolyLine> polyLine = testMap.polyLines().get(0);
        assertTrue(polyLine.attributes().contains("highway"));
        assertEquals("road", polyLine.attributes().get("highway"));
        assertEquals(8, polyLine.value().points().size());
        assertFalse(polyLine.value().isClosed());
    }

    /*
     * creates two buildings from two relations using each two ways
     */
    @Test
    public void joinedBuildingsFromRelation() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testJoinedBuildingsFromRelation.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines() .size());
        assertEquals(2, testMap.polygons().size());
        //Tag building is assigned not properly
        for (Attributed<Polygon> polygon : testMap.polygons()) {
            assertTrue(polygon.hasAttribute("building"));
            assertEquals("yes", polygon.attributeValue("building"));
            assertEquals(4, polygon.value().shell().points().size());
        }
    }

    /*
     * creates two building from two closed ways containing the building key
     */
    @Test
    public void joinedBuildingsFromClosedWay() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testJoinedBuildingsFromClosedWay.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(2, testMap.polygons().size());
        //Tag building is assigned not properly
        for (Attributed<Polygon> polygon : testMap.polygons()) {
            assertTrue(polygon.hasAttribute("building"));
            assertEquals("yes", polygon.attributeValue("building"));
            assertEquals(4, polygon.value().shell().points().size());
        }
    }

    @Test
    public void buildingOnALandUse() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testBuildingOnALandUse.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(2, testMap.polygons().size());

        boolean buildingFound = false;
        boolean landUseFound = false;
        for (Attributed<Polygon> polygon : testMap.polygons()) {
            if (polygon.hasAttribute("building")) {
                assertEquals("yes", polygon.attributeValue("building"));
                buildingFound = true;
            } else if (polygon.hasAttribute("landuse")) {
                assertEquals("residential", polygon.attributeValue("landuse"));
                landUseFound = true;
            }
        }
        assertTrue(buildingFound && landUseFound);
    }

    @Test
    public void buildingOneInnerHole() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testBuildingOneInnerHole.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(2, testMap.polygons().size());

        boolean holeFound = false;

        for (Attributed<Polygon> polygon : testMap.polygons()) {
            assertTrue(polygon.hasAttribute("building"));
            assertEquals("yes", polygon.attributeValue("building"));
            assertEquals(4, polygon.value().shell().points().size());
            if (!polygon.value().holes().isEmpty()) {
                assertEquals(1, polygon.value().holes().size());
                assertEquals(4, polygon.value().holes().get(0).points().size());
                holeFound = true;
            }
        }

        assertTrue(holeFound);
    }

    @Test
    public void buildingMultipleHoles() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testBuildingMultipleHoles.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(1, testMap.polygons().size());

        Attributed<Polygon> polygon = testMap.polygons().get(0);
        assertTrue(polygon.hasAttribute("building"));
        assertEquals("yes", polygon.attributeValue("building"));
        assertEquals(3, polygon.value().holes().size());
        assertEquals(4, polygon.value().shell().points().size());
    }

    @Test
    public void notMultipolygon() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testNotMultipolygon.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(0, testMap.polygons().size());
    }

    @Test
    public void orphanInnerRing() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testOrphanInnerRing.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);

        assertEquals(0, testMap.polyLines().size());
        assertEquals(1, testMap.polygons().size());

        Attributed<Polygon> polygon = testMap.polygons().get(0);
        assertTrue(polygon.hasAttribute("building"));
        assertEquals("yes", polygon.attributeValue("building"));
        assertEquals(0, polygon.value().holes().size());
        assertEquals(6, polygon.value().shell().points().size());
    }

    @Test
    public void tooManyNeighbors() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testTooManyNeighbors.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polygons().size());
        assertEquals(0, testMap.polyLines().size());
    }

    @Test
    public void roadCorrupted() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testRoadCorrupted.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(0, testMap.polygons().size());
    }

    @Test
    public void joinedBuildingsTaggedRelation() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testJoinedBuildingsTaggedRelation.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(2, testMap.polygons().size());

        for (Attributed<Polygon> polygon : testMap.polygons()) {
            assertTrue(polygon.hasAttribute("building"));
            assertEquals("yes", polygon.attributeValue("building"));
            assertEquals(4, polygon.value().shell().points().size());
        }
    }

    @Test
    public void BuildingInsideBuilding() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testBuildingInsideBuilding.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            System.out.println(e);
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(2, testMap.polygons().size());

        for (Attributed<Polygon> polygon : testMap.polygons()){
            assertTrue(polygon.attributes().contains("building"));
            assertEquals("yes", polygon.attributes().get("building"));

            assertEquals(1, polygon.value().holes().size());
            assertEquals(4, polygon.value().shell().points().size());
            assertEquals(4, polygon.value().holes().get(0).points().size());
        }

        Polygon firstPolygon = testMap.polygons().get(0).value();
        Polygon secondPolygon = testMap.polygons().get(1).value();
        assertTrue(secondPolygon.holes().get(0).containsPoint(firstPolygon.shell().firstPoint()) ||
                firstPolygon.holes().get(0).containsPoint(secondPolygon.shell().firstPoint()));
    }

    @Test
    public void MultipleBuildingsInOneRelation() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testMultipleBuildingsInOneRelation.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        HashMap<Integer,Integer> holeCount = polygonHoleCounter(testMap);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(4, testMap.polygons().size());
        HashMap<Integer, Integer> mapRef = new HashMap<>();
        mapRef.put(0, 1);
        mapRef.put(1, 2);
        mapRef.put(2, 1);
        assertEquals(mapRef, holeCount);
    }

    /*
     * test if correct set of polygon keys are filtered
     */
    @Test
    public void PolygonRelationsToKeep() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testPolygonRelationsToKeep.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(0, testMap.polyLines().size());
        assertEquals(6, testMap.polygons().size());
    }

    /*
     * test if correct set of polyline keys are filtered
     */
    @Test
    public void PolylineKeysToKeep() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testPolylineKeysToKeep.osm").getFile().replace("%20",  " "), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        Map testMap = testTransformer.transform(testMapReadResult);
        assertEquals(7, testMap.polyLines().size());
        assertEquals(0, testMap.polygons().size());
    }

}
