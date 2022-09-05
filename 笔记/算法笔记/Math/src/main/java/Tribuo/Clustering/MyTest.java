package Tribuo.Clustering;

import org.tribuo.clustering.ClusteringFactory;
import org.tribuo.data.csv.CSVLoader;

public class MyTest {
    public static void main(String[] args) {
        var clusteringFactory = new ClusteringFactory();
        var csvLoader = new CSVLoader<>(';', clusteringFactory);


    }
}
