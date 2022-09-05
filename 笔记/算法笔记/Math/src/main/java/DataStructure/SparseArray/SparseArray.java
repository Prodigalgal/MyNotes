package DataStructure.SparseArray;

import java.util.Random;
import java.util.Scanner;

public class SparseArray {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入行：");
        int x = scanner.nextInt();
        System.out.print("输入列：");
        int y = scanner.nextInt();

        int[][] chess = new int[x][y];
        Random random = new Random();

        chess[random.nextInt(x)][random.nextInt(y)] = random.nextInt(10);
        chess[random.nextInt(x)][random.nextInt(y)] = random.nextInt(10);

        System.out.println("#########原始数组#########");
        for (int[] ints : chess) {
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }
            System.out.println();
        }

        System.out.println("#########稀疏数组#########");
        int count = 0;
        for (int[] ints : chess) {
            for (int anInt : ints) {
                if (anInt != 0)
                    count++;
            }
        }
        count++;
        int[][] sparse = new int[count][3];
        sparse[0][0] = x;
        sparse[0][1] = y;
        sparse[0][2] = --count;

        int tp = 0;
        for (int i = 0; i < chess.length; i++) {
            for (int j = 0; j < chess[i].length; j++) {
                if (chess[i][j] != 0) {
                    tp++;
                    sparse[tp][0] = i;
                    sparse[tp][1] = j;
                    sparse[tp][2] = chess[i][j];
                }
            }
        }

        for (int[] ints : sparse) {
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }
            System.out.println();
        }

        System.out.println("#########还原数组#########");
        int[][] reInts = new int[sparse[0][0]][sparse[0][1]];

        for (int j = 1; j < sparse.length; j++) {
            reInts[sparse[j][0]][sparse[j][1]] = sparse[j][2];
        }

        for (int[] ints : reInts) {
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }
            System.out.println();
        }


    }
}
