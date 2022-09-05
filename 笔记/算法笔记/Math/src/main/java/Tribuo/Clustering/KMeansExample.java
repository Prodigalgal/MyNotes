package Tribuo.Clustering;

import org.tribuo.MutableDataset;
import org.tribuo.clustering.ClusterID;
import org.tribuo.clustering.evaluation.ClusteringEvaluator;
import org.tribuo.clustering.example.GaussianClusterDataSource;
import org.tribuo.clustering.kmeans.KMeansTrainer;
import org.tribuo.util.Util;

public class KMeansExample {
    public static void main(String[] args) {
        // 创建分类器
        var eval = new ClusteringEvaluator();
        // 使用内置数据集
        var data = new MutableDataset<>(new GaussianClusterDataSource(500, 1L));
        var test = new MutableDataset<>(new GaussianClusterDataSource(500, 2L));

        // KM
        // 五个中心
        // 迭代十次
        // 使用欧氏距离
        // 一个线程
        // 随机种子
        var trainer = new KMeansTrainer(5, 10, KMeansTrainer.Distance.EUCLIDEAN, 1, 1);
        var startTime = System.currentTimeMillis();
        var model = trainer.train(data);
        var endTime = System.currentTimeMillis();
        System.out.println("Training with 5 clusters took " + Util.formatDuration(startTime, endTime));

        // 获取质心
        var centroids = model.getCentroids();
        for (var centroid : centroids) {
            System.out.println(centroid);
        }

        // KM++
        var plusplusTrainer = new KMeansTrainer(5,10, KMeansTrainer.Distance.EUCLIDEAN, KMeansTrainer.Initialisation.PLUSPLUS,1,1);
        startTime = System.currentTimeMillis();
        var plusplusModel = plusplusTrainer.train(data);
        endTime = System.currentTimeMillis();
        System.out.println("Training with 5 clusters took " + Util.formatDuration(startTime,endTime));

        // 获取质心
        var ppCentroids = plusplusModel.getCentroids();
        for (var centroid : ppCentroids) {
            System.out.println(centroid);
        }

        // 模型评估
        var trainEvaluation = eval.evaluate(model,data);
        System.out.println(trainEvaluation.toString());
        var testEvaluation = eval.evaluate(model,test);
        System.out.println(testEvaluation.toString());
        var testPlusPlusEvaluation = eval.evaluate(plusplusModel,test);
        System.out.println(testPlusPlusEvaluation.toString());

        // 多线程

    }
}
