package PopulationData;

import Algorithm.Cluster.Kmeans.K_means;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.DoubleStream;

public class ByAge {
    public static void main(String[] args) {
        FemalePopulationByAge();
    }


    public static void FemalePopulationByAge() {

        try {

            FileReader fr = new FileReader("src/main/java/PopulationData/按年龄人口数分类.csv", "GBK");
            BufferedReader br = new BufferedReader(fr);
            String head = br.readLine();
            String s;
            System.out.println(head);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // DoubleStream x = points.stream().mapToDouble(p -> p.x);
        // DoubleStream y = points.stream().mapToDouble(p -> p.y);
        // table = Table.create(DoubleColumn.create("X", x), DoubleColumn.create("Y", y));

    }
}
