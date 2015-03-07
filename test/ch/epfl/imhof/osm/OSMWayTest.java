package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ch.epfl.imhof.PointGeo;

public class OSMWayTest {


    @Test(expected = IllegalStateException.class)
    public void builderCannotBuildWithoutNodes() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        b.build();
    }
    
    @Test(expected = IllegalStateException.class)
    public void builderCannotBuildWithOneNode() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        PointGeo positionOne = new PointGeo(Math.PI/4, Math.PI/8);
        OSMNode.Builder builderOne = new OSMNode.Builder(35295150, positionOne);
        b.addNode(builderOne.build());
        b.build();
    }
     
    @Test
    public void builderIncompletionWorks() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        PointGeo positionOne = new PointGeo(Math.PI/4, Math.PI/8);
        PointGeo positionTwo = new PointGeo(2*Math.PI/9, Math.PI/3);
        OSMNode.Builder builderOne = new OSMNode.Builder(35295150, positionOne);
        OSMNode.Builder builderTwo = new OSMNode.Builder(25125354, positionTwo); //some random spot in Sweden
        
        OSMNode node1 = builderOne.build();
        b.addNode(node1);
        assertTrue(b.isIncomplete());
        OSMNode node2 = builderTwo.build();
        b.addNode(node2);
        assertFalse(b.isIncomplete());
        b.setIncomplete();
        assertTrue(b.isIncomplete());
    }
    
    @Test
    public void IDsAreSavedCorrectly() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        PointGeo positionOne = new PointGeo(Math.PI/4, Math.PI/8);
        PointGeo positionTwo = new PointGeo(2*Math.PI/9, Math.PI/3);
        OSMNode.Builder builderOne = new OSMNode.Builder(35295150, positionOne);
        OSMNode.Builder builderTwo = new OSMNode.Builder(25125354, positionTwo); //some random spot in Sweden
        OSMNode node1 = builderOne.build();
        OSMNode node2 = builderTwo.build();
        b.addNode(node1);
        b.addNode(node2);

        OSMWay way = b.build();
        assertEquals(51740696, way.id());
        assertEquals(35295150, way.firstNode().id());
        assertEquals(25125354, way.lastNode().id());
    }
    
    @Test
    public void nodesCountReturnsCorrectValues() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        PointGeo positionOne = new PointGeo(Math.PI/4, Math.PI/8);
        PointGeo positionTwo = new PointGeo(2*Math.PI/9, Math.PI/3);
        OSMNode.Builder builderOne = new OSMNode.Builder(35295150, positionOne);
        OSMNode.Builder builderTwo = new OSMNode.Builder(25125354, positionTwo); //some random spot in Sweden
        OSMNode node1 = builderOne.build();
        OSMNode node2 = builderTwo.build();
        b.addNode(node1);
        b.addNode(node2);
        OSMWay way = b.build();
        
        assertEquals(2, way.nodesCount());
        
        OSMWay.Builder b2 = new OSMWay.Builder(51740696);
        b2.addNode(node1);
        b2.addNode(node2);
        b2.addNode(node1);
        OSMWay way2 = b2.build();
        
        assertEquals(way2.nodes().size(), way2.nodesCount());
        assertEquals(3, way2.nodesCount());
    }
    
    @Test
    public void firstNodeAndLastNodeReturnCorrectValues() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        PointGeo positionOne = new PointGeo(Math.PI/4, Math.PI/8);
        PointGeo positionTwo = new PointGeo(2*Math.PI/9, Math.PI/3);
        OSMNode.Builder builderOne = new OSMNode.Builder(35295150, positionOne);
        OSMNode.Builder builderTwo = new OSMNode.Builder(25125354, positionTwo); //some random spot in Sweden
        OSMNode node1 = builderOne.build();
        OSMNode node2 = builderTwo.build();
        b.addNode(node1);
        b.addNode(node1);
        b.addNode(node2);
        b.addNode(node1);
        b.addNode(node1);
        b.addNode(node2);
        OSMWay way = b.build();
        
        assertSame(node1, way.firstNode());
        assertSame(node2, way.lastNode());
    }
    
    @Test
    public void isClosedReturnsCorrectValues() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        PointGeo positionOne = new PointGeo(Math.PI/4, Math.PI/8);
        PointGeo positionTwo = new PointGeo(2*Math.PI/9, Math.PI/3);
        OSMNode.Builder builderOne = new OSMNode.Builder(35295150, positionOne);
        OSMNode.Builder builderTwo = new OSMNode.Builder(25125354, positionTwo); //some random spot in Sweden
        OSMNode node1 = builderOne.build();
        OSMNode node2 = builderTwo.build();
        b.addNode(node1);
        b.addNode(node2);
        
        OSMWay way1 = b.build();
        assertFalse(way1.isClosed());
        
        b.addNode(node2);
        b.addNode(node1);
        OSMWay way2 = b.build();
        assertTrue(way2.isClosed());
    }
    
    @Test
    public void nodesGetterReturnsCorrectValues() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        PointGeo positionOne = new PointGeo(Math.PI/4, Math.PI/8);
        PointGeo positionTwo = new PointGeo(2*Math.PI/9, Math.PI/3);
        OSMNode.Builder builderOne = new OSMNode.Builder(35295150, positionOne);
        OSMNode.Builder builderTwo = new OSMNode.Builder(25125354, positionTwo); //some random spot in Sweden
        OSMNode node1 = builderOne.build();
        OSMNode node2 = builderTwo.build();
        b.addNode(node1);
        b.addNode(node2);
        b.addNode(node1);
        OSMWay way = b.build();
        
        assertSame(node1, way.nodes().get(0));
        assertSame(node2, way.nodes().get(1));
        assertSame(node1, way.nodes().get(2));
    }
    
    @Test
    public void nonRepeatingNodesReturnsCorrectValues() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        PointGeo positionOne = new PointGeo(Math.PI/4, Math.PI/8);
        PointGeo positionTwo = new PointGeo(2*Math.PI/9, Math.PI/3);
        OSMNode.Builder builderOne = new OSMNode.Builder(35295150, positionOne);
        OSMNode.Builder builderTwo = new OSMNode.Builder(25125354, positionTwo); //some random spot in Sweden
        OSMNode node1 = builderOne.build();
        OSMNode node2 = builderTwo.build();
        b.addNode(node1);
        b.addNode(node2);
        b.addNode(node1);
        OSMWay way = b.build();

        //With a closed way
        assertTrue(way.isClosed());
        assertEquals(2, way.nonRepeatingNodes().size());
        assertSame(node1, way.nonRepeatingNodes().get(0));
        assertSame(node2, way.nonRepeatingNodes().get(1));
        
        //With an open way
        b.addNode(node2);
        OSMWay way2 = b.build();
        assertFalse(way2.isClosed());
        assertEquals(4, way2.nonRepeatingNodes().size());
        assertSame(node1, way2.nonRepeatingNodes().get(0));
        assertSame(node2, way2.nonRepeatingNodes().get(1));
        assertSame(node1, way2.nonRepeatingNodes().get(2));
        assertSame(node2, way2.nonRepeatingNodes().get(3));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void cantAddToNodesList() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        PointGeo positionOne = new PointGeo(Math.PI/4, Math.PI/8);
        PointGeo positionTwo = new PointGeo(2*Math.PI/9, Math.PI/3);
        OSMNode.Builder builderOne = new OSMNode.Builder(35295150, positionOne);
        OSMNode.Builder builderTwo = new OSMNode.Builder(25125354, positionTwo); //some random spot in Sweden
        OSMNode node1 = builderOne.build();
        OSMNode node2 = builderTwo.build();
        b.addNode(node1);
        b.addNode(node2);
        b.addNode(node1);
        OSMWay way = b.build();
        
        List<OSMNode> list = way.nodes();
        list.add(node1);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void cantAddToNonRepeatingNodesList() {
        OSMWay.Builder b = new OSMWay.Builder(51740696);
        PointGeo positionOne = new PointGeo(Math.PI/4, Math.PI/8);
        PointGeo positionTwo = new PointGeo(2*Math.PI/9, Math.PI/3);
        OSMNode.Builder builderOne = new OSMNode.Builder(35295150, positionOne);
        OSMNode.Builder builderTwo = new OSMNode.Builder(25125354, positionTwo); //some random spot in Sweden
        OSMNode node1 = builderOne.build();
        OSMNode node2 = builderTwo.build();
        b.addNode(node1);
        b.addNode(node2);
        b.addNode(node1);
        OSMWay way = b.build();
        
        List<OSMNode> list = way.nonRepeatingNodes();
        list.add(node1);
    }
}
