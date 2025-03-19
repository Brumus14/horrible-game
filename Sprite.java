public class Sprite {
    private GameArena arena;
    private Rectangle shape;
    private double positionX;
    private double positionY;
    private double width;
    private double height;
    private double depth;
    private Player player;

    public Sprite(GameArena a, Player p, double x, double y, double w, double h,
                  String colour) {
        arena = a;
        player = p;
        positionX = x;
        positionY = y;
        width = w;
        height = h;
        shape = new Rectangle(0, 0, 0, 0, colour);
        arena.addRectangle(shape);
    }

    public double getDepth() {
        return depth;
    }

    public Rectangle getShape() {
        return shape;
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

    public void move(double x, double y) {
        positionX += x;
        positionY += y;
    }

    public void Update() {
        double deltaX = player.getPositionX() - positionX;
        double deltaY = player.getPositionY() - positionY;

        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        shape.setWidth(width / distance);
        shape.setHeight(height / distance);

        double dx = positionX - player.getPositionX();
        double dy = positionY - player.getPositionY();

        double dirX = player.getDirectionX();
        double dirY = player.getDirectionY();
        double planeX = player.getPlaneX();
        double planeY = player.getPlaneY();

        double invDet = 1.0 / (planeX * dirY - dirX * planeY);
        double transformX = invDet * (dirY * dx - dirX * dy);
        double transformY = invDet * (-planeY * dx + planeX * dy);

        if (transformY > 0) {
            int screenWidth = arena.getWidth();
            double spriteScreenX =
                (screenWidth / 2) * (1 + transformX / transformY);

            shape.setXPosition(spriteScreenX);
        }

        shape.setYPosition(arena.getHeight() / 2);

        depth = distance;
        shape.setDepth(distance);
    }
}
