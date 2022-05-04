package BlueCup;

import java.util.ArrayList;
import java.util.List;

public class S2021No1H {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        triangle(list, 2);
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) == 1) {
                System.out.println(i+1);
                break;
            }
        }
    }

    public static void triangle(List<Integer> list, int n){
        int[][] triangle = new int[n][];
        for (int i = 0; i < triangle.length; i++) {
            triangle[i] = new int[i + 1];
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    triangle[i][j] = 1;
                    list.add(triangle[i][j]);
                } else {
                    triangle[i][j] = triangle[i - 1][j] + triangle[i - 1][j - 1];
                    list.add(triangle[i][j]);
                }
            }
        }
    }
}
