package Sort;

import java.util.Arrays;
import java.util.Random;

public class HeapSort {
    static int length = 10;
    public static void main(String[] args) {
        int[] a = getArray();
        System.out.println(Arrays.toString(a));
        HeapSort hs = new HeapSort();
        hs.sort(a);
        System.out.println(Arrays.toString(a));
    }

    public void sort(int[] a) {
        // 从底下开始找非叶子节点
        // 构建出大顶堆
        for (int i = a.length / 2 - 1; i >= 0; i--) {
            adjust(a, i, a.length);
        }
        for (int i = a.length - 1; i > 0; i--) {
            // 将最大的元素放到素组末尾
            swap(a, 0, i);
            // 将数组长度缩短后，再进行大顶堆调整
            adjust(a, 0, i);
        }
    }

    public void adjust(int[] a, int index, int length){
        // 保存堆的头部
        int tp = a[index];
        // 遍历头部节点的左子节点
        // 继续左子节点的左子节点
        for (int i = index * 2 + 1; i < length; i = i * 2 + 1) {
            // 获取到左右节点中较大的
            if(i + 1 < length && a[i] < a[i + 1]) {
                i++;
            }
            // 如果子节点大于父节点，交换
            if(a[i] > tp) {
                a[index] = a[i];
                index = i;
            } else {
                break;
            }
        }
        // 将原先的父节点放到被交换的节点位置
        a[index] = tp;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static int[] getArray() {
        int[] arr = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length);
        }
        return arr;
    }
}
