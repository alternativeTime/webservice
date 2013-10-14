package au.edu.mq.cbms.unicarbkb.webservices.test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.primitives.Longs;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.commons.logging.*;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import au.edu.mq.cbms.unicarbkb.webservices.Algorithm.DotProduct;
import au.edu.mq.cbms.unicarbkb.webservices.db.DbConnector;
import au.edu.mq.cbms.unicarbkb.webservices.db.Prepare;
import au.edu.mq.cbms.unicarbkb.webservices.db.Search;
import au.edu.mq.cbms.unicarbkb.webservices.util.MyLogger;

public class GlycanCombine2F1 {
	/**
	 * |selected| = true positives + false positives <br>
	 * the count of selected (or retrieved) items
	 */
	private long selected;
	private final static Logger LOGGER = Logger
			.getLogger(GlycanCombine2F1.class.getName());

	// public static final Log log = LogFactory.getLog(MassSearchingF1.class
	// .getName());
	/**
	 * |target| = true positives + false negatives <br>
	 * the count of target (or correct) items
	 */
	// private long target;
	private static double distance = 0.1;
	int topN = 3;
	static boolean testRankN = true;
	static int rankN = 1;
	static boolean testBinSize = false;
	static double screenSize = 0.25;
	// screenSize
	static boolean testNoise = false;
	static double noisePercentage = 0.5; // 1%
	static boolean showDetailInfo = true;
	private long truePositive;
	boolean isCosine=false; // false is dot product
	Search aSearch = new Search();
	static JDBCDataModel dataModelCosine = new PostgreSQLJDBCDataModel(
			DbConnector.getUnicarbDS(), "ms.ms_preferences_Cosine_test", "scan_id",
			"mz", "intensity_value", "timestamp");
	static JDBCDataModel dataModelLoglikehood = new PostgreSQLJDBCDataModel(
			DbConnector.getUnicarbDS(), "ms.ms_preferences_Loglikelihood_test",
			"scan_id", "mz", "intensity_value", "timestamp");

	void init() {
		selected = 0;
		// target = 0;
		truePositive = 0;
	}

