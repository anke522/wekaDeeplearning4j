package weka.dl4j.activations;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import weka.classifiers.functions.Dl4jMlpClassifier;
import weka.core.Instances;
import weka.dl4j.layers.DenseLayer;
import weka.dl4j.layers.OutputLayer;
import weka.util.DatasetLoader;

/**
 * JUnit tests for all available activations
 *
 * @author Steven Lang
 */
public class ActivationTest {
  /**
   * Run dummy network with give activationfunction for the first layer
   *
   * @param act Activation function to test
   * @throws Exception Something went wrong.
   */
  private static void runClf(Activation act) throws Exception {
    Dl4jMlpClassifier clf = new Dl4jMlpClassifier();
    // Data
    DenseLayer denseLayer = new DenseLayer();
    denseLayer.setNOut(2);
    denseLayer.setLayerName("Dense-layer");
    denseLayer.setActivationFunction(act);

    OutputLayer outputLayer = new OutputLayer();
    outputLayer.setActivationFunction(new ActivationSoftmax());
    outputLayer.setLayerName("Output-layer");

    clf.setNumEpochs(1);
    clf.setLayers(denseLayer, outputLayer);

    final Instances data = DatasetLoader.loadIris();
    try{
      clf.buildClassifier(data);
    } catch (Exception e){
      Assert.fail(String.format("Failed for activiation <%s>. Exception was: %s. Stacktrace:%s\n", act.getClass().getSimpleName(), e.toString(),
          Arrays.toString(e.getStackTrace())));
    }
    clf.distributionsForInstances(data);
  }

  /**
   * Test all activation functions with a dummy network
   *
   * @throws Exception
   */
  @Test
  public void testActivations() throws Exception {
    Activation[] acts =
        new Activation[] {
          new ActivationCube(),
          new ActivationELU(),
          new ActivationHardSigmoid(),
          new ActivationHardTanH(),
          new ActivationIdentity(),
          new ActivationLReLU(),
          new ActivationRationalTanh(),
          new ActivationReLU(),
          new ActivationRReLU(),
          new ActivationHardSigmoid(),
          new ActivationSoftmax(),
          new ActivationSoftPlus(),
          new ActivationSoftSign(),
          new ActivationHardTanH()
        };
    for (Activation act : acts) {
      runClf(act);
    }
  }
}
