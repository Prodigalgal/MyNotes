package Recursion;

public class EightQueen {
    int max = 14;
    int[] queen = new int[max];
    int numResult = 0;

    public static void main(String[] args) {
        EightQueen eq = new EightQueen();
        eq.check(0);
        System.out.println("总的解法为："+eq.numResult);
    }

    public void check(int n) {
        if(n == max) {
            numResult++;
            // show();
        } else {
            for (int i = 0; i < max; i++) {
                queen[n] = i;
                if(isConflict(n)) {
                    check(n+1);
                }
            }
        }
    }

    public boolean isConflict(int n) {
        for (int i = 0; i < n; i++) {
            if(queen[i] == queen[n] || Math.abs(n -i) == Math.abs(queen[n] - queen[i])) {
                return false;
            }
        }
        return true;
    }

    public void show() {
        for (int i : queen) {
            System.out.printf("%d\t", i);
        }
        System.out.println();
    }


}
