package Test;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        Test t = new Test();

        var numbers = new int[]{0, 0, 0};

        System.out.println(t.threeSum(numbers));
    }

    public int[][] kClosest(int[][] points, int k) {
        return null;
    }

    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();

        if (nums.length == 3) {
            if (Arrays.stream(nums).sum() == 0) {
                Integer[] ns =Arrays.stream(nums).boxed().toArray(Integer[]::new);
                result.add(List.of(ns));
            }
            return result;
        }

        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            var cache = -nums[i];
            var j = i + 1;
            var k = nums.length - 1;
            while (j < k) {
                if (nums[j] + nums[k] == cache) {
                    ArrayList<Integer> list = new ArrayList<>(){};
                    list.add(nums[i]);
                    list.add(nums[j]);
                    list.add(nums[k]);
                    result.add(list);

                    while (j < k && nums[j] == nums[j + 1]) j++;
                    while (j < k && nums[k] == nums[k - 1]) k--;
                }
                if (nums[j] + nums[k] < cache) j++;
                else k--;
            }
        }
        return result;
    }

    public static int[] twoSum(int[] numbers, int target) {
        int[] result = new int[2];
        for (int i = 0; i < numbers.length; i++) {
            var one = numbers[i];
            var second = target - one;
            int s_i = Arrays.binarySearch(numbers, second);
            if (s_i >= 0) {
                result[0] = i;
                result[1] = s_i;
                return result;
            }
        }
        return null;
    }
}
