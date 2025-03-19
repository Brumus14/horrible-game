public class Background {
    private Raycaster raycaster;
    private Rectangle[] lines;
    private Rectangle[] initialLines;

    public Background(GameArena arena, int lineCount, CursorManager cursor,
                      Raycaster rycst) {
        raycaster = rycst;
        lines = new Rectangle[lineCount];

        double height = cursor.getHeight() / lineCount;

        int r = 0x44;
        int g = 0x44;
        int b = 0x44;

        for (int i = 0; i < lineCount; i++) {
            if (i <= lineCount / 2) {
                double brightness = (1 - ((double)i / (lineCount / 2))) / 4;

                int red = (int)(brightness * r);
                int green = (int)(brightness * g);
                int blue = (int)(brightness * b);
                int colour = (red << 16) | (green << 8) | blue;

                Rectangle ceiling = lines[i];
                ceiling =
                    new Rectangle(0, i * height, cursor.getWidth(), height, "");
                ceiling.setDepth(999);
                ceiling.setColour(String.format("#%06x", colour));

                arena.addRectangle(ceiling);
            } else {
                double brightness =
                    ((double)(i - lineCount / 2) / (lineCount / 2));

                int red = (int)(brightness * r);
                int green = (int)(brightness * g);
                int blue = (int)(brightness * b);
                int colour = (red << 16) | (green << 8) | blue;

                Rectangle ground = lines[i];
                ground =
                    new Rectangle(0, i * height, cursor.getWidth(), height, "");
                ground.setDepth(999);
                ground.setColour(String.format("#%06x", colour));

                arena.addRectangle(ground);
            }
        }

        initialLines = lines;

        // double brightness = 0;
        //
        // int red = (int)(brightness * r);
        // int green = (int)(brightness * g);
        // int blue = (int)(brightness * b);
        // int colour = (red << 16) | (green << 8) | blue;
        //
        // Rectangle ceiling = new Rectangle(
        //     0, -cursor.getHeight(), cursor.getWidth(), cursor.getHeight(),
        //     "");
        // ceiling.setDepth(999);
        // ceiling.setColour(String.format("#%06x", colour));
        //
        // brightness = ;
        //
        // red = (int)(brightness * r);
        // green = (int)(brightness * g);
        // blue = (int)(brightness * b);
        // colour = (red << 16) | (green << 8) | blue;
        //
        // Rectangle ground = new Rectangle(
        //     0, -cursor.getHeight(), cursor.getWidth(), cursor.getHeight(),
        //     "");
        // ground.setDepth(999);
        // ground.setColour(String.format("#%06x", colour));
    }

    public void update() {
        for (int i = 0; i < lines.length; i++) {
        }
    }
}
