package BlueCup;

import java.util.ArrayList;
import java.util.List;

public class S2020No1H {
    public static void main(String[] args) {
        String s = "123242526";
        List<String> ca = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            String t1 = s.substring(i,i+1);
            if(i == s.length() - 1) {
                ca.add(t1);
            } else {
                String t2 = s.substring(i, i+2);
                if(Integer.parseInt(t2) > 26) ca.add(t1);
                else {
                    ca.add(t2);
                    i++;
                }
            }
        }
        System.out.println(ca);
    }
}
