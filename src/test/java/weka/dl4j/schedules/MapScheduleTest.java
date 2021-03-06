package weka.dl4j.schedules;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import org.junit.Test;
import weka.dl4j.ApiWrapperTest;

public class MapScheduleTest extends AbstractScheduleTest<MapSchedule> {

  @Test
  public void setValues() {
    ImmutableMap<Integer, Double> values = ImmutableMap.of(0, 0.1, 1, 0.2, 2, 0.3, 3, 0.4);
    wrapper.setValues(values);

    assertEquals(values, wrapper.getValues());
  }

  @Override
  public MapSchedule getApiWrapper() {
    return new MapSchedule();
  }
}