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
        double distance =
            Math.sqrt(Math.pow(player.getPositionX() - positionX, 2) +
                      Math.pow(player.getPositionY() - positionY, 2));
        shape.setWidth(width / distance);
        shape.setHeight(height / distance);

        double x = Math.sin(Math.atan2(positionY - player.getPositionY(),
                                       positionX - player.getPositionX())) *
                   distance;

        shape.setXPosition(x);
        shape.setYPosition(arena.getHeight() / 2);
    }
}
