package Sort;

import java.util.Random;

public class MergeSort {

    int length = 5;
    public int[] arr = new int[length];
    public int[] temp = new int[length];

    public MergeSort() {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length);
        }
    }

    public static void main(String[] args) {
        MergeSort ms = new MergeSort();
        ms.show();
        ms.sort(ms.arr, ms.temp, 0, ms.arr.length - 1);
        ms.show();
    }

    public void sort(int[] arr, int[] temp, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            sort(arr, temp, left, mid);
            sort(arr, temp, mid + 1, right);
            merge(arr, temp, left, mid, right);
        }
    }

    public void merge(int[] arr, int[] temp, int left, int mid, int right) {
        int l = left;
        int r = mid + 1;
        int t = 0;

        while (l <= mid && r <= right) {
            if(arr[l] <= arr[r]) {
                temp[t] = arr[l];
                l++;
            } else {
                temp[t] = arr[r];
                r++;
            }
            t++;
        }

        while (l <= mid) {
            temp[t] = arr[l];
            t++;
            l++;
        }
        while (r <= right) {
            temp[t] = arr[r];
            t++;
            r++;
        }

        t = 0;
        l = left;
        while (l <= right) {
            arr[l] = temp[t];
            l++;
            t++;
        }

    }



    public void show() {
        for (int i : arr) {
            System.out.printf("%d\t", i);
        }
        System.out.println();
    }

}
