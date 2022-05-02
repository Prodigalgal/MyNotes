package lib;

public class test {

    public void quick(int[] ints, int left, int right) {
        if (left < right) {
            int mid = getMid(ints, left, right);
            quick(ints, left, mid - 1);
            quick(ints, mid + 1, right);
        }
    }

    public int getMid(int[] ints, int left, int right) {
        int mid = left + 1;
        for (int i = mid; i <= right; i++) {
            if (ints[i] < ints[left]) {
                change(ints, i, mid);
                mid++;
            }
        }
        change(ints, left, mid - 1);
        return mid - 1;
    }

    public void change(int[] ints, int x, int y) {
        int tp = ints[x];
        ints[x] = ints[y];
        ints[y] = tp;
    }
}
