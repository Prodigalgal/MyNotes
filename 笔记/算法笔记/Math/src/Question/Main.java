package Question;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("F:\\中转\\TXT\\movies.txt");
        FileWriter fw = new FileWriter("movies.txt");

        BufferedReader br = new BufferedReader(fr);
        String s;
        int count = 0;
        while ((s = br.readLine()) != null) {

            fw.write(s);
            fw.write("\n");
            count++;
            if(count == 100) break;
        }

        fr.close();
        fw.close();
        br.close();
    }
}
