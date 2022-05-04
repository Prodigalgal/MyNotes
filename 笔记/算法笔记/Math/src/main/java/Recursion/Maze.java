package Recursion;

public class Maze {

    public int[][] map = new int[10][10];

    public static void main(String[] args) {
        Maze maze = new Maze();
        System.out.println("###########初始化迷宫###########");
        maze.initializeMap().show();
        maze.getWay(maze.map, 1, 1);
        System.out.println("###########结果###########");
        maze.show();
    }

    public void show() {
        for (int[] ints : map) {
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }
            System.out.println();
        }
    }

    public Maze initializeMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (i == 0 || j == 0 || j + 1 == map[i].length || i + 1 == map.length) {
                    map[i][j] = 1;
                }
            }
        }
        int x = 1;
        int y = 5;
        for (int i = 0; i < 3; i++) {
            map[x+i][y] = 1;
        }
        x = 3;
        y = 7;
        for (int i = 0; i < 2; i++) {
            map[x][y+i] = 1;
        }

        return this;
    }

    public boolean getWay(int[][] map, int i, int j) {
        if (map[8][8] == 2) {
            return true;
        } else if(map[i][j] == 0) {
            map[i][j] = 2;
            if (getWay(map, i - 1, j)) {
                return true;
            } else if (getWay(map, i, j + 1)) {
                return true;
            } else if (getWay(map, i + 1, j)) {
                return true;
            } else if (getWay(map, i, j - 1)) {
                return true;
            } else {
                map[i][j] = 3;
                return false;
            }
        } else {
            return false;
        }
    }

}
