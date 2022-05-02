package Searach;

import java.util.Arrays;
import java.util.Random;

public class FibonacciSearch {

    int length = 10;

    public int[] getArray() {
        int[] a = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            a[i] = random.nextInt(length*100);
        }
        Arrays.sort(a);
        return a;
    }

    public int[] getFib(){
        int[] f = new int[length];
        f[0] = 1;
        f[1] = 1;
        for (int i = 2; i < f.length; i++) {
            f[i] = f[i - 1] + f[i - 2];
        }
        return f;
    }

    public int search(int[] a, int left, int right, int key) {
        int low = 0;
        int high = a.length - 1;
        int k = 0;
        int mid;
        int[] f = getFib();
        while(high > f[k] - 1) {
            k++;
        }

        int[] temp = Arrays.copyOf(a, f[k]);
        for(int i = high + 1; i < temp.length; i++) {
            temp[i] = a[high];
        }

        while (low <= high) {
            mid = low + f[k - 1] - 1;
            if(key < temp[mid]) {
                high = mid - 1;
                k--;
            } else if ( key > temp[mid]) {
                low = mid + 1;
                k -= 2;
            } else {
                return Math.min(mid, high);
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        FibonacciSearch fs = new FibonacciSearch();
        int[] a = fs.getArray();
        System.out.println(Arrays.toString(a));
        int tp = a[0];
        System.out.println(fs.search(a, 0, a.length - 1, tp));
    }
}
