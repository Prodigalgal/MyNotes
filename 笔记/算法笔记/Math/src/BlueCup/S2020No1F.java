package BlueCup;

import java.io.IOException;

public class S2020No1F {
    public static void main(String[] args) throws IOException {
        byte[] buff = new byte[100];
        int len = System.in.read(buff), a = 0, b = 0, c = 0;
        for (int i = 0; i < len; i++) {
            if (buff[i] >= 'a' && buff[i] <= 'z') b++;
            else if (buff[i] >= 'A' && buff[i] <= 'Z') a++;
            else if (buff[i] >= '0' && buff[i] <= '9') c++;
        }
        System.out.print(a + "\n" + b + "\n" + c);
    }
}
