package weka.dl4j.updater;

import java.io.Serializable;
import java.util.Enumeration;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.schedule.ISchedule;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.OptionMetadata;
import weka.dl4j.ApiWrapper;
import weka.dl4j.ApiWrapperUtil;
import weka.dl4j.schedules.ConstantSchedule;
import weka.dl4j.schedules.Schedule;
import weka.gui.ProgrammaticProperty;

/**
 * Default Updater that implements WEKA option handling.
 *
 * @author Steven Lang
 */
@EqualsAndHashCode
@ToString
public abstract class Updater<T extends IUpdater>
    implements OptionHandler, ApiWrapper<T>, Serializable {

  private static final long serialVersionUID = -7446042621087079745L;
  private static final double DEFAULT_LEARNING_RATE = 0.1;

  /** Backing IUpdater object */
  T backend;

  /** Learning rate schedule */
  private Schedule<? extends ISchedule> learningRateSchedule = new ConstantSchedule();

  /** Learning rate */
  private double learningRate = DEFAULT_LEARNING_RATE;

  public Updater() {
    initializeBackend();
    if (learningRateSchedule instanceof ConstantSchedule){
      learningRateSchedule.setInitialValue(getLearningRate());
    }
  }

  @ProgrammaticProperty
  public boolean hasLearningRate() {
    return backend.hasLearningRate();
  }

  @Override
  public void setBackend(T newBackend) {
    this.backend = newBackend;
  }

  /**
   * Get the learning rate
   *
   * @return Learning rate
   */
  @OptionMetadata(
    displayName = "lr",
    description = "The learning rate (default = " + DEFAULT_LEARNING_RATE + ").",
    commandLineParamName = "lr",
    commandLineParamSynopsis = "-lr <double>",
    displayOrder = 1
  )
  public double getLearningRate() {
    return backend.getLearningRate(0, 0);
  }

  /**
   * Set the learning rate
   *
   * @param learningRate Learning rate
   */
  public void setLearningRate(double learningRate) {
    this.learningRate = learningRate;
    if (hasLearningRate()) {
      learningRateSchedule.setInitialValue(learningRate);
      this.backend.setLrAndSchedule(learningRate, this.learningRateSchedule.getBackend());
    }
  }

  /**
   * Get the learning rate schedule
   *
   * @return Learning rate schedule
   */
  @OptionMetadata(
    displayName = "lrSchedule",
    description = "The learning rate schedule (default = ConstantScheduleImpl).",
    commandLineParamName = "lrSchedule",
    commandLineParamSynopsis = "-lrSchedule <Schedule>",
    displayOrder = 1
  )
  public Schedule getLearningRateSchedule() {
    return learningRateSchedule;
  }

  /**
   * Set the learning rate schedule
   *
   * @param learningRateSchedule Learning rate schedule
   */
  public void setLearningRateSchedule(Schedule<? extends ISchedule> learningRateSchedule) {
    this.learningRateSchedule = learningRateSchedule;
    if (hasLearningRate()) {
      learningRateSchedule.setInitialValue(learningRate);
      this.backend.setLrAndSchedule(this.learningRate, this.learningRateSchedule.getBackend());
    }
  }

  @Override
  public T getBackend() {
    return backend;
  }

  /**
   * Create an API wrapped updater from a given updater object.
   *
   * @param newBackend Backend object
   * @return API wrapped object
   */
  public static Updater<? extends IUpdater> create(IUpdater newBackend) {
    return ApiWrapperUtil.getImplementingWrapper(Updater.class, newBackend, "weka.dl4j.updater");
  }

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
   * @throws Exception if an option is not supported
   */
  public void setOptions(String[] options) throws Exception {

    Option.setOptions(options, this, this.getClass());
  }
}
