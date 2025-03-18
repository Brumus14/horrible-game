import java.awt.*;

public class Player {
    private double positionX;
    private double positionY;
    private double rotation;
    private double planeX;
    private double planeY;
    private double speed;
    private double origSpeed;
    private double rotationSpeed;
    private double mouseRotateSpeed = 0.0005;
    private int screenWidth;

    public Player(double posX, double posY, double plane, double sp, double rSp,
                  int screenWidth) {
        positionX = posX;
        positionY = posY;
        rotation = 0;
        planeX = plane;
        planeY = 0;
        speed = sp;
        origSpeed = sp;
        rotationSpeed = rSp;
        this.screenWidth = screenWidth;
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

    public void HandleMovement(GameArena arena) {
        // speed increase if shift
        if (arena.shiftPressed()) {
            speed = origSpeed * 2;
        } else {
            speed = origSpeed;
        }

        // position movement
        if (arena.letterPressed('w')) {
            positionX += Math.sin(rotation) * speed;
            positionY += Math.cos(rotation) * speed;
        }

        if (arena.letterPressed('s')) {
            positionX -= Math.sin(rotation) * speed;
            positionY -= Math.cos(rotation) * speed;
        }

        if (arena.letterPressed('a')) {
            positionX -= Math.cos(rotation) * speed;
            positionY += Math.sin(rotation) * speed;
        }

        if (arena.letterPressed('d')) {
            positionX += Math.cos(rotation) * speed;
            positionY -= Math.sin(rotation) * speed;
        }

        if (arena.leftPressed()) {
            rotate(-rotationSpeed);
        }

        if (arena.rightPressed()) {
            rotate(rotationSpeed);
        }

        // rotation
        rotate((int)(MouseInfo.getPointerInfo().getLocation().getX() - (screenWidth / 2)) * mouseRotateSpeed);

        while (rotation < 0) {
            rotation += 2 * Math.PI;
        }

        while (rotation >= 2 * Math.PI) {
            rotation -= 2 * Math.PI;
        }

        planeX = 0.66 * Math.cos(rotation);
        planeY = 0.66 * -Math.sin(rotation);
    }
}
