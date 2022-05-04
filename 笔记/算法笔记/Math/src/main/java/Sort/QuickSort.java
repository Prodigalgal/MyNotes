package Sort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;


public class QuickSort {


    static int length = 1000;

    public QuickSort() {
    }

    public static int[] getArray() {
        int[] arr = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length);
        }
        return arr;
    }

    public static void main(String[] args) {
        QuickSort qs = new QuickSort();

        for (int i = 0; i < 10; i++) {
            int[] arr = getArray();
            int[] arr0 = arr.clone();
            int[] arr1 = arr.clone();
            int[] arr2 = arr.clone();

            LocalDateTime time0 = LocalDateTime.now();
            qs.quickSort(arr0, 0, arr0.length - 1);
            LocalDateTime time1 = LocalDateTime.now();

            LocalDateTime time2 = LocalDateTime.now();
            qs.quickSort2(arr1, 0, arr1.length - 1);
            LocalDateTime time3 = LocalDateTime.now();

            LocalDateTime time4 = LocalDateTime.now();
            qs.quickX(arr2);
            LocalDateTime time5 = LocalDateTime.now();


            long duration0 = Duration.between(time0, time1).toNanos();
            long duration1 = Duration.between(time2, time3).toNanos();
            long duration2 = Duration.between(time4, time5).toNanos();
            System.out.println("A用时：" + duration0 +" B用时：" + duration1 +  " C用时：" + duration2 + " 时间差：" + (duration0 - duration1) + " 时间差：" + (duration0 - duration2));
        }

    }

    private void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int partitionIndex = partition(arr, left, right);
            quickSort(arr, left, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, right);
        }
    }

    private int partition(int[] arr, int left, int right) {
        int index = left + 1;
        for (int i = index; i <= right; i++) {
            if (arr[i] < arr[left]) {
                swap(arr, i, index);
                index++;
            }
        }
        swap(arr, left, index - 1);
        return index - 1;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public void quickSort2(int[] a, int left, int right) {
        // 获取中间下标
        int midValue = a[(left + right) / 2];
        // 移动的左边界
        int l = left;
        // 移动的右边界
        int r = right;

        while (l < r) {
            // 找出两边错误位置的数字
            while (a[l] < midValue) l++;
            while (a[r] > midValue) r--;

            // 如果边界越过中点
            if (l >= r) {
                break;
            }

            swap(a, l, r);

            // 交换完发现相等，进行移动
            if (a[l] == midValue) r--;
            if (a[r] == midValue) l++;
        }

        if (l == r) {
            l++;
            r--;
        }

        if (l < right) quickSort(a, l, right);
        if (r > left) quickSort(a, left, r);

    }

    public void quickX(int[] a) {
        int max = a[0], index = 0, N = a.length;
        for (int i = 0; i < N; i++)
            if (a[i] > max) { max = a[i]; index = i; }
        swap(a, N - 1, index);
        quick(a, 0, a.length - 1);
    }

    private void quick(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int i = lo, j = hi + 1, v = a[lo];
        while (true) {
            while (a[++i] < v);
            while (a[--j] > v);
            if (i >= j) break;
            swap(a, i, j);
        }
        swap(a, lo, j);
        quick(a, lo, j - 1);
        quick(a, j + 1, hi);
    }


}
