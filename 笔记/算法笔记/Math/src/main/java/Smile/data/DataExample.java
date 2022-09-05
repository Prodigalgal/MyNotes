package Smile.data;

import org.apache.commons.csv.CSVFormat;
import smile.io.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class DataExample {
    public static void main(String[] args) throws IOException, URISyntaxException {
        var format = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader();
        var zip = Read.csv("src/main/java/Smile/data/winequality-red.csv", format);
        System.out.println(zip);
    }
}
