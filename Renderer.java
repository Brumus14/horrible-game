public class Renderer {
    private Sprite[] sprites;
    private Rectangle[] lines;
    private double[] lineDepths;

    public Renderer(Sprite[] s, Rectangle[] l, double[] lD) {
        sprites = s;
        lines = l;
        lineDepths = lD;
    }

    public void render() {
        double depth = 0;
        Sprite spriteNext = null;
        Rectangle lineNext = null;
        boolean isLineNext = false;

        for (Sprite sprite : sprites) {
            if (sprite.getDepth() > depth) {
                spriteNext = sprite;
            }
        }

        // for ()
    }
}
