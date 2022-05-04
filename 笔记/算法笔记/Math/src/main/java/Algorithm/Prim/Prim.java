package Algorithm.Prim;

import java.util.Arrays;

public class Prim {
    public int nodeNum = 7;
    public int[][] source = new int[nodeNum][nodeNum];
    public boolean[] isVisited = new boolean[nodeNum];
    public int[][] result = new int[nodeNum][nodeNum];
    public String[] nodeName = new String[]{"A", "B", "C", "D", "E", "F", "G"};


    public static void main(String[] args) {
        Prim p = new Prim();
        for (int[] ints : p.source) {
            Arrays.fill(ints, 99);
        }
        for (int[] ints : p.result) {
            Arrays.fill(ints, 0);
        }
        Arrays.fill(p.isVisited, false);

        //    ABCDEFG
        //    0123456
        int[][] s = p.source;

        s[0][1] = s[1][0] = 5;
        s[0][2] = s[2][0] = 7;
        s[1][3] = s[3][1] = 9;
        s[0][6] = s[6][0] = 2;
        s[6][1] = s[1][6] = 3;
        s[2][4] = s[4][2] = 8;
        s[3][5] = s[5][3] = 4;
        s[6][4] = s[4][6] = 4;
        s[6][5] = s[5][6] = 6;
        s[4][5] = s[5][4] = 5;

        p.MST(0);
        p.show(p.result);
    }

    public void MST(int n) {
        // 首个节点设为已访问
        isVisited[n] = true;

        for (int i = 1; i < nodeNum; i++) {
            // 保存最小值以及下标
            int minV = 99;
            int x = 0;
            int y = 0;

            for (int j = 0; j < nodeNum; j++) {
                for (int k = 0; k < nodeNum; k++) {
                    if (source[j][k] < minV && isVisited[j] && !isVisited[k]) {
                        minV = source[j][k];
                        x = j;
                        y = k;
                    }
                }
            }

            isVisited[y] = true;
            result[x][y] = result[y][x] = 1;
        }

    }

    public void show(int[][] ints) {
        System.out.printf("%s\t", " ");
        for (String s : nodeName) {
            System.out.printf("%s\t", s);
        }
        System.out.println();
        int i = 0;
        for (int[] ins : ints) {
            System.out.printf("%s\t", nodeName[i]);
            for (int anInt : ins) {
                System.out.printf("%d\t", anInt);
            }
            i++;
            System.out.println();
        }
    }

}
