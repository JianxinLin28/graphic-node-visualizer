public class Node {

    Object data;
    Node right;
    Node left;
    int x, y;

    Node() {
        data = null;
        right = null;
        left = null;
    }

    Node (Object data) {
        this.data = data;
    }

    public String toString() {
        return "x = " + x + ", y = " + y;
    }
}
