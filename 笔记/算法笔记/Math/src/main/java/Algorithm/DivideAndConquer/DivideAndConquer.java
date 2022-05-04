package Algorithm.DivideAndConquer;

public class DivideAndConquer {
    public static void main(String[] args) {
        hanoiTower(20, "A", "B", "C");
    }

    public static void hanoiTower(int n, String a, String b, String c) {
        // 只有一个盘则直接移动到C
        if(n == 1) System.out.println("第" + n + "个盘" + a + "->" + c);
        else {
            // 先将最底下以外的盘的全部移到B
            hanoiTower(n-1, a, c, b);
            // 将最底下的盘移动到C
            System.out.println("第" + n + "个盘" + a + "->" + c);
            // 在将B盘移动到C
            hanoiTower(n-1, b, a, c);
        }
    }
}
