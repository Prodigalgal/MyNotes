package Question;

import java.util.*;

public class Solution {

    public static void main(String[] args) {
        // int[] x = {0, 1, 2, -1, -4};
        // Solution s = new Solution();
        // System.out.println(s.containsDuplicate(x));
        // System.out.println();
        // Queue<String> queue = new LinkedList<>();
        // queue.offer("A");
        // queue.offer("B");
        // queue.offer("C");
        // System.out.println(queue.poll());
        // System.out.println(queue.poll());
        // System.out.println(queue.poll());

        // int[] a = new int[5];
        // for (int i = 0; i < a.length; i++) {
        //     System.out.println(1);
        // }

        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i <= 9; i++) {
            map.put( i, 2021);
        }

        // 终止标志：true 终止
        boolean flag = false;

        // 上限 20210
        for (int i = 0; i < 20210; i++) {
            String s = String.valueOf(i);
            for (int j = 0; j < s.length(); j++) {
                int n = s.charAt(j) - '0';
                int cnt = map.get(n);
                if(cnt > 0){
                    map.put(n, cnt - 1);
                }else{
                    System.out.println(i);
                    flag = true;
                    break;
                }
            }
            if (flag){
                break;
            }
        }

    }

    public boolean containsDuplicate(int[] nums) {
        return Arrays.stream(nums).distinct().count() < nums.length;
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
