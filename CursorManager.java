import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;

public class CursorManager {
    Robot r;
    int posX, posY;
    Cursor visibleCursor;
    Cursor hiddenCursor;
    JPanel panel;
    JFrame frame;
    Boolean visible = true;
    GraphicsDevice previousDevice;

    public CursorManager(JPanel panel, int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.panel = panel;
        frame = ((JFrame)SwingUtilities.getWindowAncestor(panel));

        previousDevice = frame.getGraphicsConfiguration().getDevice();

        resetRobot();

        visibleCursor = ((JFrame)SwingUtilities.getWindowAncestor(panel))
                            .getContentPane()
                            .getCursor();

        BufferedImage hiddenImg =
            new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            hiddenImg, new Point(0, 0), "blank cursor");
    }

    public void Update() {
        if (previousDevice != frame.getGraphicsConfiguration().getDevice()) {
            resetRobot();
        }

        previousDevice = frame.getGraphicsConfiguration().getDevice();

        GraphicsDevice device = frame.getGraphicsConfiguration().getDevice();
        java.awt.Rectangle deviceBounds =
            device.getDefaultConfiguration().getBounds();

        r.mouseMove(
            (int)deviceBounds.getX() + device.getDisplayMode().getWidth() / 2,
            (int)deviceBounds.getY() + device.getDisplayMode().getHeight() / 2);
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

    public void resetRobot() {
        try {
            r = new Robot(frame.getGraphicsConfiguration().getDevice());
        } catch (AWTException e) {
            System.out.println("Error creating robot");
        }
    }

    public double getX() {
        GraphicsDevice device = frame.getGraphicsConfiguration().getDevice();
        java.awt.Rectangle deviceBounds =
            device.getDefaultConfiguration().getBounds();

        return MouseInfo.getPointerInfo().getLocation().getX() -
            deviceBounds.getX();
    }

    public double getWidth() {
        return frame.getGraphicsConfiguration()
            .getDevice()
            .getDisplayMode()
            .getWidth();
    }

    public double getHeight() {
        return frame.getGraphicsConfiguration()
            .getDevice()
            .getDisplayMode()
            .getHeight();
    }
}
