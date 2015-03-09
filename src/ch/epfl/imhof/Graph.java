package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Graph<N> {
    private final Map<N, Set<N>> graph;
    
    public Graph(Map<N, Set<N>> neighbors) {
        graph = Collections.unmodifiableMap(new HashMap<>(neighbors));
    }
    
    public Set<N> nodes() {
        return graph.keySet();
        //TODO generates UOE if any modification ?
    }
    
    public Set<N> neighborsOf(N node) throws IllegalArgumentException {
        if (!graph.containsKey(node)) {
            throw new IllegalArgumentException("Graph doens't contain node");
        }
        return graph.get(node);
        //TODO generates UOE if any modification ?
    }
    
    public final static class Builder<N> {
        Map<N, Set<N>> graph = new HashMap<>();
        
        public void addNode(N n) {
            if (!graph.containsKey(n)) {
                graph.put(n, new HashSet<>());
            }
        }
        
        public void addEdge(N n1, N n2) throws IllegalArgumentException {
            if (!graph.containsKey(n1) || !graph.containsKey(n2)) {
                throw new IllegalArgumentException("Graph doesn't contain at least one of the point");
            }
            graph.get(n1).add(n2);
            graph.get(n2).add(n1);
        }
        
        public Graph<N> build() {
            return new Graph<>(graph);
        }
    }
}
