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
    private double speed = 0.01;
    int frameCounter = 0;
    boolean noiseCooldown = true;
    Sprite displaySprite;

    public Enemy(GameArena arena, Generator gen, Player player, Raycaster r) {
        this.gen = gen;
        this.player = player;
        xPos = (int)(Math.random() * (gen.mapSize - 2)) + 1;
        yPos = gen.mapSize - 2;
        xPos = 12;
        yPos = 1;
        listLocations = new ArrayList<>();
        listLocations.add(new double[] {12.5, 1.5});
        targetLocation = listLocations.getFirst();
        displaySprite = new Sprite(arena, player, xPos + 0.5, yPos + 1.5, 200,
                                   200, "purple", r);
    }

    private boolean checkNoise() {
        if (player.makingNoise &&
            Math.sqrt(Math.pow(player.getPositionX() - xPos, 2) +
                      Math.pow(player.getPositionY() - yPos, 2)) < 10) {
            // targetLocation[0][0] = (int)player.getPositionX();
            // targetLocation[0][1] = (int)player.getPositionY();
            listLocations =
                new ArrayList<>(Arrays.asList(listLocations.getFirst()));
            //visited = new ArrayList<>();

            search(20, (int)player.getPositionX(), (int)player.getPositionY());
            return true;
        }
        return false;
    }

    /*public int search(int depth, int x, int y){
        System.out.println(x + " " + y);
        visited.add(new ArrayList<Integer>(Arrays.asList(x, y)));
        if (x == (int)xPos && y == (int)yPos) {
            listLocations.removeFirst();
            return 1;
        }
        if (depth > 0) {
            if (x == 0 || y == 0 || x == gen.mapSize - 1 ||
                y == gen.mapSize - 1 ||
                (Math.abs((int)xPos - x) + Math.abs((int)yPos - y)) > depth) {
                return 0;
            }
            if (!(visited.contains(
                    new ArrayList<Integer>(Arrays.asList(x + 1, y)))) &&
                gen.area[y][x + 1] == 0) {
                if (search(depth - 1, x + 1, y) == 1) {
                    listLocations.add(new double[] {x + 0.5, y + 0.5});
                    return 1;
                }
            }
            if (!(visited.contains(
                    new ArrayList<Integer>(Arrays.asList(x - 1, y)))) &&
                gen.area[y][x - 1] == 0) {
                if (search(depth - 1, x - 1, y) == 1) {
                    listLocations.add(new double[] {x + 0.5, y + 0.5});
                    return 1;
                }
            }

            if (!(visited.contains(
                    new ArrayList<Integer>(Arrays.asList(x, y + 1)))) &&
                gen.area[y + 1][x] == 0) {
                if (search(depth - 1, x, y + 1) == 1) {
                    listLocations.add(new double[] {x + 0.5, y + 0.5});
                    return 1;
                }
            }

            if (!(visited.contains(
                    new ArrayList<Integer>(Arrays.asList(x, y - 1)))) &&
                gen.area[y - 1][x] == 0) {
                if (search(depth - 1, x, y - 1) == 1) {
                    listLocations.add(new double[] {x + 0.5, y + 0.5});
                    return 1;
                }
            }
        }
        return 0;
    }*/

    public void search(int depth, int x, int y) {
        Node curNode = getPathNode(depth, x, y);
        if(curNode != null) {
            listLocations = new ArrayList<>();
            speed = 0.04;
            curNode = curNode.prevNode;
            while(curNode != null){
                listLocations.add(new double[] {curNode.x + 0.5, curNode.y + 0.5});
                curNode = curNode.prevNode;
            }
        }
    }

    public Node getPathNode(int depth, int x, int y) {
        ArrayList<Node> next = new ArrayList<>();
        ArrayList<ArrayList<Integer>> visited = new ArrayList<>();
        next.add(new Node(x, y, null));

        ArrayList<Node> toRemove;
        ArrayList<Node> toAdd;

        for(int i = 0; i < depth; i++){
            while(depth > 0){
                toRemove = new ArrayList<>();
                toAdd = new ArrayList<>();
                for(Node node : next){
                    visited.add(new ArrayList<>(Arrays.asList(node.x, node.y)));
                    if(node.x == (int)xPos && node.y == (int)yPos){
                        return node;
                    }
                    else{
                        if(node.x + 1 < gen.mapSize - 1 && !visited.contains(new ArrayList<>(Arrays.asList(node.x + 1, node.y))) &&
                                gen.area[node.y][node.x + 1] == 0){
                            toAdd.add(new Node(node.x + 1, node.y, node));
                        }
                        if(node.x - 1 > 0 && !visited.contains(new ArrayList<>(Arrays.asList(node.x - 1, node.y))) &&
                                gen.area[node.y][node.x - 1] == 0){
                            toAdd.add(new Node(node.x - 1, node.y, node));
                        }
                        if(node.y + 1 < gen.mapSize - 1 && !visited.contains(new ArrayList<>(Arrays.asList(node.x, node.y + 1))) &&
                                gen.area[node.y + 1][node.x] == 0){
                            toAdd.add(new Node(node.x, node.y + 1, node));
                        }
                        if(node.y - 1 > 0 && !visited.contains(new ArrayList<>(Arrays.asList(node.x, node.y - 1))) &&
                                gen.area[node.y - 1][node.x] == 0){
                            toAdd.add(new Node(node.x, node.y - 1, node));
                        }
                        toRemove.add(node);
                    }
                }
                for(Node node : toRemove){
                    next.remove(node);
                }
                for(Node node : toAdd){
                    next.add(node);
                }
                depth--;
            }
        }
        return null;
    }

    private void move() {
        if (!(Math.abs(xPos - targetLocation[0]) +
                  Math.abs(yPos - targetLocation[1]) <
              0.05)) {
            if (!(Math.abs(xPos - targetLocation[0]) < 0.025)) {
                xPos += (((targetLocation[0]) - xPos) /
                         (Math.abs(targetLocation[0] - xPos))) *
                        speed;
            }
            if (!(Math.abs(yPos - targetLocation[1]) < 0.025)) {
                yPos += (((targetLocation[1]) - yPos) /
                         (Math.abs(targetLocation[1] - yPos))) *
                        speed;
            }
        } else {
            listLocations.remove(0);
            if (!listLocations.isEmpty()) {
                targetLocation = new double[] {listLocations.getFirst()[0], listLocations.getFirst()[1]};
                //for(double[] location : listLocations){
                    //System.out.println("x: " + location[0] + " y: " + location[1]);
                //}
            } else {
                speed = 0.01;
                ArrayList<ArrayList<Double>> visited = new ArrayList<>();
                for(int i = 0; i < 20; i++){
                    boolean possibleMove = false;
                    int attempts = 0;
                    while (!possibleMove && attempts < 10) {
                        int moveX = (int)(Math.random() * 2);
                        int movePos = (int)(Math.random() * 2);
                        // System.out.println(moveX + " " + movePos + " " +
                        // (targetLocation[1] + movePos));
                        if (moveX == 1 &&
                                (targetLocation[0] + (movePos == 1 ? 1 : -1)) > 0 &&
                                (targetLocation[0] + (movePos == 1 ? 1 : -1)) < 24 &&
                                gen.area[(int)targetLocation[1]]
                                        [(int)targetLocation[0] +
                                        (movePos == 1 ? 1 : -1)] == 0 &&
                                !visited.contains(new ArrayList<>(Arrays.asList(targetLocation[0] +
                                        (movePos == 1 ? 1 : -1), targetLocation[1])))){
                            targetLocation[0] += movePos == 1 ? 1 : -1;
                            possibleMove = true;
                        } else if ((targetLocation[1] + (movePos == 1 ? 1 : -1)) >
                                0 &&
                                (targetLocation[1] + (movePos == 1 ? 1 : -1)) <
                                        24 &&
                                gen.area[(int)targetLocation[1] +
                                        (movePos == 1 ? 1 : -1)]
                                        [(int)targetLocation[0]] == 0  &&
                                !visited.contains(new ArrayList<>(Arrays.asList(targetLocation[0], targetLocation[1] +
                                        (movePos == 1 ? 1 : -1))))) {
                            targetLocation[1] += movePos == 1 ? 1 : -1;
                            possibleMove = true;
                        }
                        attempts++;
                    }
                    if(attempts == 10){
                        break;
                    }
                    else{
                       //System.out.println(targetLocation[0] + " " + targetLocation[1]);
                        listLocations.add(listLocations.size(), new double[] {targetLocation[0], targetLocation[1]});
                        //for(double[] location : listLocations){
                            //System.out.println("x: " + location[0] + " y: " + location[1]);
                        //}
                        visited.add(new ArrayList<>(Arrays.asList(targetLocation[0], targetLocation[1])));
                    }
                }
            }
            targetLocation = new double[] {listLocations.getFirst()[0], listLocations.getFirst()[1]};
            for(double[] location : listLocations){
                //System.out.println(Arrays.toString(location));
            }
        }
    }

    public void Update() {
        if (frameCounter == 120) {
            if (checkNoise()) {
                noiseCooldown = true;
                frameCounter = 0;
            } else {
                noiseCooldown = false;
            }

            // System.out.println("Did a check noise: " + listLocations.size() +
            // "PlayerPos: " + (int)player.getPositionX() + ", " +
            //(int)player.getPositionY() + "EnemyPos: " + (int)xPos + ", " +
            //(int)yPos);
        }
        // System.out.println("xPos: " + xPos + " yPos: " + yPos);
        move();
        displaySprite.setPositionX(xPos);
        displaySprite.setPositionY(yPos);
        displaySprite.Update();
        if (noiseCooldown) {
            frameCounter++;
        }
    }
}
