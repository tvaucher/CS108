package ch.epfl.imhof;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.imhof.osm.OSMNode;

public class GraphTest {
    private OSMNode node0 = new OSMNode.Builder(00000000, new PointGeo(0, 0)).build();
    private OSMNode node1 = new OSMNode.Builder(11111111, new PointGeo(Math.PI/4, Math.PI/4)).build();
    private OSMNode node2 = new OSMNode.Builder(22222222, new PointGeo(Math.PI/2, Math.PI/2)).build();
    
    
    
    @Test
    public void builderCanBuildWithoutNodes() {
        Graph.Builder<OSMNode> builder = new Graph.Builder<>();
        builder.build();
    }
    
    @Test
    public void basicUsageWorksAsIntended() {
        Graph.Builder<OSMNode> builder = new Graph.Builder<>();
        builder.addNode(node0);
        builder.addNode(node1);
        builder.addNode(node2);
        builder.addEdge(node0, node1);
        builder.addEdge(node0, node2);
        Graph<OSMNode> graph = builder.build();
        
        assertTrue(graph.nodes().contains(node0));
        assertTrue(graph.nodes().contains(node1));
        assertTrue(graph.nodes().contains(node2));
        
        assertTrue(graph.neighborsOf(node0).contains(node1));
        assertTrue(graph.neighborsOf(node0).contains(node2));
        assertFalse(graph.neighborsOf(node1).contains(node2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void cantAddEdgeToNonExistantNode() {
        Graph.Builder<OSMNode> builder = new Graph.Builder<>();
        builder.addNode(node0);
        builder.addEdge(node1, node2);
    }
    

}
