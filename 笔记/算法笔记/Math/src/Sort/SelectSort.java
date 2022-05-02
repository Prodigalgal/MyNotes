package Sort;

import java.util.Random;

public class SelectSort {

    int length = 10;
    public int[] arr = new int[length];

    public SelectSort() {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length);
        }
    }

    public static void main(String[] args) {
        SelectSort ss = new SelectSort();
        ss.sort(ss.arr);
    }

    public void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int tp = arr[i];
            int index = i;
            for (int j = i; j < arr.length; j++) {
                if (tp > arr[j]) {
                    tp = arr[j];
                    index = j;
                }
            }
            arr[index] = arr[i];
            arr[i] = tp;
            show();
        }
    }

    public void show() {
        for (int i : arr) {
            System.out.printf("%d\t", i);
        }
        System.out.println();
    }

}
