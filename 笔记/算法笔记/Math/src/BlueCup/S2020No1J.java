package BlueCup;

import java.util.*;

public class S2020No1J {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Node> nodes = new ArrayList<>();
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i + 1));
        }
        int m = sc.nextInt();
        for (int i = 0; i < m; i++) {
            int op = sc.nextInt();
            int x = sc.nextInt();
            int y = sc.nextInt();

            if (op == 1 && x != y) {
                Node n1 = nodes.get(x - 1);
                Node n2 = nodes.get(y - 1);
                n1.nodes.add(n2);
                n2.nodes.add(n1);
            }
            if (op == 2) {
                Node first = nodes.get(x - 1);
                first.cache += y;
                fromTo(first, first.nodes, y);
            }
        }

        for (Node node : nodes) {
            System.out.print(node.cache + " ");
        }
    }

    public static void fromTo(Node from, Set<Node> to, int cache) {
        for (Node node : to) {
            node.cache += cache;
            HashSet<Node> cache_nodes = new HashSet<>(node.nodes);
            cache_nodes.remove(from);
            to.forEach(cache_nodes::remove);
            fromTo(node, cache_nodes, cache);
        }
    }

    static class Node {
        int id;
        Set<Node> nodes = new HashSet<>();
        int cache;

        public Node(int id) {
            this.id = id;
        }
    }
}
