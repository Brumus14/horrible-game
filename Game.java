import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game {
    public static void main(String[] args) {
        GraphicsDevice graphics =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        int screenWidth = graphics.getDisplayMode().getWidth();
        int screenHeight = graphics.getDisplayMode().getHeight();

        GameArena arena = new GameArena(screenWidth, screenHeight);

        CursorManager c = new CursorManager(arena.getPanel(), screenWidth / 2,
                                            screenHeight / 2);
        c.toggleCursor();

        GameMap map = new GameMap(24, 24);
        Generator g = new Generator();
        g.randomise();
        g.createPath();
        g.load(map);

        Raycaster raycaster = new Raycaster(arena, 480);

        Background background = new Background(arena, 270, c, raycaster);

        Tutorial tutorial = new Tutorial(arena, c);

        // plane of 10 for drunk
        Player player =
            new Player(arena, 12.5, 1.5, 0.66, 0.02, 0.03, g, raycaster, c);
        Enemy jack = new Enemy(arena, g, player, raycaster, c);

        raycaster.setPlayer(player);

        boolean paused = false;
        boolean escHeld = false;

        boolean previousTPressed = false;

        while (!(arena.letterPressed('p') || jack.killPlayer || player.win)) {
            if (arena.letterPressed('t')) {
                if (!previousTPressed) {
                    tutorial.nextText();
                }

                previousTPressed = true;
            } else {
                previousTPressed = false;
            }

            if (arena.letterPressed('y')) {
                tutorial.endTutorial();
            }

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

            if (!paused && tutorial.isFinished()) {
                player.HandleMovement(arena);
                // enemy.Update();
                raycaster.raycast(map, player);
                c.Update();
                jack.Update();
                background.update();
            }
        }

        if (player.win) {
            arena.clearGameArena();

            while (!arena.letterPressed('q')) {
                arena.pause();

                Text text = new Text("YOU WIN", 200, c.getWidth() / 2 - 60 * 7,
                                     c.getHeight() / 2, "red");
                arena.addText(text);

                Text text1 = new Text("gg you are truly an epic gamer", 24,
                                      c.getWidth() / 2 - 6 * 30,
                                      c.getHeight() / 2 + 50, "red");
                Text text2 =
                    new Text("(press q to exit)", 24, c.getWidth() / 2 - 6 * 17,
                             c.getHeight() / 2 + 75, "red");

                arena.addText(text1);
                arena.addText(text2);
            }
        }

        JFrame frame =
            (JFrame)SwingUtilities.getWindowAncestor(arena.getPanel());
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
