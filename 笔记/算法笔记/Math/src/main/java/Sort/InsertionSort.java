package Sort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;

public class InsertionSort {
    int length = 1000000;
    public int[] arr = new int[length];

    public InsertionSort() {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length);
        }
    }

    public static void main(String[] args) {
        InsertionSort ss = new InsertionSort();
        int[] clone1 = ss.arr.clone();
        int[] clone2 = ss.arr.clone();

        LocalTime t0 = LocalTime.now();
        sort(clone1);
        LocalTime t1 = LocalTime.now();
        System.out.println(Duration.between(t0, t1).getSeconds());
    }

    public static void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int iValue = arr[i];
            int iIndex = i - 1;
            while(iIndex >= 0 && iValue < arr[iIndex]) {
                arr[iIndex + 1] = arr[iIndex];
                iIndex--;
            }
            arr[iIndex + 1] = iValue;
        }
    }

    public void show(int[] arr) {
        for (int i : arr) {
            System.out.printf("%d\t", i);
        }
        System.out.println();
    }

}

