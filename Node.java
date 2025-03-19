public class Node {
    int[] locs;
    Node prevNode;

    public Node(int x, int y) {
        locs = new int[]{x, y};
        prevNode = null;
    }

    public Node(int x, int y, Node prevNode) {
        locs = new int[]{x, y};
        this.prevNode = prevNode;
    }
}
