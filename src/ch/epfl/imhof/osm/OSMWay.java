package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

public final class OSMWay extends OSMEntity {
    private final List<OSMNode> nodes;

    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes)
            throws IllegalArgumentException {
        super(id, attributes);
        if (nodes.size() < 2)
            throw new IllegalArgumentException(
                    "List of node must contain at least 2 elements");
        this.nodes = Collections
                .unmodifiableList(new ArrayList<OSMNode>(nodes));
    }

    public int nodesCount() {
        return nodes.size();
    }

    public List<OSMNode> nodes() {
        return nodes;
    }

    public List<OSMNode> nonRepeatingNodes() {
        if (isClosed()) {
            return Collections.unmodifiableList(new ArrayList<>(nodes.subList(
                    0, nodes.size() - 1)));
            // TODO JUnit absolutely on this one
        }
        return nodes;
    }

    public OSMNode firstNode() {
        return nodes.get(0);
    }

    public OSMNode lastNode() {
        return nodes.get(nodes.size() - 1);
    }

    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }

    public static final class Builder extends OSMEntity.Builder {
        private List<OSMNode> nodes;

        public Builder(long id) {
            super(id);
            nodes = new ArrayList<>();
        }

        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }

        public OSMWay build() throws IllegalStateException {
            if (isIncomplete())
                throw new IllegalStateException("Cannot build Incomplete Way");
            return new OSMWay(id, nodes, b.build());
        }

        @Override
        public boolean isIncomplete() {
            if (nodes.size() < 2)
                return true;
            return super.isIncomplete();
        }
    }
}
