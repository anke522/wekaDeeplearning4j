package weka.dl4j.text.tokenization.tokenizer.factory;

import java.io.Serializable;
import java.util.Enumeration;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.dl4j.ApiWrapper;
import weka.dl4j.ApiWrapperUtil;

/**
 * TokenizerFactory wrapper for Deeplearning4j's {@link org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory}
 * classes.
 *
 * @author Steven Lang
 */
@EqualsAndHashCode
@ToString
public abstract class TokenizerFactory<T extends org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory>
    implements OptionHandler, ApiWrapper<T>, Serializable {


  private static final long serialVersionUID = 4013807949829115985L;
  /**
   * TokenizerFactory that is backing the implementation
   */
  T backend;

  public TokenizerFactory() {
    initializeBackend();
  }

  /**
   * Create an API wrapped schedule from a given ISchedule object.
   *
   * @param newBackend Backend object
   * @return API wrapped object
   */
  public static TokenizerFactory<? extends org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory> create(
      org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory newBackend) {
    return ApiWrapperUtil.getImplementingWrapper(
        TokenizerFactory.class, newBackend, "weka.dl4j.text.tokenization.tokenizer.factory");
  }

  @Override
  public void setBackend(T newBackend) {
    backend = newBackend;
  }

  @Override
  public T getBackend() {
    initializeBackend();
    return backend;
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
