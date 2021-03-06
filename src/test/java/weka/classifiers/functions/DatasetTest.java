package weka.classifiers.functions;

import java.io.File;
import org.junit.Assert;
import weka.classifiers.Evaluation;
import weka.dl4j.activations.ActivationReLU;
import weka.dl4j.activations.ActivationSoftmax;
import weka.dl4j.iterators.instance.ImageInstanceIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Instances;
import weka.dl4j.NeuralNetConfiguration;
import weka.dl4j.layers.DenseLayer;
import weka.dl4j.layers.OutputLayer;
import weka.util.DatasetLoader;

/**
 * JUnit tests applying the classifier to different arff datasets.
 *
 * @author Steven Lang
 */
public class DatasetTest {

  /** Logger instance */
  private static final Logger logger = LoggerFactory.getLogger(DatasetTest.class);

  /** Default number of epochs */
  private static final int DEFAULT_NUM_EPOCHS = 1;

  /** Seed */
  private static final int SEED = 42;

  /** Default batch size */
  private static final int DEFAULT_BATCHSIZE = 32;
  /** Current name */
  @Rule public TestName name = new TestName();
  /** Classifier */
  private Dl4jMlpClassifier clf;
  /** Start time for time measurement */
  private long startTime;

  @Before
  public void before() {
    // Init mlp clf
    clf = new Dl4jMlpClassifier();
    clf.setSeed(SEED);
    clf.setNumEpochs(DEFAULT_NUM_EPOCHS);
    clf.setDebug(false);

    // Init data
    startTime = System.currentTimeMillis();
    //        TestUtil.enableUIServer(clf);
  }

  @After
  public void after() {
    double time = (System.currentTimeMillis() - startTime) / 1000.0;
    logger.info("Testmethod: " + name.getMethodName());
    logger.info("Time: " + time + "s");
  }

  /**
   * Test date class.
   *
   * @throws Exception IO error.
   */
  @Test
  public void testDateClass() throws Exception {
    runClf(DatasetLoader.loadWineDate());
  }
  /**
   * Test numeric class.
   *
   * @throws Exception IO error.
   */
  @Test
  public void testNumericClass() throws Exception {
    runClf(DatasetLoader.loadFishCatch());
  }

  /**
   * Test nominal class.
   *
   * @throws Exception IO error.
   */
  @Test
  public void testNominal() throws Exception {
    runClf(DatasetLoader.loadIris());
  }

  @Test
  public void testMissingValues() throws Exception {
    runClf(DatasetLoader.loadIrisMissingValues());
  }

  private void runClf(Instances data) throws Exception {
    // Data
    DenseLayer denseLayer = new DenseLayer();
    denseLayer.setNOut(32);
    denseLayer.setLayerName("Dense-layer");
    denseLayer.setActivationFunction(new ActivationReLU());

    OutputLayer outputLayer = new OutputLayer();
    outputLayer.setActivationFunction(new ActivationSoftmax());
    outputLayer.setLayerName("Output-layer");

    NeuralNetConfiguration nnc = new NeuralNetConfiguration();

    clf.setNumEpochs(DEFAULT_NUM_EPOCHS);
    clf.setNeuralNetConfiguration(nnc);
    clf.setLayers(denseLayer, outputLayer);

    clf.buildClassifier(data);
    clf.distributionsForInstances(data);
  }

  /**
   * Test datasets with class meta data that is not in lexicographic order.
   *
   * @throws Exception Something went wrong.
   */
  @Test
  public void testMixedClassOrder() throws Exception {
    String prefix = "src/test/resources/nominal/";

    // Get data
    Instances testProb = DatasetLoader.loadArff(prefix + "mnist.meta.minimal.arff");
    Instances testProbInverse = DatasetLoader.loadArff(prefix + "mnist.meta.minimal.mixed-class-meta-data.arff");

    Evaluation evalNormal = eval(testProb);
    Evaluation evalMixed = eval(testProbInverse);

    // Compare accuracy
    Assert.assertEquals(evalNormal.pctCorrect(), evalMixed.pctCorrect(), 1e-7);
    Assert.assertEquals(evalNormal.pctIncorrect(), evalMixed.pctIncorrect(), 1e-7);
  }

  private static Evaluation eval(Instances metaData)
      throws Exception {

    String imagesPath = "src/test/resources/nominal/mnist-minimal";
    Dl4jMlpClassifier clf = new Dl4jMlpClassifier();
    ImageInstanceIterator iii = new ImageInstanceIterator();
    iii.setImagesLocation(new File(imagesPath));
    iii.setTrainBatchSize(2);

    clf.setInstanceIterator(iii);
    clf.setNumEpochs(5);

    // Build clf
    clf.buildClassifier(metaData);

    // Evaluate clf
    Evaluation trainEval = new Evaluation(metaData);
    trainEval.evaluateModel(clf, metaData);
    return trainEval;
  }
}
