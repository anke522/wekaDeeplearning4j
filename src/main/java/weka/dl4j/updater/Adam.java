package weka.dl4j.updater;

import java.util.Enumeration;
import weka.core.Option;
import weka.core.OptionMetadata;

/**
 * A WEKA version of DeepLearning4j's Adam.
 *
 * @author Steven Lang
 */
public class Adam extends Updater<org.nd4j.linalg.learning.config.Adam> {
  private static final long serialVersionUID = 667880237656554906L;

  @OptionMetadata(
    displayName = "beta1MeanDecay",
    description =
        "The mean decay (default = "
            + org.nd4j.linalg.learning.config.Adam.DEFAULT_ADAM_BETA1_MEAN_DECAY
            + ").",
    commandLineParamName = "beta1MeanDecay",
    commandLineParamSynopsis = "-beta1MeanDecay <double>",
    displayOrder = 1
  )
  public double getBeta1() {
    return backend.getBeta1();
  }

  public void setBeta1(double beta1) {
    backend.setBeta1(beta1);
  }

  @OptionMetadata(
    displayName = "beta2VarDecay",
    description =
        "The var decay (default = "
            + org.nd4j.linalg.learning.config.Adam.DEFAULT_ADAM_BETA2_VAR_DECAY
            + ").",
    commandLineParamName = "beta2VarDecay",
    commandLineParamSynopsis = "-beta2VarDecay <double>",
    displayOrder = 2
  )
  public double getBeta2() {
    return backend.getBeta2();
  }

  public void setBeta2(double beta2) {
    backend.setBeta2(beta2);
  }

  @OptionMetadata(
    displayName = "epsilon",
    description =
        "The epsilon parameter (default = "
            + org.nd4j.linalg.learning.config.Adam.DEFAULT_ADAM_EPSILON
            + ").",
    commandLineParamName = "epsilon",
    commandLineParamSynopsis = "-epsilon <double>",
    displayOrder = 3
  )
  public double getEpsilon() {
    return backend.getEpsilon();
  }

  public void setEpsilon(double epsilon) {
    backend.setEpsilon(epsilon);
  }

  @Override
  public org.nd4j.linalg.learning.config.Adam getBackend() {
    return backend;
  }

  @Override
  public void initializeBackend() {
    backend = new org.nd4j.linalg.learning.config.Adam();
  }

  /**
   * Returns an enumeration describing the available options.
   *
   * @return an enumeration of all the available options.
   */
  @Override
  public Enumeration<Option> listOptions() {
    return Option.listOptionsForClassHierarchy(this.getClass(),super.getClass()).elements();
  }

  /**
   * Gets the current settings of the Classifier.
   *
   * @return an array of strings suitable for passing to setOptions
   */
  @Override
  public String[] getOptions() {
    return Option.getOptionsForHierarchy(this, super.getClass());
  }

  /**
   * Parses a given list of options.
   *
   * @param options the list of options as an array of strings
   * @throws Exception if an option is not supported
   */
  public void setOptions(String[] options) throws Exception {
    Option.setOptionsForHierarchy(options, this, super.getClass());
  }


}
