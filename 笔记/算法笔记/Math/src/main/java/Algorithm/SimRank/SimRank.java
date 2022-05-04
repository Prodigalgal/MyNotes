package Algorithm.SimRank;

import java.io.*;
import java.util.*;

public class SimRank {
    public static void main(String[] args) {
        String path = "src/Algorithm/Data/tmdb_movies_data.csv";
        SimRank sr = new SimRank(path);
        sr.sim(4, new String[]{"genres", "keywords"});
        try {
            FileWriter fw = new FileWriter("src/Algorithm/Data/tmdb_movies_data_result.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            double[][] doubles = sr.scores_list.get(0);
            for (double[] aDouble : doubles) {
                StringBuilder sb = new StringBuilder();
                for (double v : aDouble) {
                    sb.append(v).append(",");
                }
                bw.write(sb.toString()+"\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<double[][]> scores_list = new ArrayList<>();

    public List<Item> items = new ArrayList<>();

    public double c = 0.8;

    public List<HashMap<String, Integer>> attrs_map = new ArrayList<>();

    public String[] head;


    public SimRank(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            head = br.readLine().split(",");
            for (int i = 0; i < head.length; i++)
                attrs_map.add(new HashMap<>());
            String line;
            int index = 0;

            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                if (split.length == head.length) {
                    items.add(new Item(head, split, index++, attrs_map));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        attrs_map.forEach(a -> {
            double[][] doubles = new double[a.size()][a.size()];
            for (int i = 0; i < a.size(); i++) {
                for (int j = 0; j < a.size(); j++) {
                    if (i == j) doubles[i][j] = 1.0;
                }
            }
            scores_list.add(doubles);
        });
    }

    static class Attribute {
        String name;
        String index;

        List<Item> items = new ArrayList<>();

        public Attribute(String name, String index) {
            this.name = name;
            this.index = index;
        }
    }

    static class Item {
        Map<String, List<String>> attributes = new HashMap<>();

        List<Attribute> attributes_list = new ArrayList<>();

        String id;

        int index;

        public Item(String[] head, String[] attrs, int index, List<HashMap<String, Integer>> list) {
            this.id = attrs[0];
            list.get(0).put(id, index);
            this.index = index;
            for (int i = 1; i < head.length; i++) {
                String[] split = attrs[i].split("\\|");
                List<String> ll = new ArrayList<>();
                Collections.addAll(ll, split);
                for (String s : ll) {
                    HashMap<String, Integer> map = list.get(i);
                    map.putIfAbsent(s, map.size());
                }
                attributes.put(head[i], ll);
            }
        }
    }

    public void sim(int iter, String[] attrs) {
        for (int x = 0; x < iter; x++) {
            items.forEach(i -> items.forEach(j -> {
                if (!i.equals(j))
                    scores_list.get(0)[i.index][j.index] = scores_list.get(0)[j.index][i.index] = calculate(i, j, attrs);
            }));
        }
    }

    public Double calculate(Item i1, Item i2, String[] attrs) {
        double score = 0.0;

        for (String attr : attrs) {

            int index = 0;
            for (int i = 0; i < head.length; i++) {
                if (attr.equals(head[i])) {
                    index = i;
                    break;
                }
            }

            double[][] scores = scores_list.get(index);
            HashMap<String, Integer> attr_map = attrs_map.get(index);

            List<String> l1 = new ArrayList<>(i1.attributes.get(attr));
            List<String> l2 = new ArrayList<>(i2.attributes.get(attr));
            double all_score = l1.size() * l2.size();

            if (l1.size() == 0 || l2.size() == 0) return score;

            for (String attr1 : l1) {
                for (String attr2 : l2) {
                    score += getSimilarity(attr1, attr2, scores, attr_map);
                }
            }

            score += scores_list.get(0)[i1.index][i2.index];
            score *= (c / all_score);
        }

        return score;
    }

    public Double calculateAttr() {
        return 0.0;
    }

    public Double getSimilarity(String attr1, String attr2, double[][] scores, HashMap<String, Integer> attrs) {
        int index1 = attrs.get(attr1);
        int index2 = attrs.get(attr2);
        return scores[index1][index2];
    }


}
