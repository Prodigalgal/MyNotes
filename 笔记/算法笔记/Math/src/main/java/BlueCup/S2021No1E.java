package BlueCup;

public class S2021No1E {
    public static void main(String[] args) {
        int x = 2021;
        int[][] f = new int[x + 1][x + 1];

        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= x; j++) {
                if (i == j) f[i][i] = 0;
                else if (Math.abs(i - j) <= 21) f[i][j] = f[j][i] = lcm(i, j);
                else f[i][j] = f[j][i] = Integer.MAX_VALUE;
            }
        }

        floyd(f, x);
        show(f);
        System.out.println(f[1][2021]);
    }

    public static void floyd(int[][] f, int x) {
        for (int k = 1; k <= x; k++)
            for (int i = 1; i <= x; i++)
                for (int j = 1; j <= x; j++)
                    if (f[i][k] == Integer.MAX_VALUE || f[k][j] == Integer.MAX_VALUE) break;
                    else if (f[i][j] > f[i][k] + f[k][j]) f[i][j] = f[i][k] + f[k][j];
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    public static void show(int[][] f) {
        for (int[] ints : f) {
            for (int anInt : ints) System.out.printf("%d\t", anInt);
            System.out.println();
        }
    }
}
