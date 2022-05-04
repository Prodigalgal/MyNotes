package Sort;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Random;

public class BubbleSort {

    int length = 10;
    public int[] arr = new int[length];

    public BubbleSort() {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = random.nextInt(length);
        }
    }

    public static void main(String[] args) {
        BubbleSort bs = new BubbleSort();
        bs.sort(bs.arr);
    }

    public void sort(int[] arr) {
        int tp;
        boolean changed = false;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    changed = true;
                    tp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tp;
                }
            }
            if(!changed){
                break;
            } else {
                changed = false;
            }
            show();
        }

    }

    public void show(){
        for (int i : arr) {
            System.out.printf("%d\t", i);
        }
        System.out.println();
    }

}



