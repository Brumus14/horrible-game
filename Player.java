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
    private double mouseRotateSpeed = 0.001;
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
    public boolean win = false;
    private double breath;
    private double breathMax = 100;
    private Rectangle breathBackground;
    private Rectangle breathRect;
    private boolean breathCooldown = false;

    public Player(GameArena arena, double posX, double posY, double plane,
                  double sp, double rSp, Generator g, Raycaster r,
                  CursorManager c) {
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
        breath = breathMax;

        breathBackground =
            new Rectangle(c.getWidth() / 2 - 303, c.getHeight() - 43,
                          (double)606, (double)26, "grey");
        breathRect = new Rectangle(c.getWidth() / 2 - 300, c.getHeight() - 40,
                                   (double)600, (double)20, "blue");
        arena.addRectangle(breathBackground);
        arena.addRectangle(breathRect);
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
        if (arena.shiftPressed() && !crouched && breath > 0 &&
            !breathCooldown) {
            speed = origSpeed * 2;
            sprinting = true;
        } else {
            speed = origSpeed;
            sprinting = false;
        }

        if (arena.ctrlPressed() && breath > 0 && !breathCooldown) {
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

        if (arena.letterPressed('l')) {
            if (mouseRotateSpeed < 0.01) {
                mouseRotateSpeed += 0.00005;
            }
        }
        if (arena.letterPressed('k')) {
            if (mouseRotateSpeed > 0.00001) {
                mouseRotateSpeed -= 0.00001;
            }
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

        if (Math.sqrt(Math.pow(positionX - (gen.endLocation[0] + 0.5), 2) +
                      Math.pow(positionY - (gen.endLocation[1] + 0.5), 2)) <
            0.5) {
            win = true;
        }

        if (breath > 0) {
            if (crouched) {
                breath -= 0.25;
            } else if (moving && sprinting) {
                breath -= 0.75;
            }
        }
        if (breath < breathMax) {
            if (moving && !crouched && !sprinting) {
                breath += 0.25;
            } else if (!moving && !crouched && !sprinting) {
                breath += 0.5;
            }
        }

        breathRect.setWidth(6 * breath);
        breathRect.setXPosition(cursor.getWidth() / 2 - 300 +
                                (300 - 3 * breath));

        if (breath <= 0) {
            breathCooldown = true;
            breathRect.setColour("red");
        }
        if (breath >= breathMax && breathCooldown) {
            breathCooldown = false;
            breathRect.setColour("blue");
        }
    }
}
