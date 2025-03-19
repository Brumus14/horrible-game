public class Node {
    int x;
    int y;
    Node prevNode;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        prevNode = null;
    }

    public Node(int x, int y, Node prevNode) {
        this.x = x;
        this.y = y;
        this.prevNode = prevNode;
    }
}
