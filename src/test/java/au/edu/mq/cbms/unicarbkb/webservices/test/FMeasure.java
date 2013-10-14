package au.edu.mq.cbms.unicarbkb.webservices.test;

import java.util.List;


/**
 * The {@link FMeasure} is an utility class for evaluators
 * which measure precision, recall and the resulting f-measure.
 *
 * Evaluation results are the arithmetic mean of the precision
 * scores calculated for each reference sample and
 * the arithmetic mean of the recall scores calculated for
 * each reference sample.
 */
public final class FMeasure {

	/** |selected| = true positives + false positives <br>
	 * the count of selected (or retrieved) items  */
	private long selected;
	
	/** |target| = true positives + false negatives <br>
	 * the count of target (or correct) items */
	private long target;
	
	private long truePositive;
  
  /**
   * Retrieves the arithmetic mean of the precision scores
   * calculated for each evaluated sample.
   *
   * @return the arithmetic mean of all precision scores
   */
  public double getPrecisionScore() {
    return selected > 0 ? (double)truePositive / (double)selected : 0;
  }

  /**
   * Retrieves the arithmetic mean of the recall score
   * calculated for each evaluated sample.
   *
   * @return the arithmetic mean of all recall scores
   */
  public double getRecallScore() {
    return target > 0 ? (double)truePositive / (double)target : 0;
  }
  
  /**
   * Retrieves the f-measure score.
   *
   * f-measure = 2 * precision * recall / (precision + recall)
   *
   * @return the f-measure or -1 if precision + recall <= 0
   */
  public double getFMeasure() {

    if (getPrecisionScore() + getRecallScore() > 0) {
      return 2 * (getPrecisionScore() * getRecallScore()) /
          (getPrecisionScore() + getRecallScore());
    }
    else {
      // cannot divide by zero, return error code
      return -1;
    }
  }
 
  public void updateScores(List references, List predictions) {
	  if(predictions!=null){
		  truePositive += countTruePositives(references, predictions);
		  selected += predictions.size();
	  }

	  target += references.size();
  }
  
  public void mergeInto(FMeasure measure) {
    this.selected += measure.selected;
    this.target += measure.target;
    this.truePositive += measure.truePositive;
  }
  
  /**
   * Creates a human read-able {@link String} representation.
   */
  @Override
  public String toString() {
    return "Precision: " + Double.toString(getPrecisionScore()) + "\n" +
        "Recall: " + Double.toString(getRecallScore()) + "\n" +
        "F-Measure: " + Double.toString(getFMeasure());
  }
  
  /**
   * This method counts the number of objects which are equal and
   * occur in the references and predictions arrays.
   *
   * These are the number of true positives.
   *
   * @param references the gold standard
   * @param predictions the predictions
   *
   * @return number of true positives
   */
  static int countTruePositives(List references,
      List predictions) {

    int truePositives = 0;

    // Note: Maybe a map should be used to improve performance
    for (int referenceIndex = 0; referenceIndex < references.size();
        referenceIndex++) {

      Object referenceName = references.get(referenceIndex);

      for (int predictedIndex = 0; predictedIndex < predictions.size();
          predictedIndex++) {
        if (referenceName.equals(predictions.get(predictedIndex))) {
          truePositives++;
        }
      }
    }
//System.out.println("--------------------truePositives: " + truePositives);
    return truePositives;
  }

  /**
   * Calculates the precision score for the given reference and
   * predicted spans.
   *
   * @param references the gold standard spans
   * @param predictions the predicted spans
   *
   * @return the precision score or NaN if there are no predicted spans
   */
  public static double precision(List references, List predictions) {

    if (predictions.size() > 0) {
      return countTruePositives(references, predictions) /
          (double) predictions.size();
    }
    else {
      return Double.NaN;
    }
  }

  /**
   * Calculates the recall score for the given reference and
   * predicted spans.
   *
   * @param references the gold standard spans
   * @param predictions the predicted spans
   *
   * @return the recall score or NaN if there are no reference spans
   */
  public static double recall(List references, List predictions) {

    if (references.size() > 0) {
      return countTruePositives(references, predictions) /
          (double) references.size();
    }
    else {
        return Double.NaN;
    }
  }
}
