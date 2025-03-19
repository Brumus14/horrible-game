import java.awt.*;

public class Player {
    private double positionX;
    private double positionY;
    private double rotation;
    private double planeX;
    private double planeY;
    private double initialPlane;
    private double speed;
    private double origSpeed;
    private double rotationSpeed;
    private double mouseRotateSpeed = 0.0005;
    private double centreX;
    public boolean moving = false;
    public boolean crouched = false;
    public boolean sprinting = false;
    private Generator gen;
    private double hitboxSize = 0.2;
    private boolean mouseRotation = true;
    private boolean mouseHeld;
    private Raycaster raycaster;
    private CursorManager cursor;
    public boolean win;

    public Player(double posX, double posY, double plane, double sp, double rSp,
                  int screenWidth, Generator g, Raycaster r, CursorManager c) {
        positionX = posX;
        positionY = posY;
        rotation = 0;
        planeX = plane;
        planeY = 0;
        initialPlane = plane;
        speed = sp;
        origSpeed = sp;
        rotationSpeed = rSp;
        gen = g;
        raycaster = r;
        cursor = c;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double posX) {
        positionX = posX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double posY) {
        positionY = posY;
    }

    public double getDirectionX() {
        return Math.sin(rotation);
    }

    public double getDirectionY() {
        return Math.cos(rotation);
    }

    public double getPlaneX() {
        return planeX;
    }

    public double getPlaneY() {
        return planeY;
    }

    public void rotate(double r) {
        rotation += r;
    }

    public boolean isCrouching() {
        return crouched;
    }

    public void HandleMovement(GameArena arena) {
        double xChange = 0;
        double yChange = 0;

        // speed increase if shift
        if (arena.shiftPressed() && !crouched) {
            speed = origSpeed * 2;
            sprinting = true;
        } else {
            speed = origSpeed;
            sprinting = false;
        }

        if (arena.ctrlPressed()) {
            crouched = true;
            speed = origSpeed * 0.5;

            double offset = raycaster.getLineOffsetY();

            if (offset > -300) {
                raycaster.setLineOffsetY(offset - 40);
            }
        } else {
            crouched = false;

            double offset = raycaster.getLineOffsetY();

            if (offset < 0) {
                raycaster.setLineOffsetY(offset + 40);
            }
        }

        // position movement
        if (arena.letterPressed('w')) {
            xChange += Math.sin(rotation);
            yChange += Math.cos(rotation);
        }

        if (arena.letterPressed('s')) {
            xChange -= Math.sin(rotation);
            yChange -= Math.cos(rotation);
        }

        if (arena.letterPressed('a')) {
            xChange -= Math.cos(rotation);
            yChange += Math.sin(rotation);
        }

        if (arena.letterPressed('d')) {
            xChange += Math.cos(rotation);
            yChange -= Math.sin(rotation);
        }

        double normaliser =
            (Math.sqrt(Math.pow(xChange, 2) + Math.pow(yChange, 2)));
        xChange /= normaliser;
        yChange /= normaliser;
        xChange *= speed;
        yChange *= speed;

        if (gen.area[(int)positionY][(
                int)(positionX + xChange +
                     (hitboxSize * (xChange / Math.abs(xChange))))] != 1) {
            positionX += xChange;
        }
        if (gen.area[(int)(positionY + yChange +
                           (hitboxSize * (yChange / Math.abs(yChange))))]
                    [(int)positionX] != 1) {
            positionY += yChange;
        }

        if (arena.leftPressed()) {
            rotate(-rotationSpeed);
        }

        if (arena.rightPressed()) {
            rotate(rotationSpeed);
        }

        // rotation
        if (mouseRotation) {
            rotate((cursor.getX() - (cursor.getWidth() / 2)) *
                   mouseRotateSpeed);
        }

        while (rotation < 0) {
            rotation += 2 * Math.PI;
        }

        while (rotation >= 2 * Math.PI) {
            rotation -= 2 * Math.PI;
        }

        planeX = initialPlane * Math.cos(rotation);
        planeY = initialPlane * -Math.sin(rotation);

        if ((arena.letterPressed('w') || arena.letterPressed('a') ||
             arena.letterPressed('d') || arena.letterPressed('s')) &&
            !crouched) {
            moving = true;
        } else {
            moving = false;
        }

        if (arena.letterPressed('m')) {
            if (!mouseHeld) {
                mouseRotation = !mouseRotation;
            }
            mouseHeld = true;
        } else {
            mouseHeld = false;
        }

        if(Math.sqrt(Math.pow(positionX - (gen.endLocation[0] + 0.5), 2) +
                Math.pow(positionY - (gen.endLocation[1] + 0.5), 2)) < 0.5){
            win = true;
        }
    }
}
