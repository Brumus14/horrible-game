public class Raycaster {
    private Rectangle[] lines;
    private int lineCount;
    private GameArena arena;

    public Raycaster(GameArena arena, int lineCount) {
        this.arena = arena;
        this.lineCount = lineCount;
        lines = new Rectangle[lineCount];

        double lineWidth = (double)arena.getWidth() / lineCount;

        for (int i = 0; i < lineCount; i++) {
            lines[i] = new Rectangle(lineWidth * i, 0, lineWidth, 0, "black");
            arena.addRectangle(lines[i]);
        }
    }

    public void raycast(GameMap map, Player player) {
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

            if (side == 0) {
                perpWallDist = (sideDistX - deltaDistX);
            } else {
                perpWallDist = (sideDistY - deltaDistY);
            }

            Rectangle line = lines[x];

            int h = arena.getHeight();

            int lineHeight = (int)(h / perpWallDist);
            int drawStart = -lineHeight / 2 + h / 2;

            if (drawStart < 0) {
                drawStart = 0;
            }

            int drawEnd = lineHeight / 2 + h / 2;

            if (drawEnd >= h) {
                drawEnd = h - 1;
            }

            line.setYPosition(drawStart);
            line.setHeight(drawEnd - drawStart);

            double brightness = Math.min(1, 2 / perpWallDist);
            int red = (int)(brightness * 255);
            int green = (int)(brightness * 255);
            int blue = (int)(brightness * 255);
            int colour;

            switch (map.GetTile(mapX, mapY)) {
            case 1:
                colour = (red << 16) | (0 << 8) | 0;
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
