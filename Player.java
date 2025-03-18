public class Player {
    private double positionX;
    private double positionY;
    private double rotation;
    private double planeX;
    private double planeY;
    private double speed;
    private double rotationSpeed;

    public Player(double posX, double posY, double pX, double pY, double sp,
                  double rSp) {
        positionX = posX;
        positionY = posY;
        rotation = 0;
        planeX = pX;
        planeY = pY;
        speed = sp;
        rotationSpeed = rSp;
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
