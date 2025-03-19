public class Enemy {
    private int xInd;
    private int yInd;
    private double xPos;
    private double yPos;
    private Generator gen;
    private Player player;
    private double[] targetLocation;
    private boolean moving;
    private double speed = 0.01;

    public Enemy(Generator gen, Player player) {
        this.gen = gen;
        this.player = player;
        xInd = (int)(Math.random() * (gen.mapSize - 2)) + 1;
        yInd = gen.mapSize - 2;
        xPos = xInd;
        yPos = yInd;
        targetLocation = new double[2];
        targetLocation[0] = 11;
        targetLocation[1] = 21;
    }

    private void checkNoise(){
        if(player.makingNoise && Math.sqrt(Math.pow(player.getPositionX() - xPos, 2) + Math.pow(player.getPositionY() - yPos, 2)) < 7){
            targetLocation[0] = (int)player.getPositionX();
            targetLocation[1] = (int)player.getPositionY();
        }
    }

    private void move(){
        if(!(Math.abs(xPos - targetLocation[0]) + Math.abs(yPos - targetLocation[1]) < 0.05)){
            if(!(Math.abs(xPos - targetLocation[0]) < 0.025)){
                xPos += (((targetLocation[0]) - xPos) / (Math.abs(targetLocation[0] - xPos))) * speed;
            }
            if(!(Math.abs(yPos - targetLocation[1]) < 0.025)) {
                yPos += (((targetLocation[1]) - yPos) / (Math.abs(targetLocation[1] - yPos))) * speed;
            }
        }
        else{
            int moveX = (int)(Math.random() * 2);
            int movePos = (int)(Math.random() * 2);
            if(moveX == 1){
                if(targetLocation[0] == gen.mapSize - 2){
                    targetLocation[0] += -1;
                }
                else if (targetLocation[0] == 1){
                    targetLocation[0] += 1;
                }
                else{
                    targetLocation[0] += movePos == 1 ? 1 : -1;
                }
            }
            else{
                if(targetLocation[1] == gen.mapSize - 2){
                    targetLocation[1] += -1;
                }
                else if (targetLocation[1] == 1){
                    targetLocation[1] += 1;
                }
                else{
                    targetLocation[1] += movePos == 1 ? 1 : -1;
                }
            }
        }
    }

    public void Update(){
        checkNoise();
        move();
    }
}
