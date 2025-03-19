public class Raycaster {
    private Rectangle[] lines;
    private double[] lineDepths;
    private int lineCount;
    private GameArena arena;
    private double lineOffsetY = 0;
    private Player player;
    private double brightessFactor = 0;

    public Raycaster(GameArena arena, int lineCount) {
        this.arena = arena;
        this.lineCount = lineCount;
        lines = new Rectangle[lineCount];
        lineDepths = new double[lineCount];

        double lineWidth = (double)arena.getWidth() / lineCount;

        for (int i = 0; i < lineCount; i++) {
            lines[i] = new Rectangle(lineWidth * i, 0, lineWidth, 0, "black");
            arena.addRectangle(lines[i]);
        }
    }

    public void setPlayer(Player p) {
        player = p;
    }

    public double getBrightnessFactor() {
        return brightessFactor;
    }

    public int getLineCount() {
        return lineCount;
    }

    public double getLineDepth(int line) {
        return lineDepths[line];
    }

    public Rectangle[] getLines() {
        return lines;
    }

    public double[] getLineDepths() {
        return lineDepths;
    }

    public double getLineOffsetY() {
        return lineOffsetY;
    }

    public void setLineOffsetY(double o) {
        lineOffsetY = o;
    }

    // y is flipped
    public void raycast(GameMap map, Player player) {
        if (player.isCrouching()) {
            brightessFactor = Math.max(1.0, brightessFactor - 0.1);
        } else {
            brightessFactor = Math.min(1.5, brightessFactor + 0.1);
        }

        for (int x = 0; x < lineCount; x++) {
            double cameraX = 2.0 * x / lineCount - 1;
            double rayDirX =
                player.getDirectionX() + player.getPlaneX() * cameraX;
            double rayDirY =
                player.getDirectionY() + player.getPlaneY() * cameraX;

            int mapX = (int)player.getPositionX();
            int mapY = (int)player.getPositionY();

            double rayDirLength =
                Math.sqrt(Math.pow(rayDirX, 2) + Math.pow(rayDirY, 2));
            double deltaDistX =
                Math.abs(rayDirLength / (rayDirX != 0 ? rayDirX : 1e-6));
            double deltaDistY =
                Math.abs(rayDirLength / (rayDirY != 0 ? rayDirY : 1e-6));
            double perpWallDist;

            double sideDistX;
            double sideDistY;

            int stepX;
            int stepY;

            boolean hit = false;
            int side = 0;

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (player.getPositionX() - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1 - player.getPositionX()) * deltaDistX;
            }

            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (player.getPositionY() - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1 - player.getPositionY()) * deltaDistY;
            }

            while (!hit && mapX < map.getWidth() && mapY < map.getHeight()) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }

                if (map.GetTile(mapX, mapY) > 0) {
                    hit = true;
                }
            }

            if (!hit) {
                return;
            }

            if (side == 0) {
                perpWallDist = (sideDistX - deltaDistX);
            } else {
                perpWallDist = (sideDistY - deltaDistY);
            }

            lineDepths[x] = perpWallDist;

            Rectangle line = lines[x];
            line.setDepth(perpWallDist);

            int h = arena.getHeight();

            int lineHeight = (int)(h / perpWallDist);
            int drawStart = -lineHeight / 2 + h / 2;

            int drawEnd = lineHeight / 2 + h / 2;

            line.setYPosition(drawStart + lineOffsetY);
            line.setHeight(drawEnd - drawStart);

            double brightness =
                Math.min(Math.exp(-perpWallDist / brightessFactor), 0.5);
            int red = (int)(brightness * 255);
            int green = (int)(brightness * 255);
            int blue = (int)(brightness * 255);
            int colour;

            switch (map.GetTile(mapX, mapY)) {
            case 1:
                colour = ((int)(brightness * 53) << 16) |
                         ((int)(brightness * 53) << 8) | (int)(brightness * 53);
                line.setColour(String.format("#%06x", colour));
                break;
            case 2:
                colour = (0 << 16) | (green << 8) | 0;
                line.setColour(String.format("#%06x", colour));
                break;
            case 3:
                colour = (0 << 16) | (0 << 8) | blue;
                line.setColour(String.format("#%06x", colour));
                break;
            case 4:
                colour = (red << 16) | (green << 8) | 0;
                line.setColour(String.format("#%06x", colour));
                break;
            }
        }
    }
}
