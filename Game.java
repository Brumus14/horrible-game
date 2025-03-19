import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game {
    public static void main(String[] args) {
        // Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        // int screenWidth = screen.width;
        // int screenHeight = screen.height;

        GraphicsDevice graphics =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        int screenWidth = graphics.getDisplayMode().getWidth();
        int screenHeight = graphics.getDisplayMode().getHeight();

        GameArena arena = new GameArena(screenWidth, screenHeight);

        // GameMap map = new GameMap(5, 5);
        // map.SetMap(new int[][] {
        //     {1, 1, 1, 1, 1},
        //     {1, 0, 0, 1, 1},
        //     {1, 1, 0, 1, 1},
        //     {1, 0, 0, 0, 1},
        //     {1, 1, 1, 1, 1},
        // });

        GameMap map = new GameMap(24, 24);
        Generator g = new Generator();
        g.randomise();
        g.createPath();
        g.load(map);
        CursorManager c = new CursorManager(arena.getPanel(), screenWidth / 2,
                                            screenHeight / 2);
        c.toggleCursor();

        Raycaster raycaster = new Raycaster(arena, 480);

        // plane of 10 for drunk
        Player player = new Player(12.5, 1.5, 0.66, 0.02, 0.03, screenWidth, g,
                                   raycaster, c);
        Enemy jack = new Enemy(arena, g, player, raycaster);

        boolean paused = false;
        boolean escHeld = false;

        while (!arena.letterPressed('p')) {
            if (arena.escPressed() && !escHeld) {
                c.Update();
                c.toggleCursor();
                paused = !paused;
            }

            if (arena.escPressed()) {
                escHeld = true;
            } else {
                escHeld = false;
            }

            arena.pause();

            if (!paused) {
                player.HandleMovement(arena);
                // enemy.Update();
                raycaster.raycast(map, player);
                c.Update();
                jack.Update();
            }
        }

        JFrame frame =
            (JFrame)SwingUtilities.getWindowAncestor(arena.getPanel());
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
