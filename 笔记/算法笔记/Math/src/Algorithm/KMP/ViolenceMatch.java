package Algorithm.KMP;

public class ViolenceMatch {
    public static void main(String[] args) {
        String s1 = "ACBCDEFG";
        String s2 = "CD";
        ViolenceMatch vm = new ViolenceMatch();
        vm.match(s1, s2);
    }

    public void match(String s1, String s2) {
        int s1l = s1.length();
        int s2l = s2.length();

        int i = 0;
        int j = 0;

        while (i < s1l && j < s2l) {
            if(s1.charAt(i) == s2.charAt(j)) {
                i++;
                j++;
            } else {
                i -= (j - 1);
                j = 0;
            }
        }

        System.out.println(i-j);
    }
}
