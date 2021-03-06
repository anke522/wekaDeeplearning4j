package weka.filters.unsupervised.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.WekaException;
import weka.filters.AbstractFilterTest;
import weka.filters.Filter;

/**
 * JUnit tests for the {@link FlatToRelational}.
 *
 * @author Steven Lang
 */
public class FlatToRelationalTest extends AbstractFilterTest {

  /**
   * Constructs the <code>AbstractFilterTest</code>. Called by subclasses.
   *
   * @param name the name of the test class
   */
  public FlatToRelationalTest(String name) {
    super(name);
  }

  public void testDetermineOutputFormat() {}

  /**
   * Test filtering for different attribute types.
   *
   * @throws Exception Something went wrong
   */
  public void testProcess() throws Exception {
    Instances[] datasets =
        new Instances[] {
          makeTestDataset(0, 100, 100, 0, 0, 0, 0, 2, Attribute.NOMINAL, 100),
          makeTestDataset(0, 100, 0, 100, 0, 0, 0, 2, Attribute.NOMINAL, 100),
          makeTestDataset(0, 100, 0, 0, 100, 0, 0, 2, Attribute.NOMINAL, 100),
          makeTestDataset(0, 100, 0, 0, 0, 100, 0, 2, Attribute.NOMINAL, 100),
        };
    for (Instances insts : datasets) {
      final FlatToRelational filter = getFilter("1-100", 5, true);
      filter.setInputFormat(insts);
      final Instances filteredData = Filter.useFilter(insts, filter);

      // Attribute number should be reduced to 2 (one bag and one class)
      assertEquals(2, filteredData.numAttributes());
      // Check if first attribute is relation valued
      assertTrue(filteredData.attribute(0).isRelationValued());
      // Check if first attributes relation has five attributes
      assertEquals(5, filteredData.attribute(0).relation().numAttributes());
      // Check if data size is still the same
      assertEquals(insts.numInstances(), filteredData.numInstances());
    }
  }

  /**
   * Test multiple attribute types.
   *
   * @throws Exception Something went wrong
   */
  public void testMultipleAttributeTypes() throws Exception {
    Attribute att1 = new Attribute("numeric1");
    Attribute att2 = new Attribute("nominal1", Arrays.asList("val1", "val2"));
    Attribute att3 = att1.copy("numeric2");
    Attribute att4 = att2.copy("nominal2");
    ArrayList<Attribute> atts = new ArrayList<>(Arrays.asList(att1, att2, att3, att4));
    Instances insts = new Instances("test", atts, 0);

    // Generate 100 test instances
    for (int i = 0; i < 100; i++) {
      Instance inst = new DenseInstance(4);
      inst.setDataset(insts);
      inst.setValue(0, Math.random());
      inst.setValue(1, "val1");
      inst.setValue(2, Math.random());
      inst.setValue(3, "val2");
      insts.add(inst);
    }
    final FlatToRelational filter = getFilter("1-4", 2, true);
    filter.setInputFormat(insts);
    final Instances filteredData = Filter.useFilter(insts, filter);
    // Attribute number should be reduced to 1 (one bag)
    assertEquals(1, filteredData.numAttributes());
    // Check if first attribute is relation valued
    assertTrue(filteredData.attribute(0).isRelationValued());
    // Check if first attributes relation has 2 attributes
    assertEquals(2, filteredData.attribute(0).relation().numAttributes());
    // Check if data size is still the same
    assertEquals(insts.numInstances(), filteredData.numInstances());
    // Check if first attribute of relation is numeric
    assertEquals(Attribute.NUMERIC, filteredData.attribute(0).relation().attribute(0).type());
    assertEquals(Attribute.NOMINAL, filteredData.attribute(0).relation().attribute(1).type());
  }

