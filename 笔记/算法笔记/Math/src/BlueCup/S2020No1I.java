package BlueCup;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class S2020No1I {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Node> nodes = new ArrayList<>();
        int n = sc.nextInt();
        int need = sc.nextInt();

        for (int i = 0; i < n; i++) {
            int preNo = sc.nextInt();
            int weight = sc.nextInt();
            nodes.add(new Node(preNo, weight));
        }

        nodes.forEach(nd -> {
            if (nd.preNo != 0) {
                Node parent = nodes.get(nd.preNo - 1);
                nd.parent = parent;
                if (nd.weight > parent.weight) parent.right = nd;
                else parent.left = nd;
            }
        });

        Node index = nodes.get(need - 1);
        int firstMin = Integer.MIN_VALUE;
        int firstMax = Integer.MAX_VALUE;
        int count = -1;

        if (index.left == null) {
            firstMax = index.weight;
            Node tp = index;
            while (tp.parent != null) {
                Node up = tp.parent;
                if (up.weight < firstMax) {
                    firstMin = up.weight;
                    break;
                }
                tp = tp.parent;
            }
            if (firstMin != Integer.MIN_VALUE) count += (firstMax - firstMin);
        }

        firstMin = Integer.MIN_VALUE;
        firstMax = Integer.MAX_VALUE;

        if (index.right == null) {
            firstMin = index.weight;
            Node tp = index;
            while (tp.parent != null) {
                Node up = tp.parent;
                if (up.weight > firstMin) {
                    firstMax = up.weight;
                    break;
                }
                tp = tp.parent;
            }
            if (firstMax != Integer.MAX_VALUE) count += (firstMax - firstMin);
        }

        if (index.left != null && index.right != null) System.out.println(0);
        else if (firstMax == Integer.MAX_VALUE || firstMin == Integer.MIN_VALUE) System.out.println(-1);
        else System.out.println(count);
    }

    static class Node {
        int weight;
        int preNo;
        Node left;
        Node right;
        Node parent;

        public Node(int preNo, int weight) {
            this.preNo = preNo;
            this.weight = weight;
        }
    }
}
