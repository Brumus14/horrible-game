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
    public boolean makingNoise = false;
    private boolean crouched = false;
    private Generator gen;
    private double hitboxSize = 0;

    public Player(double posX, double posY, double plane, double sp, double rSp,
                  int screenWidth, Generator g) {
        positionX = posX;
        positionY = posY;
        rotation = 0;
        planeX = plane;
        planeY = 0;
        speed = sp;
        origSpeed = sp;
        rotationSpeed = rSp;
        this.screenWidth = screenWidth;
        gen = g;
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
        double xChange = 0;
        double yChange = 0;

        // speed increase if shift
        if (arena.shiftPressed() && !crouched) {
            speed = origSpeed * 2;
        } else {
            speed = origSpeed;
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

        double normaliser = (Math.sqrt(Math.pow(xChange, 2) + Math.pow(yChange, 2)));
        xChange /= normaliser;
        yChange /= normaliser;
        xChange *= speed;
        yChange *= speed;

        if(gen.area[(int)positionY][(int)(positionX + xChange + (hitboxSize * (xChange/Math.abs(xChange))))] == 0) {
            positionX += xChange;
        }
        if(gen.area[(int)(positionY + yChange + (hitboxSize * (yChange/Math.abs(yChange))))][(int)positionX] == 0) {
            positionY += yChange;
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

        if((arena.letterPressed('w') || arena.letterPressed('a') || arena.letterPressed('d') || arena.letterPressed('s')) && !crouched){
            makingNoise = true;
        }
        else{
            makingNoise = false;
        }
    }
}
