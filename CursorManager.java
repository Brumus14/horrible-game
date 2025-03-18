import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class CursorManager {
    Robot r;
    int posX, posY;

    public CursorManager(JPanel panel, int posX, int posY) {
        this.posX = posX;
        this.posY = posY;

        try{
            r = new Robot();
        }
        catch(AWTException e){
            System.out.println("Error creating robot");
        }


        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");

        // Set the blank cursor to the JFrame.
        ((JFrame) SwingUtilities.getWindowAncestor(panel)).getContentPane().setCursor(blankCursor);
    }

    public void Update(){
        r.mouseMove(posX, posY);
    }
}
