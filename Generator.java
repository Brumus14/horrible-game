public class Generator {
    int mapSize = 24;
    int[][] area = new int[mapSize][mapSize];
    int[] startLocation = new int[2];
    int[] endLocation = new int[2];

    public Generator() {
        startLocation[0] = mapSize / 2;
        startLocation[1] = 1;
    }

    public void load(GameMap map){
        map.SetMap(area);
    }

    public void randomise(){
        for(int i = 0; i < mapSize; i++){
            for(int j = 0; j < mapSize; j++){
                if(i == 0 || i == mapSize - 1 || j == 0 || j == mapSize - 1){
                    area[i][j] = 1;
                }
                else{
                    area[i][j] = (int) (Math.random() * 1) == 0 ? 1 : 0;
                }
            }
        }
    }

    public void createPath(){
        int curX = startLocation[0];
        int nextX;
        area[startLocation[0]][startLocation[1]] = 0;
        area[startLocation[0]][startLocation[1] + 1] = 0;

        for(int i = 2; i < mapSize - 1; i += 2){
            nextX = (int)(Math.random() * (mapSize - 2)) + 1;
            for(int j = curX; j != nextX; j += (nextX - curX) / Math.abs(nextX - curX)){
                area[j][i] = 0;
            }
            area[nextX][i] = 0;
            area[nextX][i + 1] = 0;

            curX = nextX;
        }
        endLocation[0] = curX;
        endLocation[1] = mapSize - 1;
        area[curX][mapSize - 1] = 1;
    }

    public void display(){
        for(int i = 0; i < mapSize; i++){
            for(int j = 0; j < mapSize; j++){
                System.out.print(area[j][i] + " ");
            }
            System.out.println();
        }
    }
}
