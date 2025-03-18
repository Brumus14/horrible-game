public class Sprite {
    GameArena arena;
    Rectangle shape;
    double positionX;
    double positionY;
    double width;
    double height;
    Player player;

    public Sprite(GameArena a, Player p, double x, double y, double w, double h,
                  String colour) {
        arena = a;
        player = p;
        positionX = x;
        positionY = y;
        width = w;
        height = h;
        shape = new Rectangle(0, 0, w, h, colour);
        arena.addRectangle(shape);
    }

    public void Update() {
        double deltaX = player.getPositionX() - positionX;
        double deltaY = player.getPositionY() - positionY;

        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        shape.setWidth(width / distance);
        shape.setHeight(height / distance);

        double angle =
            (deltaX * player.getDirectionX() + deltaY * player.getPositionY()) /
            (Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) *
             Math.sqrt(Math.pow(player.getDirectionX(), 2) +
                       Math.pow(player.getDirectionY(), 2)));

        double x = Math.tan(angle);

        shape.setXPosition(x);
        shape.setYPosition(arena.getHeight() / 2);
    }
}
