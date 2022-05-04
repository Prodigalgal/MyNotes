package Algorithm.Cluster.Kmeans;

import com.ibm.icu.impl.Row;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.ScatterPlot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class K_means {
    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("src/main/java/Algorithm/Data/附件1 弱覆盖栅格数据(筛选).csv");
        BufferedReader br = new BufferedReader(fr);
        String head = br.readLine();
        String s;
        int index = 0;
        while ((s = br.readLine()) != null) {
            String[] split = s.split(",");
            points.add(new Point(index, Double.parseDouble(split[0]), Double.parseDouble(split[1])));
        }
        DoubleStream x = points.stream().mapToDouble(p -> p.x);
        DoubleStream y = points.stream().mapToDouble(p -> p.y);
        Table table = Table.create(DoubleColumn.create("X", x), DoubleColumn.create("Y", y));
        // 计算前展示
        Plot.show(ScatterPlot.create("K_means", table, "X", "Y"));
        // 开始计算
        kmeans(4);
        // 计算后展示
        IntStream c = points.stream().mapToInt(p -> p.cent_id);
        table.addColumns(IntColumn.create("cluster", c));
        Plot.show(ScatterPlot.create("K_means", table, "X", "Y", "cluster"));
    }

    public static List<Center> centers = new ArrayList<>();

    public static List<Point> points = new ArrayList<>();

    public static void kmeans(int centers){
        Random r = new Random();
        for (int i = 0; i < centers; i++) {
            int p = r.nextInt(points.size());
            Point point = points.get(p);
            K_means.centers.add(new Center(i, point.x, point.y));
        }

        boolean flag = true;
        while (flag) {
            points.forEach(p -> {
                Center min = distanceCluster(p, K_means.centers);
                p.cent_id = min.id;
            });
            K_means.centers.forEach(K_means::newCenter);
            flag = isChang(K_means.centers);
        }
    }

    // 计算两个坐标点之间的欧式距离
    public static double distanceL2(Point a, Point b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    // 计算每个坐标点距离哪个簇最近
    public static Center distanceCluster(Point p, List<Center> centers) {
        Center a = centers.get(0);
        for (Center c : centers) {
            if (distanceL2(p, a.point) > distanceL2(p, c.point)) {
                a = c;
            }
        }
        return a;
    }

    // 计算簇的新中心点坐标
    public static void newCenter(Center center) {
        double sumx = 0, sumy = 0;
        int i = 0;

        for (Point point : points) {
            if(point.cent_id == center.id) {
                sumx += point.x;
                sumy += point.y;
                i++;
            }
        }

        if (center.point.x != sumx / i || center.point.y != sumy / i) {
            center.point.x = sumx / i;
            center.point.y = sumy / i;
            center.changed = true;
        } else if (center.point.x == sumx / i && center.point.y == sumy / i) {
            center.changed = false;
        }
    }

    // 判断所有簇的中心点是否不再发生变化
    public static boolean isChang(List<Center> centers) {
        boolean ex = false;
        for (Center c : centers) {
            if (c.changed) {
                ex = true;
                break;
            }
        }
        return ex;
    }


    static class Point {
        int id;
        double x, y;

        int cent_id;

        public Point(int id, double x, double y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public Point() {}
    }

    static class Center {
        Point point = new Point();

        int id;

        boolean changed = true;

        public Center(int id, double x, double y) {
            this.id = id;
            point.x = x;
            point.y = y;
        }
    }


}
