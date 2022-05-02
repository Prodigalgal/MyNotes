package Algorithm.Floyd;

public class Floyd {
    public static void main(String[] args) {
        int[][] e = new int[10][10];
    }


    public static void floyd(int[][] e, int n) {
        for (int k = 1; k <= n; k++)
            for (int i = 1; i <= n; i++)
                for (int j = 1; j <= n; j++)
                    if (e[i][j] > e[i][k] + e[k][j])
                        e[i][j] = e[i][k] + e[k][j];
    }
}
