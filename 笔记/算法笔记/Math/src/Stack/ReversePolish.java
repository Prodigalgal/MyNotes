package Stack;

import java.util.Stack;

public class ReversePolish {

    public static void main(String[] args) {
        String s = "(3+4)*5-6";
        String s1 = infixToSuffix(s);
        System.out.println("表达式为："+ s);
        System.out.println("后缀表达式为："+s1);
        System.out.println("结果为："+calculating(s1));
    }

    public static int calculating(String question) {
        Stack<Integer> numStack = new Stack<>();
        String[] cache = question.split(" ");
        for (String s : cache) {
            if(s.matches("[0-9]+")){
                numStack.push(Integer.valueOf(s));
            } else if(isOperation(s.toCharArray()[0])) {
                int n1 = numStack.pop();
                int n2 = numStack.peek();
                numStack.push(s.equals("+") ? n2 + n1 : s.equals("-") ? n2 - n1 : s.equals("*") ? n2 * n1 : s.equals("/") ? n2 / n1 : 0);
            }
        }
        return numStack.pop();
    }

    public static String infixToSuffix(String question) {
        Stack<Character> optStack = new Stack<>();
        Stack<String> cacheResult = new Stack<>();
        StringBuilder number = new StringBuilder();

        char[] chars = question.toCharArray();

        for (int i = 0; i < chars.length; i++) {

            char c = chars[i];

            if (isNumber(c)) {
                number.append(c);
                if (i + 1 >= chars.length || !isNumber(chars[i + 1])) {
                    cacheResult.push(number.toString());
                    number = new StringBuilder();
                }
            }

            if (isOperation(c)) {
                while (true) {
                    if (c == ')') {
                        while (optStack.peek() != '(') {
                            cacheResult.push(String.valueOf(optStack.pop()));
                        }
                        optStack.pop();
                        break;
                    }

                    if (optStack.isEmpty() || optStack.peek() == '(') {
                        optStack.push(c);
                        break;
                    }

                    if (comparePriority(c, optStack.peek())) {
                        cacheResult.push(String.valueOf(optStack.pop()));
                    } else {
                        optStack.push(c);
                        break;
                    }
                }
            }
        }

        while (!optStack.isEmpty()) {
            cacheResult.push(String.valueOf(optStack.pop()));
        }

        StringBuilder result = new StringBuilder();
        while (!cacheResult.isEmpty()) {
            result.append(" ").append(cacheResult.pop());
        }

        return result.reverse().toString();
    }

    public static boolean isNumber(char num) {
        return num >= 48 && num <= 57;
    }

    public static boolean isOperation(char opt) {
        return opt == '+' || opt == '-' || opt == '*' || opt == '/' || opt == '(' || opt == ')';
    }

    public static boolean comparePriority(char opt, char peek) {
        return getPriority(opt) <= getPriority(peek);
    }

    public static int getPriority(char opt) {
        return opt == '*' || opt == '/' ? 2 : opt == '+' || opt == '-' ? 1 : 0;
    }


}

