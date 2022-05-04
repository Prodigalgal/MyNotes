package BlueCup;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class S2020No1E {
    public static void main(String[] args) throws IOException {
        List<BigInteger> fob = fob();
        BigInteger i_520 = fob.get(519);
        BigInteger i_2020 = fob.get(2019);

        System.out.println(gcd(i_2020, i_520));

    }

    public static List<BigInteger> fob() {
        List<BigInteger> list = new ArrayList<>();
        list.add(BigInteger.ONE);
        list.add(BigInteger.ONE);

        for (int i = 2; i < 2021; i++) {
            list.add(list.get(i - 1).add(list.get(i - 2)));
        }

        return list;
    }

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        return b.compareTo(BigInteger.ZERO) == 0 ? a : gcd(b, a.mod(b));
    }
}
