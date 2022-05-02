package Stack;

import java.util.Scanner;
import java.util.Stack;

public class Calculator {
    static Stack<Integer> numStake = new Stack<>();
    static Stack<Character> optStake = new Stack<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String question = scanner.nextLine();
        System.out.println(calculation(question));
    }

    public static int calculation(String question) {
        char[] chars = question.toCharArray();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (isNumber(c)) {
                number.append(c);
                // 判断数字长度，如果下一位不是不是数字或者结束了就压入栈
                if (i + 1 >= chars.length || !isNumber(chars[i + 1])) {
                    numStake.push(Integer.parseInt(number.toString()));
                    number = new StringBuilder();
                }
            }
            if (isOperation(c)) {
                // 符号栈不为空时，判断下符号优先级
                while (!optStake.isEmpty() && comparePriority(c)) {
                    // 如果要压入的符号优先级低，则弹出计算
                    numStake.push(Calculating(numStake.pop(), numStake.pop(), optStake.pop()));
                }
                optStake.push(c);
            }
        }
        // 最后将剩下依次弹出计算
        while (!optStake.isEmpty()) {
            numStake.push(Calculating(numStake.pop(), numStake.pop(), optStake.pop()));
        }
        return numStake.peek();
    }

    public static boolean isNumber(char num) {
        return num >= 48 && num <= 57;
    }

    public static boolean isOperation(char opt) {
        return opt == '+' || opt == '-' || opt == '*' || opt == '/';
    }

    public static boolean comparePriority(char opt) {
        // 优先级高返回false，优先级低返回true
        return getPriority(opt) <= getPriority(optStake.peek());
    }

    public static int getPriority(char opt) {
        return opt == '*' || opt == '/' ? 2 : opt == '+' || opt == '-' ? 1 : 0;
    }

    public static int Calculating(int num1, int num2, char opt) {
        return opt == '+' ? num1 + num2 : opt == '-' ? num2 - num1 : opt == '*' ? num1 * num2 : opt == '/' ? num1 + num2 : 0;
    }

}
