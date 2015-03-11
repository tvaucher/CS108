package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class OSMMapReaderTest {
    
    private OSMMap bcMap = OSMMapReader.readOSMFile("data/bc.osm", false);
    private OSMMap rlcMap = OSMMapReader.readOSMFile("data/lc.osm", false);
    
    
    // All expected values have been found by manually inspecting the OSM files
    // (use Ctrl+F to get the number of ways / nodes / relations).
    @Test
    public void allWaysAreSaved() {        
        assertEquals(1, bcMap.ways().size());
        assertEquals(49, rlcMap.ways().size());
    }
    
    @Test
    public void allRelationsAreSaved() {
        assertEquals(0, bcMap.relations().size());
        assertEquals(1, rlcMap.relations().size());
    }
    
    @Test
    public void tagsAreSavedCorrectly() {
        // This can't really be much more than simple random sample testing.
        // Nodes
        // See lines 41-46 for the node (id 566975629)
        // line 663 for the nd in the way (id 72320770)
        OSMNode node = rlcMap.ways().get(20).nodes().get(3);
        assertEquals("entrance", node.attributeValue("barrier"));
        assertEquals("no", node.attributeValue("bicycle"));
        assertEquals("entrance", node.attributeValue("building"));

        // Ways
        assertEquals("yes", bcMap.ways().get(0).attributeValue("building"));
        assertEquals("BC", bcMap.ways().get(0).attributeValue("name"));
        
        // Relations
        assertEquals("Rolex Learning Center", rlcMap.relations().get(0).attributeValue("name"));
    }
    
    
    
}
