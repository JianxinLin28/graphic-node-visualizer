import java.util.ArrayList;

public class GraphNode {
    Object data;
    ArrayList<GraphNode> neighbors;
    int x, y;
    int level;
    boolean selected = false;

    GraphNode() {
        data = null;
        neighbors = new ArrayList<>();
    }

    GraphNode(Object data) {
        this.data = data;
        neighbors = new ArrayList<>();
    }

    public boolean isEqual(GraphNode other) {
        return this.data.equals(other.data);
    }

    public void addNeighbor(GraphNode newNode) {
        if (newNode != null) {
            neighbors.add(newNode);
        }
    }

    public String toString() {
        return "data: " + data + " #Level" + level + " & y = " + y;
    }
}
