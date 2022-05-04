package Algorithm.KMP;

public class KMP {
    public static void main(String[] args) {
        String s1 = "ABCDABCX";
        String s2 = "DAB";
        KMP kmp = new KMP();
        System.out.println(kmp.kmp(s1, s2, kmp.kmpNext(s1)));

    }

    public int kmp(String s1, String s2, int[] next){
        int s1l = s1.length();
        int s2l = s2.length();

        for (int i = 0, j = 0;i <s1l; i++) {
            // 如果没有匹配到，就根据kmp原理移动j
            while (j > 0 && s2.charAt(j) != s1.charAt(i)) j = next[j -1];
            // 如果匹配到了就移到下一位继续
            if(s2.charAt(j) == s1.charAt(i)) j++;
            // 完全匹配完了就返回
            if(j == s2l) return i - j + 1;
        }
        return -1;
    }

    public int[] kmpNext(String dest){
        int[] next = new int[dest.length()];
        next[0] = 0;
        for (int i = 1, j = 0; i < dest.length(); i++) {
            while (j > 0 && dest.charAt(i) != dest.charAt(j)) {
                j = next[j-1];
            }
            if(dest.charAt(i) == dest.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
        return next;
    }
}
