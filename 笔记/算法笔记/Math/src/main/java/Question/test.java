package Question;

import java.util.Arrays;
import java.util.Random;

public class test {
    public static void main(String[] args) {
        test t = new test();
        // 冒泡排序
        System.out.println("###############冒泡排序###############");
        int[] a0 = t.getArrayRandom();
        System.out.println("原始数组=" + Arrays.toString(a0));
        t.bubbleSort(a0);
        System.out.println("排序后数组=" + Arrays.toString(a0));

        // 插入排序
        System.out.println("###############插入排序###############");
        int[] a1 = t.getArrayRandom();
        System.out.println("原始数组=" + Arrays.toString(a1));
        t.insertSort(a1);
        System.out.println("排序后数组=" + Arrays.toString(a1));

        // 选择排序
        System.out.println("###############选择排序###############");
        int[] a2 = t.getArrayRandom();
        System.out.println("原始数组=" + Arrays.toString(a2));
        t.selectSort(a2);
        System.out.println("排序后数组=" + Arrays.toString(a2));

        // 希尔排序
        System.out.println("###############希尔排序###############");
        int[] a3 = t.getArrayRandom();
        System.out.println("原始数组=" + Arrays.toString(a3));
        t.shellSort(a3);
        System.out.println("排序后数组=" + Arrays.toString(a3));

        // 快速排序
        System.out.println("###############快速排序###############");
        int[] a4 = t.getArrayRandom();
        System.out.println("原始数组=" + Arrays.toString(a4));
        t.quickSort(a4, 0, a4.length - 1);
        System.out.println("排序后数组=" + Arrays.toString(a4));

        // 归并排序
        System.out.println("###############归并排序###############");
        int[] a5 = t.getArrayRandom();
        System.out.println("原始数组=" + Arrays.toString(a5));
        int[] temp = new int[a5.length];
        t.mergeSort(a5, temp,0, a5.length - 1);
        System.out.println("排序后数组=" + Arrays.toString(a5));

    }



    public void bubbleSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    swap(a, j, j + 1);
                }
            }
        }
    }

    public void insertSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            // 待插入数值
            int tp = a[i];
            // 预设的最小下标
            int index = i - 1;
            // 只要没到数组头部，并且index指向小于tp，就交换数值，将index前移
            while (index >= 0 && a[index] > tp) {
                a[index + 1] = a[index];
                index--;
            }
            // index 始终保持在tp的前面
            a[index + 1] = tp;
        }
    }

    public void selectSort(int[] a) {
        // 选择排序，每排一次，只交换一次
        for (int i = 0; i < a.length; i++) {
            int tp = a[i];
            int index = i;
            for (int j = i; j < a.length; j++) {
                if(tp > a[j]) {
                    tp = a[j];
                    index = j;
                }
            }
            swap(a, i, index);
        }
    }

    public void shellSort(int[] a) {
        // 设置最后一个分组
        int len = 1;
        // 设置分组数列
        while (len < a.length / 3) len = 3 * len + 1;
        // 开始遍历
        while (len >= 1) {
            // 对每个分组进行插入排序
            for (int i = len; i < a.length; i++) {
                // 待插入数值
                int tp = a[i];
                // 预设的最小下标
                int index = i - len;
                // 只要没到数组头部，并且index指向小于tp，就交换数值，将index前移
                while (index >= 0 && a[index] > tp) {
                    a[index + len] = a[index];
                    index--;
                }
                // index 始终保持在tp的前面
                a[index + len] = tp;
            }
            len /= 3;
        }

    }

    public void quickSort(int[] a, int left, int right) {
        // 获取中间下标值
        int midValue = a[(left + right) / 2];
        // 移动的左边界
        int l = left;
        // 移动的右边界
        int r = right;

        while (l < r) {
            // 找出两边错误位置的数字, 最坏的情况就是找到midValue
            while (a[l] < midValue) l++;
            while (a[r] > midValue) r--;

            // 如果找到了midValue，说明左右都已经符合条件
            if(l >= r) break;

            swap(a, l, r);

            // 交换完发现相等，否则陷入死循环
            if (a[l] == midValue) r--;
            if (a[r] == midValue) l++;
        }

        if(l == r) {
            l++;
            r--;
        }

        if(l < right) quickSort(a, l, right);
        if(r > left) quickSort(a, left, r);

    }

    public void mergeSort(int[] a, int[] temp, int left, int right) {
        if(left < right) {
            int mid = (left + right) / 2;
            mergeSort(a, temp, left, mid);
            mergeSort(a, temp, mid + 1, right);
            merge(a, temp, left, mid, right);
        }
    }

    private void merge(int[] a, int[] temp, int left, int mid, int right) {
        int l = left;
        int r = mid + 1;
        int t = 0;

        while (l <= mid && r <= right) {
            if(a[l] <= a[r]) {
                temp[t] = a[l];
                l++;
            } else {
                temp[t] = a[r];
                r++;
            }
            t++;
        }

        while (l <= mid) {
            temp[t] = a[l];
            t++;
            l++;
        }
        while (r <= right) {
            temp[t] = a[r];
            t++;
            r++;
        }

        t = 0;
        l = left;
        while (l <= right) {
            a[l] = temp[t];
            l++;
            t++;
        }

    }


    public void swap(int[] a, int x, int y) {
        int tp = a[x];
        a[x] = a[y];
        a[y] = tp;
    }

    public int[] getArrayRandom() {
        int length = 10;
        int[] a = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            a[i] = random.nextInt(length);
        }
        return a;
    }

}
