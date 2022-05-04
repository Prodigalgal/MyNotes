package Searach;

import java.util.Arrays;
import java.util.Random;

public class InsertSearch {
    public int[] getArray() {
        int length = 10;
        int[] a = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            a[i] = random.nextInt(length*100);
        }
        Arrays.sort(a);
        return a;
    }

    public int search(int[] a, int left, int right, int tp) {
        if(left > right) return -1;

        int mid = left + (right - left) * (tp - a[left]) / (a[right] - a[left]);

        if(a[mid] == tp) return mid;

        return tp > a[mid] ? search(a, mid + 1, right, tp) : search(a, left, mid - 1, tp);
    }

    public static void main(String[] args) {
        InsertSearch is = new InsertSearch();
        int[] a = is.getArray();
        System.out.println(Arrays.toString(a));
        int tp = a[0];
        System.out.println(is.search(a, 0, a.length - 1, tp));
    }

}
