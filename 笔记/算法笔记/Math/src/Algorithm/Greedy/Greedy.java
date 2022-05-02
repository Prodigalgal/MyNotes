package Algorithm.Greedy;

import java.util.*;

public class Greedy {
    public List<String> areas =new ArrayList<>();
    public List<Node> nodes = new ArrayList<>();

    public static void main(String[] args) {
        Greedy g = new Greedy();

        g.areas.add("北京");
        g.areas.add("上海");
        g.areas.add("天津");
        g.areas.add("广州");
        g.areas.add("深圳");
        g.areas.add("成都");
        g.areas.add("杭州");
        g.areas.add("大连");

        Node k1 = new Node(Arrays.asList("北京","上海","天津"), "k1");
        Node k2 = new Node(Arrays.asList("北京","广州","深圳"), "k2");
        Node k3 = new Node(Arrays.asList("杭州","上海","成都"), "k3");
        Node k4 = new Node(Arrays.asList("上海","天津"), "k4");
        Node k5 = new Node(Arrays.asList("杭州","大连"), "k5");

        g.nodes.add(k1);
        g.nodes.add(k2);
        g.nodes.add(k3);
        g.nodes.add(k4);
        g.nodes.add(k5);

        List<String> a = g.areas;
        List<Node> n = g.nodes;
        List<Node> result = new ArrayList<>();

        while (a.size() != 0) {
            g.initRange(n, a);
            Node fitness = g.getFitness(n);
            g.removeAreas(fitness, a);
            result.add(fitness);
        }
        result.forEach(System.out::println);
    }

    public void initRange(List<Node> nodes, List<String> areas) {
        for (Node n : nodes) {
            n.range = 0;
            for (String a : areas) {
                if(n.areas.contains(a))
                    n.range++;
            }
        }
    }

    public void removeAreas(Node node, List<String> areas) {
        areas.removeAll(node.areas);
    }

    public Node getFitness(List<Node> nodes) {
        Optional<Node> max = nodes.stream().max(Comparator.comparingInt(x -> x.range));
        return max.orElse(null);
    }
}

class Node {
    public int range;
    public List<String> areas;
    public String name;

    public Node(List<String> areas, String name) {
        this.areas = areas;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                '}';
    }
}