  /** Creates a test dataset */
  public static Instances makeTestDataset(
      int seed,
      int numInstances,
      int numNominal,
      int numNumeric,
      int numString,
      int numDate,
      int numRelational,
      int numClasses,
      int classType,
      int classIndex)
      throws Exception {

    ArrayList<Attribute> attributes = new ArrayList<>();

    final List<String> nominalValues = Arrays.asList("val1", "val2");
    Attribute att = new Attribute("nominal", nominalValues);
    for (int i = 0; i < numNominal; i++) {
      attributes.add(att.copy(att.name() + i));
    }

    att = new Attribute("numeric");
    for (int i = 0; i < numNumeric; i++) {
      attributes.add(att.copy(att.name() + i));
    }

    att = new Attribute("string", true);
    for (int i = 0; i < numString; i++) {
      attributes.add(att.copy(att.name() + i));
    }

    att = new Attribute("date", "yyyy");
    for (int i = 0; i < numDate; i++) {

      attributes.add(att.copy(att.name() + i));
    }

    Attribute cls =
        new Attribute(
            "class",
            IntStream.range(0, numClasses).mapToObj(i -> "class" + i).collect(Collectors.toList()));
    attributes.add(cls);
    Random rand = new Random(seed);
    String[] randString = "l k ; r j i e a j ; l s k d n c x c e i u r k n ; a".split(" ");
    String[] randDates = {"2000", "2001", "2002"};
    Instances data = new Instances("data", attributes, numInstances);
    data.setClassIndex(attributes.indexOf(cls));
    for (int i = 0; i < numInstances; i++) {
      Instance inst = new DenseInstance(attributes.size());
      inst.setDataset(data);
      int j = 0;
      int offset = 0;
      for (; j < numNominal + offset; j++) {
        inst.setValue(j, nominalValues.get(rand.nextInt(nominalValues.size())));
      }
      offset += numNominal;

      for (; j < numNumeric + offset; j++) {
        inst.setValue(j, rand.nextDouble());
      }
      offset += numNumeric;

      for (; j < numString + offset; j++) {
        inst.setValue(j, randString[rand.nextInt(randString.length)]);
      }
      offset += numString;
      for (; j < numDate + offset; j++) {
        inst.setValue(j, attributes.get(j).parseDate(randDates[rand.nextInt(randDates.length)]));
      }
      data.add(inst);
    }

    return data;
  }

  public void testKeepOtherAttributes() throws Exception {
    final Instances data = makeTestDataset(0, 100, 100, 10, 0, 0, 0, 2, Attribute.NOMINAL, 110);
     FlatToRelational filter = getFilter("1-100", 5, true);
    filter.setInputFormat(data);
    Instances filteredData = Filter.useFilter(data, filter);
    // 1 bag + 10 numeric + 1 class
    assertEquals(1 + 10 + 1, filteredData.numAttributes());
    for (int i = 1; i < 11; i++) {
      assertEquals(Attribute.NUMERIC, filteredData.attribute(i).type());
    }

    filter = getFilter("1-100", 5, false);
    filter.setInputFormat(data);
    filteredData = Filter.useFilter(data, filter);
    // 1 bag  + 1 class
    assertEquals(1 + 1, filteredData.numAttributes());
  }

  /**
   * Test invalid input.
   *
   * @throws Exception Something went wrong
   */
  public void testInvalidInput() throws Exception {
    Instances generated = makeTestDataset(0, 100, 0, 10, 0, 0, 0, 2, Attribute.NOMINAL, 10);

    FlatToRelational filter = getFilter("1-9", 2, true);
    filter.setInputFormat(generated);
    try {
      Filter.useFilter(generated, filter);
      fail(
          "9 attributes have been selected while the number of timeseries variables "
              + "was 2. 9 mod 2 != 0. Exception expected.");
    } catch (WekaException we) {
      // All good
    }

    filter = getFilter("1-10", 2, true);
    generated = makeTestDataset(0, 100, 4, 4, 4, 4, 4, 2, Attribute.NOMINAL, 20);
    filter.setInputFormat(generated);
    try {
      Filter.useFilter(generated, filter);
      fail("The attributes types do not match in the given range. Exception expected.");
    } catch (WekaException we) {
      // All good
    }

    filter = getFilter("1-10", -1, true);
    generated = makeTestDataset(0, 100, 4, 4, 4, 4, 4, 2, Attribute.NOMINAL, 20);
    filter.setInputFormat(generated);
    try {
      Filter.useFilter(generated, filter);
      fail("The number of variables was set to -1. Exception expected.");
    } catch (WekaException we) {
      // All good
    }
  }

  @Override
  public Filter getFilter() {
    return new FlatToRelational();
  }

  /**
   * Get the filter initialized with given parameters.
   *
   * @param range Attribute selection range
   * @param numVariables Number of variables in the timeseries
   * @return Filter object
   */
  protected FlatToRelational getFilter(String range, int numVariables, boolean keepOtherAttributes) {
    final FlatToRelational f = new FlatToRelational();
    f.setNumVariables(numVariables);
    f.setRange(range);
    f.setKeepOtherAttributes(keepOtherAttributes);
    return f;
  }
}
