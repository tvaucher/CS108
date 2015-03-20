package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a non-oriented graph made up of a collection of nodes (not
 * necessarily OSMNodes, though).
 * 
 * @author Maxime Kjaer (250694)
 * @author Timote Vaucher (246532)
 * 
 * @param <N>
 *            A node. In this project, nodes will be OSMEntities (though the
 *            class accepts anything)
 */
public final class Graph<N> {
    private final Map<N, Set<N>> graph;

    /**
     * Construct a new Graph object.
     * 
     * @param neighbors
     *            A Map of the neighbors. The keys of the Map each correspond to
     *            one node in the graph. For each key, the value will be a Set
     *            of the nodes that the key's node is connected to.
     */
    public Graph(Map<N, Set<N>> neighbors) {
        graph = Collections.unmodifiableMap(new HashMap<>(neighbors));
    }

    /**
     * A getter for the graph's nodes.
     * 
     * @return The Set of keys in the map.
     */
    public Set<N> nodes() {
        return graph.keySet();
        // TODO generates UOE if any modification ?
    }

    /**
     * A getter for a node's neighbors (the key associated to a certain node
     * during the construction).
     * 
     * @param node
     *            One of the nodes that were passed as an argument during the
     *            construction of the Graph.
     * @return The node's neighbors (in other words, the key that's tied to the
     *         node argument).
     * @throws IllegalArgumentException
     *             if the graph doesn't contain the node.
     */
    public Set<N> neighborsOf(N node) throws IllegalArgumentException {
        if (!graph.containsKey(node)) {
            throw new IllegalArgumentException("Graph doens't contain node");
        }
        return graph.get(node);
        // TODO generates UOE if any modification ?
    }

    /**
     * Builder associated to the Graph class. Enables easy and gradual
     * construction of a Graph object by adding the possibility of adding nodes
     * and edges one at a time.
     * 
     * @author Maxime Kjaer (250694)
     * @author Timote Vaucher (246532)
     * 
     * @param <N>
     *            The type of object that the graph's nodes will be (and that
     *            the Builder will accept.
     */
    public final static class Builder<N> {
        private Map<N, Set<N>> graph = new HashMap<>();

        /**
         * Add a node to the list of nodes that will be built when using the
         * build() method.
         * 
         * @param n
         *            A node object that will be added to the list of nodes in
         *            the graph.
         */
        public void addNode(N n) {
            if (!graph.containsKey(n)) {
                graph.put(n, new HashSet<>());
            }
        }

        /**
         * Adds an edge between two existing nodes.
         * 
         * @param n1
         *            One existing node that has previously been added with
         *            addNode().
         * @param n2
         *            Another existing node that has previously been added with
         *            addNode().
         * 
         * @throws IllegalArgumentException
         *             If at least one of the nodes that were passed as
         *             arguments do not exist.
         */
        public void addEdge(N n1, N n2) throws IllegalArgumentException {
            if (!graph.containsKey(n1) || !graph.containsKey(n2)) {
                throw new IllegalArgumentException(
                        "Graph doesn't contain at least one of the point");
            }
            graph.get(n1).add(n2);
            graph.get(n2).add(n1);
        }

        /**
         * Build a new Graph object from the data that has been inputted into
         * this Builder
         * 
         * @return a new Graph object made from the data that has been inputted
         *         into this Builder
         */
        public Graph<N> build() {
            return new Graph<>(graph);
        }
    }
}
