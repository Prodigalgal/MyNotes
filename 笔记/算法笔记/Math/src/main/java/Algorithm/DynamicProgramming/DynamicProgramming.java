package Algorithm.DynamicProgramming;

import java.util.Arrays;

public class DynamicProgramming {
    // 物品重量
    public int[] weight;
    // 物品价值
    public int[] value;
    // 价格表
    public int[][] price;
    // 背包空间
    public int space;
    // 物品名字
    public String[] names;
    // 添加路径
    public String[][] path;

    // 首行去除，背包空间为0时
    public DynamicProgramming() {
        weight = new int[]{0, 1, 4, 3};
        names = new String[]{"", "吉他", "音响", "电脑"};
        value = new int[]{0, 1500, 3000, 2000};
        space = 4;
        path = new String[space + 1][weight.length + 1];
        price = new int[space + 1][weight.length + 1];
        Arrays.fill(price[0], 0);
        Arrays.fill(path[0], " ");
        for (int[] ints : price) {
            ints[0] = 0;
        }
        for (String[] ss : path) {
            ss[0] = " ";
        }
    }

    public static void main(String[] args) {
        DynamicProgramming dp = new DynamicProgramming();
        dp.dynamic(dp.weight, dp.value, dp.price, dp.space);
        for (int i = 0; i < dp.price.length - 1; i++) {
            for (int j = 0; j < dp.price[i].length; j++) {
                System.out.printf("%d\t", dp.price[i][j]);
            }
            System.out.println();
        }

        for (int i = 0; i < dp.path.length - 1; i++) {
            for (int j = 0; j < dp.path[i].length; j++) {
                System.out.printf("%s\t", dp.path[i][j]);
            }
            System.out.println();
        }

        int maxX = 0;
        int maxY = 0;
        int max = 0;
        for (int i = 0; i < dp.price.length; i++) {
            for (int j = 0; j < dp.price[i].length; j++) {
                if (max < dp.price[i][j]) {
                    maxX = i;
                    maxY = j;
                    max = dp.price[i][j];
                }
            }
        }
        System.out.println("最佳情况：" + dp.path[maxX][maxY]);

    }

    public void dynamic(int[] weight, int[] value, int[][] price, int space) {
        // i 代表了物品
        for (int i = 1; i < weight.length; i++) {
            // j 代表了背包容量
            for (int j = 1; j <= space; j++) {
                // 物品重量大于背包容量
                if (weight[i] > j) {
                    price[i][j] = price[i - 1][j];
                    path[i][j] = path[i - 1][j];
                    // 物品重量小于等于背包容量
                } else {
                    // 前一格子物品价值
                    int preV = price[i - 1][j];
                    // 当前物品价值+剩余容量可放入物品价值
                    int composeV = value[i] + price[i - 1][j - weight[i]];
                    // int max = Math.max(preV, composeV);
                    if (composeV > preV) {
                        price[i][j] = composeV;
                        path[i][j] = names[i] + path[i - 1][j - weight[i]];
                    } else {
                        price[i][j] = preV;
                        path[i][j] = path[i - 1][j];
                    }
                }
            }
        }
    }
}


