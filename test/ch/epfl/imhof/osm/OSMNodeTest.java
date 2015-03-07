package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;

public class OSMNodeTest {
    private static final double DELTA = 0.000001;
    
    @Test(expected = IllegalStateException.class)
    public void builderCannotBuildIncompleteNode() {
        long id = 35295150;
        double longitude = Math.PI/4;
        double latitude = Math.PI/8;
        PointGeo position = new PointGeo(longitude, latitude);
        OSMNode.Builder b = new OSMNode.Builder(id, position);
        
        b.setIncomplete();
        assertTrue(b.isIncomplete());
        b.build();
    }
    
    @Test
    public void basicUsageWorksAsIntended() {
        long id = 35295150;
        double longitude = Math.PI/4;
        double latitude = Math.PI/8;
        PointGeo position = new PointGeo(longitude, latitude);
        OSMNode.Builder b = new OSMNode.Builder(id, position);
        b.setAttribute("test", "123");

        assertFalse(b.isIncomplete());
        OSMNode node = b.build();
        
        assertEquals(longitude, node.position().longitude(), DELTA);
        assertEquals(latitude, node.position().latitude(), DELTA);
        assertEquals(id, node.id());
        assertTrue(node.hasAttribute("test"));
        assertEquals("123", node.attributeValue("test"));
    }
    
    @Test
    public void builderUsesImmutability() {
        long id = 35295150;
        double longitude = Math.PI/4;
        double latitude = Math.PI/8;
        PointGeo position = new PointGeo(longitude, latitude);
        OSMNode.Builder b = new OSMNode.Builder(id, position);
        b.setAttribute("test", "123");
         
        longitude = 0;
        latitude = 0;
        id = 0;
        position = new PointGeo(0, 0);
        
        OSMNode node = b.build();
        
        assertEquals(35295150, node.id());
        assertEquals(Math.PI/4, node.position().longitude(), DELTA);
        assertEquals(Math.PI/8, node.position().latitude(), DELTA);
    }
    
    @Test
    public void emptyInputIsntAProblem() {
        long id = 35295150;
        double longitude = Math.PI/4;
        double latitude = Math.PI/8;
        PointGeo position = new PointGeo(longitude, latitude);
        OSMNode.Builder b = new OSMNode.Builder(id, position);
        b.setAttribute("", "test");
        b.setAttribute("123", "");
        
        OSMNode node = b.build();
        assertTrue(node.hasAttribute("123"));
        assertTrue(node.hasAttribute(""));
        
        assertEquals("test", node.attributeValue(""));
        assertEquals("", node.attributeValue("123"));
    }
}
