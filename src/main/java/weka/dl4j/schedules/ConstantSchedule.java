package weka.dl4j.schedules;

import java.util.Enumeration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.nd4j.linalg.schedule.ISchedule;
import weka.core.Option;
import weka.core.OptionMetadata;
import weka.dl4j.schedules.ConstantSchedule.ConstantScheduleImpl;
import weka.gui.ProgrammaticProperty;

@EqualsAndHashCode(callSuper = true)
@ToString
public class ConstantSchedule extends Schedule<ConstantScheduleImpl> {

  private static final long serialVersionUID = -6950419697195232864L;

  @Override
  public ConstantScheduleImpl getBackend() {
    return backend;
  }

  @Override
  public void setBackend(ConstantScheduleImpl newBackend) {
    // Do nothing
  }

  @Override
  public void initializeBackend() {
    this.backend = new ConstantScheduleImpl(initialValue);
  }

  @Override
  @ProgrammaticProperty
  public double getInitialValue() {
    return initialValue;
  }

  @Override
  @ProgrammaticProperty
  public void setInitialValue(double initialValue) {
    this.initialValue = initialValue;
    this.backend.setValue(initialValue);
  }

  @Data
  public static class ConstantScheduleImpl implements ISchedule {

    private double value;

    private static final long serialVersionUID = 7134767476736787119L;

    public ConstantScheduleImpl() {
      this(1.0);
    }

    public ConstantScheduleImpl(double value) {
      this.value = value;
    }

    @Override
    public double valueAt(int iteration, int epoch) {
      return value;
    }

    @Override
    public ISchedule clone() {
      return new ConstantScheduleImpl(value);
    }
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
