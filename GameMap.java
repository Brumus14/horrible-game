public class GameMap {
    private int[][] map;
    private int width;
    private int height;

    public GameMap(int w, int h) {
        width = w;
        height = h;
        map = new int[h][w];
    }

    public int GetTile(int x, int y) {
        if (x < 0 || x >= map[0].length || y < 0 || y >= map.length) {
            return 0;
        }

        return map[y][x];
    }

    public void SetTile(int x, int y, int tile) {
        map[y][x] = tile;
    }

    public void SetMap(int[][] m) {
        map = m;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