	@SuppressWarnings("null")
	public List<Long> matchMS(int scan_id, int algorithm, int numberOfResult) {

		try {
			DataModel aReloadFromJDBCDataModelCosine = new ReloadFromJDBCDataModel(
					dataModelCosine);
			DataModel aReloadFromJDBCDataModelLoglikehood = new ReloadFromJDBCDataModel(
					dataModelLoglikehood);
			UserSimilarity cosineSimilarity = null;
			cosineSimilarity = new PearsonCorrelationSimilarity(
					aReloadFromJDBCDataModelCosine);

			UserSimilarity loglikehoodSimilarity = new LogLikelihoodSimilarity(
					aReloadFromJDBCDataModelLoglikehood);

			UserNeighborhood cosineNeighborhood = new NearestNUserNeighborhood(
					numberOfResult, cosineSimilarity,
					aReloadFromJDBCDataModelCosine);// aReloadFromJDBCDataModel);
			UserNeighborhood loglikehoodNeighborhood = new NearestNUserNeighborhood(
					numberOfResult, loglikehoodSimilarity,
					aReloadFromJDBCDataModelLoglikehood);// aReloadFromJDBCDataModel);
			// long[] cosineNeighbors =
			// cosineNeighborhood.getUserNeighborhood(scan_id);
			// long[] loglikehoodNeighbors =
			// loglikehoodNeighborhood.getUserNeighborhood(scan_id);
			Map<Long, Long> aHashMap = new HashMap<Long, Long>();
			List<Long> similarityArray = new ArrayList<Long>();
//			putScanID(scan_id, cosineSimilarity,
//					cosineNeighborhood.getUserNeighborhood(scan_id), aHashMap);
			putScanID(scan_id, loglikehoodSimilarity,
					loglikehoodNeighborhood.getUserNeighborhood(scan_id),
					aHashMap);
			if (isCosine)
				putScanID(scan_id, cosineSimilarity,
						cosineNeighborhood.getUserNeighborhood(scan_id), aHashMap);
			//Dot Product 
			else {
				try {
					List<List<String>> dotProctList = (new DotProduct())
							.doDotProductF1All(scan_id, numberOfResult);
					topN = numberOfResult;
					putCosineScanID(scan_id, aHashMap, dotProctList);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			long maxKey = getMaxGlycan(aHashMap);
			similarityArray.add(maxKey);
			return similarityArray;
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void putCosineScanID(int scan_id, Map<Long, Long> aHashMap,
			List<List<String>> dotProctList) throws Exception {
		int i =0;
		for (List<String> aStringList : dotProctList) {
			// scan_id
			i++;
			Float isdistance;
			if ((isdistance = Float.parseFloat(aStringList.get(1))) > distance){
				long tempSimilartiy = Long.parseLong(aSearch
						.getGlycanSequenceID(Long.parseLong(aStringList.get(0))).get(0).get(0));
				System.out.println("aNeighbor:" + isdistance + "  : Scan_id: " + scan_id + " :Glan_id: " + tempSimilartiy  + ":Dis:" +aStringList.get(1));
				if (aHashMap.containsKey(tempSimilartiy)) {
					
					aHashMap.put(tempSimilartiy,
							aHashMap.get(tempSimilartiy) + (long) (topN+1-i)*topN);
				} else {
					
						aHashMap.put(tempSimilartiy, (long) (topN+1-i)*topN);
				}				
//				aHashMap.put(tempSimilartiy, (long) 1);
			}
			System.out.println("");
			// sum value

		}
	}

	private long getMaxGlycan(Map<Long, Long> aHashMap) {
		long tempGlycan = 0;
		long maxKey =0;
		for (Long key : aHashMap.keySet()) {
			if (aHashMap.get(key) > tempGlycan) {
				maxKey = key;
				tempGlycan = aHashMap.get(key);
			}
		}
		System.out.println("Glycan: " + maxKey);
		return maxKey;
	}

	private void putScanID(int scan_id, UserSimilarity aSimilarity,
			long[] aNeighbors, Map<Long, Long> aHashMap) throws TasteException {
		if (aNeighbors.length == 0)
			return;
		int i =0;
//		System.out.println("___");
		for (long aNeighbor : aNeighbors) {
			i++;
			if (aSimilarity.userSimilarity(scan_id, aNeighbor) > distance) {
				try {
					
					long tempSimilartiy = Long.parseLong(aSearch
							.getGlycanSequenceID(aNeighbor).get(0).get(0));
					System.out.println("aNeighbor:" + aNeighbor + "  : Scan_id: " + scan_id + " :Glan_id: " + tempSimilartiy  + ":Dis:" + aSimilarity.userSimilarity(scan_id, aNeighbor));
					if (aHashMap.containsKey(tempSimilartiy)) {
						if (Math.round(aSimilarity.userSimilarity(scan_id, aNeighbor)*100)==100) // two items are equal
							aHashMap.put(tempSimilartiy,
									aHashMap.get(tempSimilartiy) + topN*topN);
						else
						aHashMap.put(tempSimilartiy,
								aHashMap.get(tempSimilartiy) + (long) (topN+1-i)*topN);
					} else {
						if (Math.round(aSimilarity.userSimilarity(scan_id, aNeighbor)*100)==100)
							aHashMap.put(tempSimilartiy,(long)topN*topN);
						else
							aHashMap.put(tempSimilartiy, (long) (topN+1-i)*topN);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	List<List<String>> getScanList(int reference_id) {
		Search aSearch = new Search();
		try {
			return aSearch.getScanList(reference_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private void doTest() {
		int referenceID = 871;
		int folderOne = 873;
		int folderTwo = 875;
		int folderThree = 881;
		// System.out.println("Folder: Algorithm : Precision : Recall : FMeasure:noisePercentage");
		for (int i = 1; i <= 1; i++) {
			if (showDetailInfo)
				System.out.println("Fold " + 871);
			FMeasure aFMeasure = new FMeasure();
			doCaculation(referenceID, 873, 875, 881, i, aFMeasure, topN);
			printF1Result(referenceID, i, aFMeasure);
		}

		referenceID = 873;
		// System.out.println("Algorithm : Precision : Recall : FMeasure");
		for (int i = 1; i <= 1; i++) {
			System.out.println("Fold " + 873);
			FMeasure aFMeasure = new FMeasure();
			doCaculation(referenceID, 871, 875, 881, i, aFMeasure, topN);
			printF1Result(referenceID, i, aFMeasure);
		}
		// System.out
		// .println(referenceID
		// +
		// "----------------------------------------------------------------------------------------------------");
		referenceID = 875;
		// System.out.println("Algorithm : Precision : Recall : FMeasure");
		for (int i = 1; i <= 1; i++) {
			System.out.println("Fold " + 875);
			FMeasure aFMeasure = new FMeasure();
			doCaculation(referenceID, 871, 873, 881, i, aFMeasure, topN);
			printF1Result(referenceID, i, aFMeasure);
		}
		// System.out
		// .println(referenceID
		// +
		// "----------------------------------------------------------------------------------------------------");
		referenceID = 881;
		// System.out.println("Algorithm : Precision : Recall : FMeasure");
		for (int i = 1; i <= 1; i++) {
			System.out.println("Fold " + 881);
			FMeasure aFMeasure = new FMeasure();
			doCaculation(referenceID, 871, 873, 875, i, aFMeasure, topN);
			printF1Result(referenceID, i, aFMeasure);
		}
		// System.out
		// .println(referenceID
		// +
		// "----------------------------------------------------------------------------------------------------");
	}

	private void printF1Result(int referenceID, int i, FMeasure aFMeasure) {

		if (testNoise)
			System.out.println(referenceID + "  ,   " + i + "  ,   "
					+ aFMeasure.getPrecisionScore() + "  ,   "
					+ aFMeasure.getRecallScore() + "   ,     "
					+ aFMeasure.getFMeasure() + " , "
					+ Math.round(noisePercentage * 100));
		else if (testBinSize)
			System.out.println(referenceID + "  ,   " + i + "  ,   "
					+ aFMeasure.getPrecisionScore() + "  ,   "
					+ aFMeasure.getRecallScore() + "   ,     "
					+ aFMeasure.getFMeasure() + " , "
					+ Math.round(screenSize * 100));
		else if (testRankN)
			System.out.println(referenceID + "  ,   " + i + "  ,   "
					+ aFMeasure.getPrecisionScore() + "  ,   "
					+ aFMeasure.getRecallScore() + "   ,     "
					+ aFMeasure.getFMeasure() + " , " + Math.round(rankN));
	}

	private void doCaculation(int referenceID, int folderOne, int folderTwo,
			int folderThree, int algorithm, FMeasure aFMeasure, int topN) {
		List<Long> references = null;
		List<Long> predictions = null;
		// List<Long> aLongArray = null;
		List<List<String>> aScanList = getScanList(referenceID);
		for (List aScan : aScanList) {
			if (showDetailInfo)
				System.out.println("Start..." + ":Glycan_ID is "
						+ aScan.get(0).toString() + ", Scan_ID is"
						+ aScan.get(1).toString());
			references = new ArrayList<Long>();
			predictions = new ArrayList<Long>();
			try {
				int scan_id = Integer.parseInt(aScan.get(1).toString());
				int glycan_sequence_id = Integer.parseInt(aScan.get(0)
						.toString());
				List<List<String>> referenceList = aSearch.getReferences(
						scan_id, glycan_sequence_id, folderOne, folderTwo,
						folderThree);
				if (referenceList.size() != 0) {
					// long[] aLong = matchMS(2836,0,2);
					references.add(Long.parseLong((String) referenceList.get(0)
							.get(0)));
//					System.out.println("Ref:" + referenceList.get(0).get(0));
					// for (List aReference : referenceList) {
					// if (aReference != null){
					// references.add(Long.parseLong((String) aReference
					// .get(0)));
					// // references.add(Long.parseLong((String) aReference
					// // .get(1)));
					// if (showDetailInfo) {
					// System.out.println("A reference glycan_ID is "
					// // for (List aReference : referenceList) {
					// if (aReference != null){
					// references.add(Long.parseLong((String) aReference
					// .get(0)));
					// // references.add(Long.parseLong((String) aReference
					// // .get(1)));
					// if (showDetailInfo) {
					// System.out.println("A reference glycan_ID is "
					// + aReference.get(0) + " and Scan_ID is "
					// + aReference.get(1) + ":");
					// }
					//
					// }
					//
					// } // end for + aReference.get(0) + " and Scan_ID is "
					// + aReference.get(1) + ":");
					// }
					//
					// }
					//
					// } // end for
					if (showDetailInfo) {
						System.out.println("------------------------");
//						predictions = printMatchingInfo(algorithm, scan_id, 5);
					}
					if (topN == 0) {
						if (referenceList.size() > 0) {
							predictions = matchMS(scan_id, algorithm,
									referenceList.size());
						} else {
							System.out.println("NO MATCHING:" + scan_id);
							// predictions = matchMS(scan_id, algorithm,
							// 3);
							// if (predictions != null){
							// for (long oneLongArray : predictions) {
							// references.add((long) 0);

							// }
							// }
						}
					} else
						// (topN!=0)
						if (referenceList.size() > 0) {
							predictions = matchMS(scan_id, algorithm, 4);
						} else {
							System.out.println("NO MATCHING:" + scan_id);
						}
						
					// if (predictions == null)
					// predictions = null;
					// else {
					// predictions = Longs.asList(aLongArray);
					// }
					aFMeasure.updateScores(references, predictions);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (showDetailInfo) {
				System.out.println("End...Scan_ID:" + aScan.get(1).toString()
						+ ":Glycan_ID:" + aScan.get(0).toString());
			}
		}
	}



	private List<Long> printMatchingInfo(int algorithm, int scan_id, int topN)
			throws Exception {
		List<Long> aLongArray;
		aLongArray = matchMS(scan_id, algorithm, topN);
		int i = 0;
		for (long aScan_Id : aLongArray) {
			System.out.println(++i + " matching  glycan_ID is "
					+ aSearch.getGlycanSequenceID(scan_id).get(0).get(0)
					+ " and predict glycan_ID is " + aScan_Id + ":");
		}
		return aLongArray;
	}

	public static void main(String[] args) throws Exception {
		Properties prop = new Properties();
		Prepare aPrepare = new Prepare();
		GlycanCombine2F1 aAccuracyF1 = new GlycanCombine2F1();
		// double screenSize = 5;

//		if (testNoise) {
//			while (noisePercentage < 1.01) {
//				screenSize = 1;
//				aPrepare.getReferencewithPer(screenSize, noisePercentage);
//				aAccuracyF1.doTest();
//				noisePercentage = noisePercentage + 0.01;
//			}
//		} else if (testBinSize) {
//			noisePercentage = 0.6;
//			while (screenSize < 10.1) {
//				aPrepare.getReferencewithPer(screenSize, noisePercentage);
//				aAccuracyF1.doTest();
//				screenSize = screenSize + 0.05;
//			}
//		} else if (testRankN) {
//			rankN = 2;
//			screenSize = 1.5;
//			while (rankN < 3) {
//				aPrepare.getReferencewithTopRowNumber(screenSize, rankN);
//				aAccuracyF1.doTest();
//				rankN = rankN + 1;
//
//			}
//		}
		aAccuracyF1.doTest();
		System.out.println("END!");
		// aFMeasure.updateScores(references, predictions);
	}
}