package BlueCup;

import java.util.Arrays;

public class S2021No1B {
    public static void main(String[] args) {
        int[] cards = new int[10];
        Arrays.fill(cards, 2021);
        int num = 0;
        boolean flag = true;
        while (flag) {
            num++;
            for (char c : String.valueOf(num).toCharArray()) {
                if (cards[c - '0'] > 0) {
                    cards[c - '0']--;
                } else {
                    flag = false;
                    num--;
                }
            }
        }
        System.out.println(num);
    }
}
