package Question;


import java.util.*;

public class BlueCup {
    public static void main(String[] args) {
        BlueCup bc = new BlueCup();
        int[] s = new int[]{0, 0, 3, 4};
        System.out.println(Arrays.toString(bc.twoSum(s, 0)));
    }

    public int[] twoSum(int[] numbers, int target) {
        int[] result = new int[2];
        int len = numbers.length;
        int left = 0;
        int right = len - 1;
        boolean flag = true;
        while (flag) {
            if (numbers[left] + numbers[right] > target) right--;
            else if (numbers[left] + numbers[right] < target) left++;
            else {
                result[0] = left;
                result[1] = right;
                return result;
            }
            if (left > right) flag = false;
        }
        return result;
    }

}