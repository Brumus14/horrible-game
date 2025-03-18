import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class CursorManager {
    Robot r;
    int posX, posY;
    Cursor visibleCursor;
    Cursor hiddenCursor;
    JPanel panel;
    JFrame frame;
    Boolean visible = true;

    public CursorManager(JPanel panel, int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.panel = panel;
        frame = ((JFrame)SwingUtilities.getWindowAncestor(panel));

        try {
            r = new Robot();
        } catch (AWTException e) {
            System.out.println("Error creating robot");
        }

        visibleCursor = ((JFrame)SwingUtilities.getWindowAncestor(panel))
                            .getContentPane()
                            .getCursor();

        BufferedImage hiddenImg =
            new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            hiddenImg, new Point(0, 0), "blank cursor");
    }

    public void Update() {
        r.mouseMove(posX, posY);
    }

    public void toggleCursor() {
        if (visible) {
            frame.getContentPane().setCursor(hiddenCursor);
            visible = false;
        } else {
            frame.getContentPane().setCursor(visibleCursor);
            visible = true;
        }
    }
}
