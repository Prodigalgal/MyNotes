package BlueCup;

import java.math.BigInteger;

public class S2020No1G {
    public static void main(String[] args) {
        int n = 1000000;
        BigInteger cache = BigInteger.ZERO;
        for (int i = 0; i < n; i++) {
            cache = cache.add(new BigInteger(String.valueOf(i)).pow(8));
        }
        System.out.println(cache.mod(new BigInteger("123456789")));
    }
}
