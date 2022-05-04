package Searach;

import java.util.Arrays;
import java.util.Random;

public class BinarySearch {
    public static void main(String[] args) {
        BinarySearch bs = new BinarySearch();
        int[] a = bs.getArray();
        System.out.println(Arrays.toString(a));
        int tp = a[6];
        System.out.println(bs.search(a, 0, a.length - 1, tp));
        System.out.println(bs.search(a, tp));
    }

    public int[] getArray() {
        int length = 10;
        int[] a = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            a[i] = random.nextInt(length * 100);
        }
        Arrays.sort(a);
        return a;
    }

    public int search(int[] a, int left, int right, int tp) {
        if (left > right) {
            return -1;
        }
        int mid = (left + right) / 2;

        if (a[mid] == tp) {
            return mid;
        }

        return tp > a[mid] ? search(a, mid + 1, right, tp) : search(a, left, mid - 1, tp);
    }

    public int search(int[] a, int tp) {
        int l = 0;
        int r = a.length - 1;

        while (l < r) {
            int mid = (l + r) / 2;
            if (tp == a[mid]) return mid;
            else if (tp > a[mid]) l = mid + 1;
            else if (tp < a[mid]) r = mid + 1;
        }
        return -1;
    }
}
