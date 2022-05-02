package Algorithm.Cluster.Kmeans;

import java.util.ArrayList;

public class K_means {
    public static void main(String[] args) {

    }


    static class Point {
        String name;
        double x, y;

        public Point(String name, double x, double y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public Point() {
        }

        //计算两个坐标点之间的欧式距离
        public static double distance(Point a, Point b) {
            return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
        }

        //计算两个坐标点之间距离的平方
        public static double squaredistance(Point a, Point b) {
            return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
        }

        //计算每个坐标点距离哪个簇最近
        public static int ClusterDistance(Point p, ArrayList<Cluster> array) {
            int a = 0;
            for (int i = 0; i < array.size(); i++) {
                if (Point.distance(p, array.get(a).center) > Point.distance(p, array.get(i).center)) {
                    a = i;
                }
            }
            return a;
        }

        public static void PointAdd(ArrayList<Point> PointArray, ArrayList<double[]> DoubleArray) {
            for (int i = 0; i < DoubleArray.size(); i++) {
                PointArray.add(new Point("p" + (i + 1), DoubleArray.get(i)[0], DoubleArray.get(i)[1]));
            }
        }
    }

    //定义一个类用于表示簇
    static class Cluster {
        Point center = new Point(); // 簇中心点
        ArrayList<Point> Array = new ArrayList<>();    // 簇中的坐标元素
        boolean changed = true; // 用于判断该簇的中心点坐标是否发生变化

        public Cluster(double x, double y) {
            center.x = x;
            center.y = y;
        }

        // 计算簇的新中心点坐标
        public static void NewCenter(Cluster cluster) {
            double sumx = 0, sumy = 0;
            int i = 0;
            for (Point ex : cluster.Array) {
                sumx += ex.x;
                sumy += ex.y;
                i++;
            }
            if (cluster.center.x != sumx / i || cluster.center.y != sumy / i) {
                cluster.center.x = sumx / i;
                cluster.center.y = sumy / i;
                cluster.changed = true;
            } else if (cluster.center.x == sumx / i && cluster.center.y == sumy / i) {
                cluster.changed = false;
            }
        }

        // 判断所有簇的中心点是否不再发生变化
        public static boolean Changing(ArrayList<Cluster> array) {
            boolean ex = false;
            for (Cluster e : array) {
                if (e.changed == true) {
                    ex = true;
                }
            }
            return ex;

        }


    }


}
