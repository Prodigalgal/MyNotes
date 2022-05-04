package BlueCup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class S2020No1D {
    //9090400
    // 8499400
    // 5926800
    // 8547000
    // 4958200
    // 4422600
    // 5751200
    // 4175600
    // 6309600
    // 5865200
    // 6604400
    // 4635000
    // 10663400
    // 8087200
    // 4554000
    public static void main(String[] args) throws IOException {
        List<Integer> list = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader("src/Question/a.txt"));
        String buff;
        while (null != (buff = br.readLine())) {
            list.add(Integer.parseInt(buff));
        }
        int space = sum(list) / 2;
        int[] weight = new int[list.size() + 1];
        weight[0] = 0;
        for (int i = 0; i < list.size(); i++) {
            weight[i + 1] = list.get(i);
        }
        int[] values = new int[list.size() + 1];
        values[0] = 0;
        for (int i = 0; i < list.size(); i++) {
            values[i + 1] = list.get(i);
        }
        int[][] price = new int[list.size() + 1][space + 1];
        Arrays.fill(price[0], 0);
        for (int i = 0; i < price.length; i++) {
            price[i][0] = 0;
        }

        dp(weight, values, price, space);

        int max = max(price);

        System.out.println(space - max);
    }

    public static void dp(int[] w, int[] v, int[][] p, int s) {
        for (int i = 1; i < w.length; i++) {
            for (int j = 1; j <= s; j++) {
                if (w[i] > j) {
                    p[i][j] = p[i - 1][j];
                } else {
                    int preV = p[i - 1][j];
                    int compV = v[i] + p[i - 1][j - w[i]];
                    p[i][j] = Math.max(preV, compV);
                }
            }
        }
    }

    public static int max(int[][] p) {
        int max = 0;
        for (int[] ints : p) {
            for (int anInt : ints) {
                if (max < anInt) max = anInt;
            }
        }
        return max;
    }

    public static int sum(List<Integer> a) {
        return a.stream().reduce(Integer::sum).get();
    }
}