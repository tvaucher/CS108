package ch.epfl.imhof.osm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.xml.sax.SAXParseException;

public class OSMMapReaderTest {

    @Test
    public void simpleBuilding() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testSimpleBuilding.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(1, testMapReadResult.ways().size());
        assertEquals(0, testMapReadResult.relations().size());
        assertTrue(testMapReadResult.ways().get(0).attributes().contains("building"));
        assertEquals("yes", testMapReadResult.ways().get(0).attributes().get("building"));
    }

    @Test
    public void severalWalls() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testSeveralWalls.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(13, testMapReadResult.ways().size());
        assertEquals(1, testMapReadResult.relations().size());
        assertTrue(testMapReadResult.relations().get(0).attributes().contains("type"));
        assertEquals("multipolygon", testMapReadResult.relations().get(0).attributes().get("type"));
    }

    @Test
    public void roadWhole() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testRoadWhole.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(1, testMapReadResult.ways().size());
        assertEquals(0, testMapReadResult.relations().size());
        assertTrue(testMapReadResult.ways().get(0).attributes().contains("highway"));
        assertEquals("road", testMapReadResult.ways().get(0).attributes().get("highway"));
    }

    @Test
    public void joinedBuildings() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testJoinedBuildings.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(3, testMapReadResult.ways().size());
        assertEquals(2, testMapReadResult.relations().size());
        for (int i = 0; i < 2; ++i){
            assertTrue(testMapReadResult.relations().get(i).attributes().contains("type"));
            assertEquals("multipolygon", testMapReadResult.relations().get(i).attributes().get("type"));
        }
    }

    @Test
    public void buildingOnALandUse() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testBuildingOnALandUse.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(2, testMapReadResult.ways().size());
        assertEquals(0, testMapReadResult.relations().size());
        boolean buildingFound = false;
        boolean landUseFound = false;
        for(int i = 0; i < 2; ++i){
            if (testMapReadResult.ways().get(i).attributes().contains("building")) {
                assertEquals("yes", testMapReadResult.ways().get(0).attributes().get("building"));
                buildingFound = true;
            }
            if (testMapReadResult.ways().get(i).attributes().contains("landuse")) {
                assertEquals("residential", testMapReadResult.ways().get(1).attributes().get("landuse"));
                landUseFound = true;
            }
        }
        assert(buildingFound && landUseFound);
    }

    @Test
    public void buildingOneInnerHole() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testBuildingOneInnerHole.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(2, testMapReadResult.ways().size());
        assertEquals(1, testMapReadResult.relations().size());
        assertTrue(testMapReadResult.relations().get(0).attributes().contains("type"));
        assertEquals("multipolygon", testMapReadResult.relations().get(0).attributes().get("type"));
    }

    @Test
    public void buildingMultipleHoles() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testBuildingMultipleHoles.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(4, testMapReadResult.ways().size());
        assertEquals(1, testMapReadResult.relations().size());
        assertTrue(testMapReadResult.relations().get(0).attributes().contains("type"));
        assertEquals("multipolygon", testMapReadResult.relations().get(0).attributes().get("type"));
    }

    @Test
    public void roads() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testRoads.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(5, testMapReadResult.ways().size());
        assertEquals(0, testMapReadResult.relations().size());
        for (int i = 0; i < 5; ++i){
            assertTrue(testMapReadResult.ways().get(i).attributes().contains("highway"));
            assertEquals("primary", testMapReadResult.ways().get(i).attributes().get("highway"));
            assertEquals(3, testMapReadResult.ways().get(i).nodes().size());
        }
    }

    @Test
    public void multipolygons() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testMultipolygons.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(12, testMapReadResult.ways().size());
        assertEquals(3, testMapReadResult.relations().size());
        for (int i = 0; i < 3; ++i){
            assertTrue(testMapReadResult.relations().get(i).attributes().contains("type"));
            assertEquals("multipolygon", testMapReadResult.relations().get(i).attributes().get("type"));
        }
    }

    @Test
    public void DataCorrupted() {
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testDataCorrupted.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(0, testMapReadResult.ways().size());
        assertEquals(0, testMapReadResult.relations().size());
    }

    @Test
    public void typeLessRelations() {
        OSMMap testMapReadResult = null;
        final String attr = "";
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testTypelessRelations.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly." + e);
        }
        assertEquals(12, testMapReadResult.ways().size());
        assertEquals(3, testMapReadResult.relations().size());
        for (int i = 0; i < 3; ++i){
            assertTrue(testMapReadResult.relations().get(i).attributes().contains(attr));
            assertEquals("multipolygon", testMapReadResult.relations().get(i).attributes().get(attr));
        }
    }

    @Test
    public void gzipIsRespected(){
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testGZip.osm.gz").getFile(), true);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(5, testMapReadResult.ways().size());
        assertEquals(0, testMapReadResult.relations().size());

    }

    @Test
    public void onlyDefinedAttsUsed(){
        OSMMap testMapReadResult = null;
        try {
            testMapReadResult = OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testUndefinedAtts.osm").getFile(), false);
        } catch (Exception e) {
            fail("Unable to recognise the file and/or read it properly.");
        }
        assertEquals(4, testMapReadResult.ways().size());
        assertEquals(1, testMapReadResult.relations().size());
        for(int i=0; i<4;i++){
            assertTrue(testMapReadResult.ways().get(i).attributes().isEmpty());
            for(int j=0;j<testMapReadResult.ways().get(i).nodesCount();j++){
                assertTrue(testMapReadResult.ways().get(i).nodes().get(j).attributes().isEmpty());
            }
        }
        assertFalse(testMapReadResult.relations().get(0).attributes().isEmpty());
        assertEquals(null, testMapReadResult.relations().get(0).attributes().get("action"));
        assertEquals(null, testMapReadResult.relations().get(0).attributes().get("visible"));

    }

    @Test
    public void learningCenter() {
    	try {
			OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/lc.osm.gz").getFile(), true);
		} catch (Exception e) {
		    fail("Unable to recognise the file and/or read it properly.");
		}
    }

    @Test (expected = FileNotFoundException.class)
    public void errorNameFile() throws Exception{
        OSMMapReader.readOSMFile("     ", true);
    }

    @Test (expected = SAXParseException.class)
    public void errorInFile() throws Exception{
        OSMMapReader.readOSMFile(getClass().getResource("/OSMtestFiles/testSimpleBuildingError.osm").getFile(), false);
    }

}
