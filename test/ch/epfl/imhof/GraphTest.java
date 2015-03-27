package ch.epfl.imhof;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class GraphTest {

    @Test
    public void constructorImmutability() {

        HashMap<Integer, Set<Integer>> adjacencyList = createAdjacencyList();
        Graph<Integer> graph = new Graph<>(adjacencyList);
        int immutableCount = 0;

        for (Map.Entry<Integer, Set<Integer>> mapping: adjacencyList.entrySet()) {

            Set<Integer> valid = new HashSet<>(mapping.getValue());
            mapping.getValue().clear();

            assertEquals(valid, graph.neighborsOf(mapping.getKey()));
            try {
                graph.neighborsOf(mapping.getKey()).clear();
            } catch(UnsupportedOperationException e) {
                immutableCount += 1;
            }
        }

        adjacencyList.clear();
        assertNotEquals(graph.nodes().size(), 0);

        try {
            graph.nodes().clear();
        } catch(UnsupportedOperationException e) {
            immutableCount += 1;
        }

        if (immutableCount != createAdjacencyList().size() + 1) {
            fail("Constructor does not guarantee immutability.");
        }
    }

    @Test
    public void nodesComplete() {

        HashMap<Integer, Set<Integer>> adjacencyList = createAdjacencyList();
        Graph<Integer> graph = new Graph<>(adjacencyList);

        assertEquals(adjacencyList.keySet(), graph.nodes());
    }

    @Test
    public void correctNeighborsOf() {

        HashMap<Integer, Set<Integer>> adjacencyList = createAdjacencyList();
        Graph<Integer> graph = new Graph<>(adjacencyList);

        for (Map.Entry<Integer, Set<Integer>> mapping: adjacencyList.entrySet()) {
            assertEquals(mapping.getValue(), graph.neighborsOf(mapping.getKey()));
        }
    }

    @Test
    public void builderBuilt() {

        HashMap<Integer, Set<Integer>> adjacencyList = createAdjacencyList();
        Graph.Builder<Integer> graphBuilder = new Graph.Builder<>();

        Set<Pair> edges = new HashSet<>();
        for (Integer from: adjacencyList.keySet()) {
            for (Integer to: adjacencyList.get(from)) {
                edges.add(new Pair(from, to));
            }
        }

        for (Integer node: adjacencyList.keySet()) {
            graphBuilder.addNode(node);
        }
        for (Pair edge: edges) {
            graphBuilder.addEdge(edge.start, edge.end);
        }

        Graph<Integer> graph = graphBuilder.build();
        assertEquals(adjacencyList.keySet(), graph.nodes());

        for (Map.Entry<Integer, Set<Integer>> mapping: adjacencyList.entrySet()) {
            assertEquals(mapping.getValue(), graph.neighborsOf(mapping.getKey()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void neighborsOfThrowsExceptionWhenUnknownNode() {

        new Graph<Integer>(Collections.emptyMap()).neighborsOf(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderThrowsExceptionWhenUnknownNode1() {

        Graph.Builder<Integer> graphBuilder = new Graph.Builder<>();
        Integer node = 1;
        graphBuilder.addNode(node);
        graphBuilder.addEdge(node, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderThrowsExceptionWhenUnknownNode2() {

        Graph.Builder<Integer> graphBuilder = new Graph.Builder<>();
        Integer node = 1;
        graphBuilder.addNode(node);
        graphBuilder.addEdge(2, node);
    }

    // undirected edge as an ordered pair
    private final class Pair {
        final Integer start, end;
        Pair(Integer start, Integer end) {
            if (start < end) {
                this.start = start;
                this.end = end;
            } else {
                this.start = end;
                this.end = start;
            }
        }
    }

    private HashMap<Integer, Set<Integer>> createAdjacencyList() {

        HashMap<Integer, Set<Integer>> adjacencyList = new HashMap<>();

        adjacencyList.put(1,  createNodeSet(6));
        adjacencyList.put(2,  createNodeSet(5));
        adjacencyList.put(3,  createNodeSet(5,6,10));
        adjacencyList.put(4,  createNodeSet());
        adjacencyList.put(5,  createNodeSet(2,3,10));
        adjacencyList.put(6,  createNodeSet(1,3));
        adjacencyList.put(10, createNodeSet(3,5));

        return adjacencyList;
    }

    private Set<Integer> createNodeSet(Integer... edges) {

        Set<Integer> nodeSet = new HashSet<>();

        for (Integer edge: edges) {
            nodeSet.add(edge);
        }

        return nodeSet;
    }

}
