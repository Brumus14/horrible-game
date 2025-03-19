import java.util.ArrayList;
import java.util.Arrays;

public class Enemy {
    private double xPos;
    private double yPos;
    private Generator gen;
    private Player player;
    private ArrayList<double[]> listLocations;
    private ArrayList<ArrayList<Integer>> visited;
    private double[] targetLocation;
    private boolean moving;
    private double speed = 0.01;
    int frameCounter = 0;
    boolean noiseCooldown = true;

    public Enemy(Generator gen, Player player) {
        this.gen = gen;
        this.player = player;
        //xPos = (int)(Math.random() * (gen.mapSize - 2)) + 1;
        //yPos = gen.mapSize - 2;
        xPos = 12;
        yPos = 1;
        listLocations = new ArrayList<>();
        listLocations.add(new double[]{12, 1});
        targetLocation = listLocations.getFirst();
    }

    private boolean checkNoise(){
        if(player.makingNoise && Math.sqrt(Math.pow(player.getPositionX() - xPos, 2) + Math.pow(player.getPositionY() - yPos, 2)) < 5){
            //targetLocation[0][0] = (int)player.getPositionX();
            //targetLocation[0][1] = (int)player.getPositionY();
            listLocations = new ArrayList<>(Arrays.asList(listLocations.getFirst()));
            visited = new ArrayList<>();
            search(10, (int)player.getPositionX(), (int)player.getPositionY());
            return true;
        }
        return false;
    }

    public int search(int depth, int x, int y){
        System.out.println(x + " " + y);
        visited.add(new ArrayList<Integer>(Arrays.asList(x,y)));
        if(x == (int)xPos && y == (int)yPos){
            listLocations.removeFirst();
            return 1;
        }
        if(depth > 0){
            if(x == 0 || y == 0 || x == gen.mapSize - 1 || y == gen.mapSize - 1 || (Math.abs((int)xPos - x) + Math.abs((int)yPos - y)) > depth){
                return 0;
            }
            if(!(visited.contains(new ArrayList<Integer>(Arrays.asList(x + 1,y)))) && gen.area[y][x + 1] == 0){
                if(search(depth - 1, x + 1, y) == 1){
                    listLocations.add(new double[]{x + 0.5, y + 0.5});
                    return 1;
                }
            }
            if(!(visited.contains(new ArrayList<Integer>(Arrays.asList(x - 1,y)))) && gen.area[y][x - 1] == 0) {
                if (search(depth - 1, x - 1, y) == 1) {
                    listLocations.add(new double[]{x + 0.5, y + 0.5});
                    return 1;
                }
            }

            if(!(visited.contains(new ArrayList<Integer>(Arrays.asList(x,y + 1)))) && gen.area[y + 1][x] == 0) {
                if (search(depth - 1, x, y + 1) == 1) {
                    listLocations.add(new double[]{x + 0.5, y + 0.5});
                    return 1;
                }
            }

            if(!(visited.contains(new ArrayList<Integer>(Arrays.asList(x,y - 1)))) && gen.area[y - 1][x] == 0) {
                if (search(depth - 1, x, y - 1) == 1) {
                    listLocations.add(new double[]{x + 0.5, y + 0.5});
                    return 1;
                }
            }
        }
        return 0;
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
            listLocations.remove(targetLocation);
            if(!listLocations.isEmpty()){
                targetLocation = listLocations.getFirst();
            }
            else{
                /*int moveX = (int)(Math.random() * 2);
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
                }*/
                listLocations.add(targetLocation);
            }
        }
    }

    public void Update(){
        if(frameCounter == 120){
            if(checkNoise()){
                noiseCooldown = true;
                frameCounter = 0;
            }
            else{
                noiseCooldown = false;
            }
            System.out.println("Did a check noise: " + listLocations.size() + "PlayerPos: " +
                    (int)player.getPositionX() + ", " + (int)player.getPositionY() + "EnemyPos: " + (int)xPos + ", " + (int)yPos);
        }
        move();
        if(noiseCooldown){
            frameCounter++;
        }
    }
}
