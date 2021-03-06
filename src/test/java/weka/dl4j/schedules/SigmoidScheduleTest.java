package weka.dl4j.schedules;

import static org.junit.Assert.*;

import org.junit.Test;
import weka.dl4j.ApiWrapperTest;

public class SigmoidScheduleTest extends AbstractScheduleTest<SigmoidSchedule> {

  @Test
  public void setGamma() {
    double value = 123.456;
    wrapper.setGamma(value);

    assertEquals(value, wrapper.getGamma(), PRECISION);
  }

  @Test
  public void setStepSize() {
    int value = 123;
    wrapper.setStepSize(value);

    assertEquals(value, wrapper.getStepSize(), PRECISION);
  }

  @Override
  public SigmoidSchedule getApiWrapper() {
    return new SigmoidSchedule();
  }
}