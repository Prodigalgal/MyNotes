package BlueCup;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class S2021No1I {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int opn = sc.nextInt();

        Integer[] list = new Integer[n];
        for (int i = 0; i < n; i++) {
            list[i] = i + 1;
        }
        for (int i = 0; i < opn; i++) {
            int op = sc.nextInt();
            int start = sc.nextInt();

            if (op == 0) down(list, start);
            if (op == 1) up(list, start, n);
        }

        System.out.println(Arrays.toString(list));
    }

    public static void up(Integer[] list, int start, int n) {
        Arrays.sort(list, start - 1, n);
    }

    public static void down(Integer[] list, int start) {
        Arrays.sort(list, 0, start, (a, b) ->(b - a));
    }

}
