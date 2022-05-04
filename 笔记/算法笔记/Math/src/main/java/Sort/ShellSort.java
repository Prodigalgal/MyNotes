package Sort;

import java.util.Random;

public class ShellSort {
    int length = 10;
    public int[] arr = new int[length];

    public ShellSort() {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length);
        }
    }

    public static void main(String[] args) {
        ShellSort ss = new ShellSort();
        ss.sortByMove(ss.arr);
    }

    public void sortByChange(int[] arr) {
        int len = 1;
        while (len < arr.length / 3) len = 3 * len + 1;
        while (len >= 1) {
            for (int i = len; i < arr.length; i++) {
                for (int j = i; j >= len; j -= len) {
                    if(arr[j] < arr[j - len]) {
                        int tp = arr[j];
                        arr[j] = arr[j - len];
                        arr[j - len] = tp;
                    }
                }
                show();
            }
            len /= 3;
        }

    }

    public void sortByMove2(int[] arr) {
        int len = 1;
        while (len < arr.length / 3) len = 3 * len + 1;
        while (len >= 1) {
            for (int i = len; i < arr.length; i++) {
                int tp = arr[i];
                int j = i;
                if(arr[j] < arr[j - len]) {
                    while (j - len >= 0 && tp < arr[j - len]) {
                        arr[j] = arr[j - len];
                        j -= len;
                    }
                    arr[j] = tp;
                }
                show();
            }
            len /= 3;
        }

    }


    public void sortByMove(int[] arr) {
        int len = 1;
        // 组数
        while (len < arr.length / 3) len = 3 * len + 1;
        while (len >= 1) {
            // 下一组插入排序
            for (int i = len; i < arr.length; i++) {
                int tp = arr[i];
                int j = i - len;
                while (j >= 0 && tp < arr[j]) {
                    arr[j + len] = arr[j];
                    j -= len;
                }
                arr[j + len] = tp;
                show();
            }
            len /= 3;
        }

    }

    public void show() {
        for (int i : arr) {
            System.out.printf("%d\t", i);
        }
        System.out.println();
    }

}
