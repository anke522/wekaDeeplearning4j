package weka.dl4j.iterators.instance.sequence.text.cnn;

import java.util.Enumeration;
import org.deeplearning4j.iterator.LabeledSentenceProvider;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import weka.core.Instances;
import weka.core.InvalidInputDataException;
import weka.core.Option;
import weka.dl4j.iterators.dataset.sequence.text.cnn.CnnSentenceDataSetIterator;
import weka.dl4j.iterators.instance.sequence.text.AbstractTextEmbeddingIterator;

/**
 * Iterator that constructs datasets from text data for convolutional networks.
 *
 * @author Steven Lang
 */
public class CnnTextEmbeddingInstanceIterator extends AbstractTextEmbeddingIterator {

  private static final long serialVersionUID = 3417451906101970927L;

  @Override
  public DataSetIterator getDataSetIterator(Instances data, int seed, int batchSize) {
    initialize();
    LabeledSentenceProvider clsp = getSentenceProvider(data);
    return new CnnSentenceDataSetIterator.Builder()
        .wordVectors(wordVectors)
        .tokenizerFactory(tokenizerFactory.getBackend())
        .sentenceProvider(clsp)
        .minibatchSize(batchSize)
        .maxSentenceLength(truncateLength)
        .useNormalizedWordVectors(false)
        .sentencesAlongHeight(true)
        .stopwords(stopwords)
        .build();
  }

  @Override
  public void validate(Instances data) throws InvalidInputDataException {
    if (!getWordVectorLocation().isFile()) {
      throw new InvalidInputDataException("File not valid: " + getWordVectorLocation());
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
