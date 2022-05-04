package Question;

import java.util.*;

public class test1 {
    //    abcd -----> dcbabcd
//    dcba
//    dcba#abcd
//    abbadc ---> cd abbadc
//    cdabba#abbadc
//        #cdabba
//    acecaa ---> aacecaa
//    aaceca#acecaa
//         #aaceca
    public static void main(String[] args) {
        test1 t = new test1();
        // System.out.println(Arrays.toString(t.countBits(2)));
        // int[] n = new int[]{1,1,2};
        // System.out.println(t.removeDuplicates(n));
        // ListNode l0 = new ListNode(3);
        // ListNode l1 = new ListNode(2);
        // ListNode l2 = new ListNode(0);
        // ListNode l3 = new ListNode(-4);

        // l0.next = l1;
        // l1.next = l2;
        // l2.next = l3;
        // l3.next = l1;
        // System.out.println(t.detectCycle(l0));
        // int[][] ma = new int[][]{{1,2,3},{4,5,6},{7,8,9}};
        // t.show(ma);
        // System.out.println(t.spiralOrder(ma));
        // t.show(t.turn90(ma));
        // System.out.println(t.rotatedDigits(857));
        int a = 9;
        System.out.println(a >> 1);

    }

    public int rotatedDigits(int n) {
        int[] badNum = new int[]{3, 4, 7};
        int count = 0;

        for (int i = 1; i <= n; i++) {
            boolean isGood = true;
            String cache = String.valueOf(i);

            for (int i1 : badNum) {
                if(cache.contains(String.valueOf(i1))) {
                    isGood = false;
                    break;
                }
            }

            if(isGood){
                char[] chars = cache.toCharArray();
                char[] res = new char[chars.length];
                for (int i1 = 0; i1 < chars.length; i1++) {
                    switch (chars[i1]){
                        case '2' -> res[i1] = '5';
                        case '5' -> res[i1] = '2';
                        case '6' -> res[i1] = '9';
                        case '9' -> res[i1] = '6';
                        default -> res[i1] = chars[i1];
                    }
                }
                if(Integer.parseInt(String.valueOf(res)) != i) count++;
            }
        }
        return count;
    }

    public void show(int[][] a){
        for (int[] ints : a) {
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }
            System.out.println();
        }
    }

    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        while (matrix[0].length != 0) {
            for (int i : matrix[0]) {
                res.add(i);
            }

            int row = matrix.length;
            int col = matrix[0].length;
            int[][] cache = new int[col][row - 1];
            int x = 0;

            for (int i = col - 1; i >= 0; i--) {
                int y = 0;
                for (int j = 1; j < row; j++) {
                    cache[x][y] = matrix[j][i];
                    y++;
                }
                x++;
            }
            matrix = cache;

        }
        return res;
    }

    // public ListNode detectCycle(ListNode head) {
    //     if(head == null) return null;
    //     ListNode res = null;
    //     Map<Integer, ListNode> map = new HashMap<>();
    //
    //     while (head.next != null) {
    //         ListNode listNode = map.get(head.hashCode());
    //         if(listNode == null) {
    //             map.put(head.hashCode(), head);
    //         } else {
    //             res = listNode;
    //             break;
    //         }
    //         head = head.next;
    //     }
    //     return res;
    // }

    public int removeDuplicates(int[] nums) {
        int index = 0;
        for (int i = 1; i < nums.length; i++) {
            if(nums[index] != nums[i]) {
                nums[++index] = nums[i];
            }
        }
        return index + 1;
    }

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        res.add(new ArrayList<>());
        for (int num : nums) {
            int all = res.size();
            for (int i1 = 0; i1 < all; i1++) {
                List<Integer> tp = new ArrayList<>(res.get(i1));
                tp.add(num);
                res.add(tp);
            }
        }
        return res;
    }

    public int[] countBits(int n) {
        n++;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int count = 0;
            char[] s = Integer.toBinaryString(i).toCharArray();
            for (char c : s) if (c == '1') count++;
            res[i] = count;
        }
        return res;
    }

}

// class ListNode {
//     int val;
//     ListNode next;
//     ListNode(int x) {
//         val = x;
//         next = null;
//     }
//
//     @Override
//     public String toString() {
//         return "ListNode{" +
//                 "val=" + val +
//                 '}';
//     }
// }
