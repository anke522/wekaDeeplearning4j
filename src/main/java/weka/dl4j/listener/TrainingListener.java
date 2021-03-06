package weka.dl4j.listener;

import java.io.Serializable;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.optimize.api.BaseTrainingListener;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import weka.core.Option;
import weka.core.OptionHandler;

import java.util.Enumeration;

/**
 * Iteration listener that can be attached to a Dl4j model.
 *
 * @author Steven Lang
 */
public abstract class TrainingListener
    extends BaseTrainingListener implements OptionHandler, Serializable {
  /** SerialVersionUID */
  private static final long serialVersionUID = 8106114790187499011L;

  /** Number of samples */
  protected int numSamples;
  /** Number of epochs */
  protected int numClasses;
  /** Number of classes */
  protected int numEpochs;
  /** Training dataset iterator */
  protected transient DataSetIterator validationIterator;
  /** Validation dataset iterator */
  protected transient DataSetIterator trainIterator;

  /**
   * Initialize the iterator with its necessary member variables
   * @param numClasses Number of classes
   * @param numEpochs Number of epochs
   * @param numSamples Number of Samples
   * @param trainIterator Training iterator
   * @param validationIterator Validation iterator
   */
  public void init(
      int numClasses,
      int numEpochs,
      int numSamples,
      DataSetIterator trainIterator,
      DataSetIterator validationIterator) {
    this.numClasses = numClasses;
    this.numEpochs = numEpochs;
    this.numSamples = numSamples;
    this.trainIterator = trainIterator;
    this.validationIterator = validationIterator;
  }

  /**
   * Log a message
   * @param msg Message
   */
  public abstract void log(String msg);

  @Override
  public void iterationDone(Model model, int iteration, int epoch) {}

  /**
   * Returns an enumeration describing the available options.
   *
   * @return an enumeration of all the available options.
   */
  @Override
  public Enumeration<Option> listOptions() {

    return Option.listOptionsForClass(this.getClass()).elements();
  }

  /**
   * Gets the current settings of the Classifier.
   *
   * @return an array of strings suitable for passing to setOptions
   */
  @Override
  public String[] getOptions() {

    return Option.getOptions(this, this.getClass());
  }

  /**
   * Parses a given list of options.
   *
   * @param options the list of options as an array of strings
   * @exception Exception if an option is not supported
   */
  public void setOptions(String[] options) throws Exception {

    Option.setOptions(options, this, this.getClass());
  }
}
