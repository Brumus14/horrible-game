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
    private int frameCounter = 0;
    private boolean noiseCooldown = true;
    private Sprite displaySprite;
    public boolean killPlayer = false;
    private int hearingDistance = 5;
    private Rectangle heartbeat;
    private double heartTimer = 1;

    public Enemy(GameArena arena, Generator gen, Player player, Raycaster r,
                 CursorManager c) {
        this.gen = gen;
        this.player = player;
        xPos = gen.startEnemyX;
        yPos = gen.mapSize / 2;
        listLocations = new ArrayList<>();
        listLocations.add(new double[] {xPos + 0.5, yPos + 0.5});
        targetLocation = listLocations.getFirst();
        displaySprite = new Sprite(arena, player, xPos + 0.5, yPos + 1.5, 200,
                                   200, 0x800080, r);

        heartbeat =
            new Rectangle(0, 0, c.getWidth(), c.getHeight(), "%ff000000");
        arena.addRectangle(heartbeat);
    }

    private boolean checkNoise() {
        if (Math.sqrt(Math.pow(player.getPositionX() - xPos, 2) +
                      Math.pow(player.getPositionY() - yPos, 2)) <
            hearingDistance) {
            // targetLocation[0][0] = (int)player.getPositionX();
            // targetLocation[0][1] = (int)player.getPositionY();
            listLocations = new ArrayList<>();
            // visited = new ArrayList<>();

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
        if (curNode != null) {
            listLocations = new ArrayList<>();
            speed = 0.03;
            curNode = curNode.prevNode;
            while (curNode != null) {
                listLocations.add(
                    new double[] {curNode.x + 0.5, curNode.y + 0.5});
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

        for (int i = 0; i < depth; i++) {
            while (depth > 0) {
                toRemove = new ArrayList<>();
                toAdd = new ArrayList<>();
                for (Node node : next) {
                    visited.add(new ArrayList<>(Arrays.asList(node.x, node.y)));
                    if (node.x == (int)xPos && node.y == (int)yPos) {
                        return node;
                    } else {
                        if (node.x + 1 < gen.mapSize - 1 &&
                            !visited.contains(new ArrayList<>(
                                Arrays.asList(node.x + 1, node.y))) &&
                            gen.area[node.y][node.x + 1] == 0) {
                            toAdd.add(new Node(node.x + 1, node.y, node));
                        }
                        if (node.x - 1 > 0 &&
                            !visited.contains(new ArrayList<>(
                                Arrays.asList(node.x - 1, node.y))) &&
                            gen.area[node.y][node.x - 1] == 0) {
                            toAdd.add(new Node(node.x - 1, node.y, node));
                        }
                        if (node.y + 1 < gen.mapSize - 1 &&
                            !visited.contains(new ArrayList<>(
                                Arrays.asList(node.x, node.y + 1))) &&
                            gen.area[node.y + 1][node.x] == 0) {
                            toAdd.add(new Node(node.x, node.y + 1, node));
                        }
                        if (node.y - 1 > 0 &&
                            !visited.contains(new ArrayList<>(
                                Arrays.asList(node.x, node.y - 1))) &&
                            gen.area[node.y - 1][node.x] == 0) {
                            toAdd.add(new Node(node.x, node.y - 1, node));
                        }
                        toRemove.add(node);
                    }
                }
                for (Node node : toRemove) {
                    next.remove(node);
                }
                for (Node node : toAdd) {
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
            if (!listLocations.isEmpty()) {
                listLocations.removeFirst();
            }
            if (!listLocations.isEmpty()) {
                targetLocation = new double[] {listLocations.getFirst()[0],
                                               listLocations.getFirst()[1]};
                // for(double[] location : listLocations){
                // System.out.println("x: " + location[0] + " y: " +
                // location[1]);
                //}
            } else {
                speed = 0.01;
                ArrayList<ArrayList<Double>> visited = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    boolean possibleMove = false;
                    int attempts = 0;
                    while (!possibleMove && attempts < 10) {
                        int moveX = (int)(Math.random() * 2);
                        int movePos = (int)(Math.random() * 2);
                        // System.out.println(moveX + " " + movePos + " " +
                        // (targetLocation[1] + movePos));
                        if (moveX == 1 &&
                            (targetLocation[0] + (movePos == 1 ? 1 : -1)) > 0 &&
                            (targetLocation[0] + (movePos == 1 ? 1 : -1)) <
                                24 &&
                            gen.area[(int)targetLocation[1]]
                                    [(int)targetLocation[0] +
                                     (movePos == 1 ? 1 : -1)] == 0 &&
                            !visited.contains(new ArrayList<>(Arrays.asList(
                                targetLocation[0] + (movePos == 1 ? 1 : -1),
                                targetLocation[1])))) {
                            targetLocation[0] += movePos == 1 ? 1 : -1;
                            possibleMove = true;
                        } else if ((targetLocation[1] +
                                    (movePos == 1 ? 1 : -1)) > 0 &&
                                   (targetLocation[1] +
                                    (movePos == 1 ? 1 : -1)) < 24 &&
                                   gen.area[(int)targetLocation[1] +
                                            (movePos == 1 ? 1 : -1)]
                                           [(int)targetLocation[0]] == 0 &&
                                   !visited.contains(
                                       new ArrayList<>(Arrays.asList(
                                           targetLocation[0],
                                           targetLocation[1] +
                                               (movePos == 1 ? 1 : -1))))) {
                            targetLocation[1] += movePos == 1 ? 1 : -1;
                            possibleMove = true;
                        }
                        attempts++;
                    }
                    if (attempts == 10) {
                        break;
                    } else {
                        // System.out.println(targetLocation[0] + " " +
                        // targetLocation[1]);
                        listLocations.add(new double[] {targetLocation[0],
                                                        targetLocation[1]});
                        // for(double[] location : listLocations){
                        // System.out.println("x: " + location[0] + " y: " +
                        // location[1]);
                        //}
                        visited.add(new ArrayList<>(Arrays.asList(
                            targetLocation[0], targetLocation[1])));
                    }
                }
            }
            if (!listLocations.isEmpty()) {
                targetLocation = new double[] {listLocations.getFirst()[0],
                                               listLocations.getFirst()[1]};
            }

            // for(double[] location : listLocations){
            // System.out.println(Arrays.toString(location));
            //}
        }
    }

    public void isPlayerCollide() {
        if (Math.sqrt(Math.pow(player.getPositionX() - xPos, 2) +
                      Math.pow(player.getPositionY() - yPos, 2)) < 0.5) {
            killPlayer = true;
        } else {
            killPlayer = false;
        }
    }

    public void Update() {
        isPlayerCollide();
        if (frameCounter == 20) {
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

        if (player.crouched && !player.moving) {
            hearingDistance = 0;
        } else if (player.crouched) {
            hearingDistance = 2;
        } else if (player.sprinting && player.moving) {
            hearingDistance = 10;
        } else if (player.moving) {
            hearingDistance = 7;
        } else {
            hearingDistance = 1;
        }

        heartTimer /=
            1 +
            (1 / Math.max(1, ((Math.sqrt(
                                 Math.pow(player.getPositionX() - xPos, 2) +
                                 Math.pow(player.getPositionY() - yPos, 2))))));

        if (heartTimer < 0.01) {
            heartTimer = 1;
        }

        //        System.out.println("ff0000" + Integer.toHexString((int)(((30 -
        //        Math.sqrt(Math.pow(positionX - (gen.endLocation[0] + 0.5), 2)
        //        +
        //                Math.pow(positionY - (gen.endLocation[1] + 0.5), 2)))
        //                / 100) * Integer.valueOf("000000ff", 16))));
        //        System.out.println((((30 - Math.sqrt(Math.pow(positionX -
        //        (gen.endLocation[0] + 0.5), 2) +
        //                Math.pow(positionY - (gen.endLocation[1] + 0.5), 2)))
        //                / 100)));
        String suffix = Integer.toHexString(
            (int)(Integer.valueOf("000000ff", 16) * heartTimer / 20));
        if (suffix.length() == 1) {
            suffix = "0" + suffix;
        }
        heartbeat.setColour("%ff0000" + suffix);
        //System.out.println(heartTimer + " " + suffix);
    }
}
