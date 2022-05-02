package BlueCup;

import java.util.*;

public class S2021No1C {
    public static void main(String[] args) {

        Queue<Node> nodes = new LinkedList<>();
        Set<Line> lines = new HashSet<>();

        for (int i = 0; i <= 19; i++) {
            for (int j = 0; j <= 20; j++) {
                nodes.add(new Node(i, j));
            }
        }

        while (!nodes.isEmpty()) {
            Node poll = nodes.poll();
            nodes.forEach(n -> {
                if (poll.x != n.x) {
                    double k = (poll.y * 1.0 - n.y) / (poll.x - n.x);
                    double b = (n.x * 1.0 * poll.y - poll.x * n.y) / (n.x - poll.x);
                    lines.add(new Line(k, b));
                }
            });
        }
        System.out.println(lines.size() + 20);
    }

    static class Line {
        double k;
        double b;

        public Line(double k, double b) {
            this.k = k;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Line line)) return false;
            return Double.compare(line.k, k) == 0 && Double.compare(line.b, b) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(k, b);
        }

        @Override
        public String toString() {
            return "Line{" +
                    "k=" + k +
                    ", b=" + b +
                    '}';
        }
    }

    static class Node {
        int x;
        int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
