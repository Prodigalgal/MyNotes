package Tribuo.Classification;

import com.oracle.labs.mlrg.olcut.provenance.ProvenanceUtil;
import org.tribuo.Model;
import org.tribuo.MutableDataset;
import org.tribuo.Trainer;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.evaluation.TrainTestSplitter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassificationExample {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 文件加载器
        var labelFactory = new LabelFactory();
        var csvLoader = new CSVLoader<>(labelFactory);

        // 设置列标题
        var irisHeaders = new String[]{"sepalLength", "sepalWidth", "petalLength", "petalWidth", "species"};
        // 设置文件路径、设置哪个列是输出、将预设标题放入，最后生成数据源
        var irisesSource = csvLoader.loadDataSource(
                Paths.get("src/main/java/Tribuo/Classification/bezdekIris.data"),
                "species",
                irisHeaders);
        // 切分数据按7：3
        var irisSplitter = new TrainTestSplitter<>(irisesSource, 0.7, 1L);
        // 将训练与测试数据分开
        var trainingDataset = new MutableDataset<>(irisSplitter.getTrain());
        var testingDataset = new MutableDataset<>(irisSplitter.getTest());

        System.out.printf("Training data size = %d, number of features = %d, number of classes = %d%n", trainingDataset.size(), trainingDataset.getFeatureMap().size(), trainingDataset.getOutputInfo().size());
        System.out.printf("Testing data size = %d, number of features = %d, number of classes = %d%n", testingDataset.size(), testingDataset.getFeatureMap().size(), testingDataset.getOutputInfo().size());

        // 创建训练模型
        Trainer<Label> trainer = new LogisticRegressionTrainer();
        // 查看它的默认超参数是什么
        System.out.println(trainer.toString());
        // 使用完全可配置的参数。LinearSGDTrainer
        // LinearSGDTrainer(objective=LogMulticlass,optimiser=AdaGrad(initialLearningRate=1.0,epsilon=0.1,initialValue=0.0),epochs=5,minibatchSize=1,seed=12345)

        // 开始训练
        Model<Label> irisModel = trainer.train(trainingDataset);

        // 评估模型
        // class：类别
        // n：该类数量
        // tp：分类器正确预测的次数
        // fn：分类器将该类预测为另一类的次数
        // fp：分类器将另一类预测到该类的次数
        // recall：tp / n 即分类器正确检测到此类的比率
        // prec：精度 n / （tp + fp）
        // accuracy：准确性 sum（tp） / sum（n）
        // Balanced Error Rate：平衡错误率，每个类错误率的平均值
        var evaluator = new LabelEvaluator();
        var evaluation = evaluator.evaluate(irisModel, testingDataset);
        System.out.println(evaluation.toString());
        // 打印混淆矩阵
        System.out.println(evaluation.getConfusionMatrix().toString());

        // 模型元数据
        // 看到这 4 个特征，以及它们值的直方图
        var featureMap = irisModel.getFeatureIDMap();
        for (var v : featureMap) {
            System.out.println(v.toString());
            System.out.println();
        }
        // 查看模型数据来源
        var provenance = irisModel.getProvenance();
        System.out.println(ProvenanceUtil.formattedProvenanceString(provenance.getDatasetProvenance().getSourceProvenance()));
        // 检查训练器的出处，以了解有关训练算法的信息
        System.out.println(ProvenanceUtil.formattedProvenanceString(provenance.getTrainerProvenance()));

        // 加载和保存模型
        // 使用Java序列化来保存和加载模型
        File tmpFile = new File("iris-lr-model.ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tmpFile))) {
            oos.writeObject(irisModel);
        }

        // 加载保存的模型
        // 使用Tribuo附带的序列化允许列表,确保只在Tribuo相关的类中加载
        // String filterPattern = Files.readAllLines(Paths.get("../docs/jep-290-filter.txt")).get(0);
        // ObjectInputFilter filter = ObjectInputFilter.Config.createFilter(filterPattern);
        Model<?> loadedModel;
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(tmpFile)))) {
            // ois.setObjectInputFilter(filter);
            loadedModel = (Model<?>) ois.readObject();
        }

        // 需要一个未经检查的强制转换来将正确的类型应用于模型
        // 在正常使用模型之前，可以使用此检查来保护强制转换为适当的泛型类型。
        if (loadedModel.validate(Label.class)) {
            System.out.println("It's a Model<Label>!");
        } else {
            System.out.println("It's some other kind of Model.");
        }

        // 通过比较模型的出处来检查模型是否相同
        System.out.println(loadedModel.getProvenance().equals(irisModel.getProvenance()));

    }
}
