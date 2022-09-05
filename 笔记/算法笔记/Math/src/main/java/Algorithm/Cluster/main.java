package Algorithm.Cluster;



import Algorithm.Cluster.Kmeans.K_means;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.ScatterPlot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static Algorithm.Cluster.Kmeans.K_means.*;

public class main {
    public static Table table;

    public static void main(String[] args) {
        // 初始化
        try {
            Integer line = null;
            FileReader fr = new FileReader("src/main/java/Algorithm/Data/test.csv");
            BufferedReader br = new BufferedReader(fr);
            String head = br.readLine();
            String s;
            int index = 0;
            int count = 0;
            while ((s = br.readLine()) != null) {
                String[] split = s.split(",");
                points.add(new K_means.Point(index, Double.parseDouble(split[0]), Double.parseDouble(split[1])));
                count++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DoubleStream x = points.stream().mapToDouble(p -> p.x);
        DoubleStream y = points.stream().mapToDouble(p -> p.y);
        table = Table.create(DoubleColumn.create("X", x), DoubleColumn.create("Y", y));
        // 计算前展示
        Plot.show(ScatterPlot.create("K_means", table, "X", "Y"));

        kmeansCustom();

        // 计算后展示
        IntStream c = points.stream().mapToInt(p -> p.cent_id);
        table.addColumns(IntColumn.create("cluster", c));
        Plot.show(ScatterPlot.create("K_means", table, "X", "Y", "cluster"));
    }

    static int nums = 0;

    public static void kmeansCustom() {
        int initCenter = 1;
        Random r = new Random();
        for (int i = 0; i < initCenter; i++) {
            int index = r.nextInt(points.size());
            K_means.Point point = points.get(index);
            centers.add(new K_means.Center(i, point.x, point.y));
        }

        int iter = 0;
        boolean flag = true;
        while (flag) {
            if (nums == 5) break;
            for (K_means.Point point : points) {
                K_means.Center center = distanceCluster(point, centers);
                point.cent_id = center.id;
            }
            centers.forEach(K_means::newCenter);

            if (!checkCustomCondition(0.1)) {
                int index = r.nextInt(points.size());
                K_means.Point point = points.get(index);
                centers.add(new K_means.Center(centers.size(), point.x, point.y));
            }

            flag = isChang(centers);
            System.out.println("第 " + iter++ + " 次迭代");
            System.out.println("中心点数量 " + centers.size());
            if (centers.size() == 1) break;
            nums++;
        }

    }
}
