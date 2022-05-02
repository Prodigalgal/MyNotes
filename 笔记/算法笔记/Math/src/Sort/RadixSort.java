package Sort;

import java.util.Arrays;
import java.util.Random;

public class RadixSort {
    public static void main(String[] args) {
        RadixSort rs = new RadixSort();
        int[] a = rs.getArrayRandom();
        System.out.println(Arrays.toString(a));
        rs.sort(a);
        System.out.println(Arrays.toString(a));
    }

    public void sort(int[] a){
        int[][] bucket = new int[10][a.length];
        int[] buckets = new int[10];
        int maxLen = getNumLength(a);
        int n = 1;
        for (int i = 0; i < maxLen; i++, n *= 10) {
            for (int tp : a) {
                int indexBucket = tp / n % 10;
                bucket[indexBucket][buckets[indexBucket]] = tp;
                buckets[indexBucket]++;
            }
            int index = 0;
            for (int j = 0; j < buckets.length; j++) {
                if(buckets[j] != 0) {
                    for (int k = 0; k < buckets[j]; k++) {
                        a[index++] = bucket[j][k];
                    }
                    buckets[j] = 0;
                }
            }
        }
    }

    private int getNumLength(int[] a) {
        return (Arrays.stream(a).max().getAsInt() + "").length();
    }
    public int[] getArrayRandom() {
        int length = 10;
        int[] a = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            a[i] = random.nextInt(length*100);
        }
        return a;
    }
}
