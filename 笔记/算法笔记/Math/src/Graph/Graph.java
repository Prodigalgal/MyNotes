package Graph;

import java.util.*;

public class Graph {
    public ArrayList<String> vertexes;
    public boolean[] isVisited;
    public int[][] edges;
    public int lineNum;
    public int weight = 1;
    public int n;
    public Queue<Integer> queue;

    public Graph(int n) {
        vertexes = new ArrayList<>();
        edges = new int[n][n];
        lineNum = 0;
        this.n = n;
        isVisited = new boolean[n];
        queue = new LinkedList<>();
    }

    public static void main(String[] args) {
        Graph graph = new Graph(5);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");

        graph.addLine(0, 1);
        graph.addLine(2, 3);
        graph.addLine(1, 3);

        graph.addLine("A", "E");

        graph.show();

        // System.out.println(graph.getValueByIndex(graph.getFirstNodeIndex(0)));
        // System.out.println(graph.getValueByIndex(graph.getNextNodeIndex(0,1)));

        graph.dfs(graph.isVisited, 0);
        graph.isVisited = new boolean[graph.n];
        System.out.println();
        graph.bfs(graph.isVisited, 0);
    }


    public void bfs(boolean[] isVisited, int index) {
        queue.offer(index);

        while (!queue.isEmpty()) {
            Integer poll = queue.poll();
            System.out.print(vertexes.get(poll) + " -> ");
            isVisited[poll] = true;
            List<Integer> allNextNode = getAllNextNode(poll);

            for (int i : allNextNode) {
                System.out.print(vertexes.get(i) + " -> ");
                isVisited[i] = true;
                List<Integer> list = getAllNextNode(i);
                list.forEach(x -> queue.offer(x));
            }
        }
    }

    public void dfs(boolean[] isVisited, int index) {
        System.out.print(vertexes.get(index) + " -> ");
        isVisited[index] = true;
        List<Integer> allNextNode = getAllNextNode(index);
        for (int i : allNextNode) {
            dfs(isVisited, i);
        }
    }

    public List<Integer> getAllNextNode(int index){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < edges[index].length; i++) {
            if (edges[index][i] == weight && !isVisited[i])
                list.add(i);
        }
        return list;
    }

    public int getFirstNodeIndex(int index) {
        for (int i = 0; i < edges[index].length; i++) {
            if (edges[index][i] == weight)
                return i;
        }
        return -1;
    }

    public int getNextNodeIndex(int preIndex, int currentIndex) {
        for (int i = currentIndex + 1; i < edges[preIndex].length; i++) {
            if (edges[preIndex][i] == weight)
                return i;
        }
        return -1;
    }

    public String getValueByIndex(int index) {
        return vertexes.get(index);
    }

    public void addVertex(String node) {
        vertexes.add(node);
    }

    public void addLine(int n1, int n2) {
        edges[n1][n2] = weight;
        edges[n2][n1] = weight;
        lineNum++;
    }

    public void addLine(String s1, String s2) {
        int n1 = vertexes.indexOf(s1);
        int n2 = vertexes.indexOf(s2);
        edges[n1][n2] = weight;
        edges[n2][n1] = weight;
        lineNum++;
    }

    public int getWeight(int n1, int n2) {
        return edges[n1][n2];
    }

    public void show() {
        System.out.printf("%s\t", " ");
        for (String s : vertexes) {
            System.out.printf("%s\t", s);
        }
        System.out.println();
        int count = 0;
        for (int[] ints : edges) {
            System.out.printf("%s\t", vertexes.get(count++));
            for (int anInt : ints) {
                System.out.printf("%d\t", anInt);
            }
            System.out.println();
        }
    }

}
