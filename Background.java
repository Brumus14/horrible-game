public class Background {
    private Raycaster raycaster;
    private Rectangle[] lines;
    private double height;

    public Background(GameArena arena, int lineCount, CursorManager cursor,
                      Raycaster rycst) {
        raycaster = rycst;
        lines = new Rectangle[lineCount];

        height = cursor.getHeight() / lineCount;

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

                Rectangle ceiling =
                    new Rectangle(0, i * height, cursor.getWidth(), height, "");
                ceiling.setDepth(999);
                ceiling.setColour(String.format("#%06x", colour));

                lines[i] = ceiling;
            } else {
                double brightness =
                    ((double)(i - lineCount / 2) / (lineCount / 2));

                int red = (int)(brightness * r);
                int green = (int)(brightness * g);
                int blue = (int)(brightness * b);
                int colour = (red << 16) | (green << 8) | blue;

                Rectangle ground =
                    new Rectangle(0, i * height, cursor.getWidth(), height, "");
                ground.setDepth(999);
                ground.setColour(String.format("#%06x", colour));

                if (i == lineCount - 1) {
                    ground.setHeight(cursor.getHeight());
                }

                lines[i] = ground;
            }

            arena.addRectangle(lines[i]);
        }
    }

    public void update() {
        for (int i = 0; i < lines.length; i++) {
            lines[i].setYPosition(i * height + raycaster.getLineOffsetY());
        }
    }
}
