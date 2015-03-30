package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMEntity.Builder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OSMNodeTest extends OSMEntityTest {

    private static final PointGeo TEST_POINT_GEO = new PointGeo(0.125621, 0.803253);
    private static final double DELTA = 0.000001;

    @Override
    OSMEntity newEntity(long id, Attributes entityAttributes) {
        PointGeo testPointGeo = TEST_POINT_GEO;
        return new OSMNode(id, testPointGeo, entityAttributes);
    }

    @Override
    Builder newEntityBuilder() {
        PointGeo testPointGeo = TEST_POINT_GEO;
        return new OSMNode.Builder(1, testPointGeo);
    }

    @Test
    public void constructorAndPosition() {
        OSMNode testNode = new OSMNode(1, TEST_POINT_GEO, EMPTY_ATTRIBUTES);
        assertSame(testNode.position(), TEST_POINT_GEO);
    }

    @Test
    public void builderBuiltPosition() {
        OSMNode.Builder testBuild = new OSMNode.Builder(1, TEST_POINT_GEO);
        OSMNode testBuildResult = testBuild.build();
        assertSame(testBuildResult.position(), TEST_POINT_GEO);
    }

    // I put this back because it indeed is important to know that it gives us the expected exception.
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
