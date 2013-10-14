package au.edu.mq.cbms.unicarbkb.webservices.test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;
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

public class MassSearchingTestOnly {
	/**
	 * |selected| = true positives + false positives <br>
	 * the count of selected (or retrieved) items
	 */
	private long selected;
	private final static Logger LOGGER = Logger.getLogger(MassSearchingTestOnly.class .getName());

//	public static final Log log = LogFactory.getLog(MassSearchingF1.class
//			.getName());
	/**
	 * |target| = true positives + false negatives <br>
	 * the count of target (or correct) items
	 */
	private long target;
	private static double distance=0.5;
	int topN = 5;
	static double screenSize = 0.25;
	static int MAX_SCAN_ID = 100015;
	       //screenSize
	static double noisePercentage = 0; //1%
	static boolean showDetailInfo = true;
	private long truePositive;
	static boolean TESTALL = true;
	
	Search aSearch = new Search();
	static JDBCDataModel dataModel = new PostgreSQLJDBCDataModel(
			DbConnector.getUnicarbDS(), "ms.ms_preferences", "scan_id", "mz",
			"intensity_value", "timestamp");
	static JDBCDataModel dataModelAll = new PostgreSQLJDBCDataModel(
			DbConnector.getUnicarbDS(), "ms.ms_preferences_all", "scan_id", "mz",
			"intensity_value", "timestamp");

	void init() {
		selected = 0;
		target = 0;
		truePositive = 0;
	}

	@SuppressWarnings("null")
	public static List<Long> matchMS(int scan_id, int algorithm, int numberOfResult) {

		try {
			DataModel aReloadFromJDBCDataModel = new ReloadFromJDBCDataModel(
					dataModel);
			UserSimilarity similarity = null;
			// dataModel= new
			// PostgreSQLJDBCDataModel(DbConnector.getUnicarbDS(),
			// "ms.ms_preferences", "scan_id", "mz", "intensity_value",
			// "timestamp");
			if (algorithm == 1)
				similarity = new EuclideanDistanceSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 2)
				similarity = new EuclideanDistanceSimilarity(
						aReloadFromJDBCDataModel,Weighting.WEIGHTED);
			else if (algorithm == 3) //PearsonCorrelation=cosine similarity
				similarity = new PearsonCorrelationSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 4)
//				aReloadFromJDBCDataModel.setPreference(userID, itemID, value)
//				similarity = new PearsonCorrelationSimilarity(
//						aReloadFromJDBCDataModel,Weighting.WEIGHTED);
//			else if (algorithm == 5)
				similarity = new UncenteredCosineSimilarity(
						aReloadFromJDBCDataModel);
//			else if (algorithm == 6)
//				similarity = new UncenteredCosineSimilarity(
//						aReloadFromJDBCDataModel,Weighting.WEIGHTED);
			else if (algorithm == 5)
				similarity = new LogLikelihoodSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 6)
				similarity = new CityBlockSimilarity(aReloadFromJDBCDataModel);
			else if (algorithm == 7)
				similarity = new SpearmanCorrelationSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 8)
				similarity = new TanimotoCoefficientSimilarity(
						aReloadFromJDBCDataModel);
			else
				similarity = new EuclideanDistanceSimilarity(
						aReloadFromJDBCDataModel);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(
					numberOfResult, similarity, aReloadFromJDBCDataModel);// aReloadFromJDBCDataModel);
			long[] neighbors = neighborhood.getUserNeighborhood(scan_id);
			if (neighbors.length == 0)
				return null;
			 List<Long> similarityArray = new ArrayList<Long>();
			  
			 for (long aNeighbor:neighbors){
				if (similarity.userSimilarity(scan_id, aNeighbor)>distance){
					similarityArray.add(aNeighbor);
					if (showDetailInfo){
						 System.out.println(//"Scan ID: " + scan_id +
						 "      aNeighbor Similarity:" + aNeighbor + " userSimilarity: " +
						 similarity.userSimilarity(scan_id, aNeighbor));
						 System.out.println("");
					}
				}
			 }
			return similarityArray;
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("null")
	public static List<Long> matchMSAll(int scan_id, int algorithm, int numberOfResult) {
		 List<Long> similarityArray = new ArrayList<Long>();
		try {
			DataModel aReloadFromJDBCDataModel = new ReloadFromJDBCDataModel(
					dataModelAll);
			UserSimilarity similarity = null;
			// dataModel= new
			// PostgreSQLJDBCDataModel(DbConnector.getUnicarbDS(),
			// "ms.ms_preferences", "scan_id", "mz", "intensity_value",
			// "timestamp");
			if (algorithm == 1)
				similarity = new EuclideanDistanceSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 2)
				similarity = new EuclideanDistanceSimilarity(
						aReloadFromJDBCDataModel,Weighting.WEIGHTED);
			else if (algorithm == 3) //PearsonCorrelation=cosine similarity
				similarity = new PearsonCorrelationSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 4)
//				aReloadFromJDBCDataModel.setPreference(userID, itemID, value)
//				similarity = new PearsonCorrelationSimilarity(
//						aReloadFromJDBCDataModel,Weighting.WEIGHTED);
//			else if (algorithm == 5)
				similarity = new UncenteredCosineSimilarity(
						aReloadFromJDBCDataModel);
//			else if (algorithm == 6)
//				similarity = new UncenteredCosineSimilarity(
//						aReloadFromJDBCDataModel,Weighting.WEIGHTED);
			else if (algorithm == 5)
				similarity = new LogLikelihoodSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 6)
				similarity = new CityBlockSimilarity(aReloadFromJDBCDataModel);
			else if (algorithm == 7)
				similarity = new SpearmanCorrelationSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 8)
				similarity = new TanimotoCoefficientSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 9) {
				List<List<String>> dotProctList = null;
				try {
					dotProctList = (new DotProduct())
							.doDotProduct(scan_id, numberOfResult);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (List<String> aStringList : dotProctList) {
					// scan_id
					long isdistance;
					if ((isdistance = Long.parseLong(aStringList.get(0))) > distance)
						similarityArray.add(isdistance);
					// sum value

				}
			}
			else
				similarity = new EuclideanDistanceSimilarity(
						aReloadFromJDBCDataModel);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(
					numberOfResult, similarity, aReloadFromJDBCDataModel);// aReloadFromJDBCDataModel);
			long[] neighbors = neighborhood.getUserNeighborhood(scan_id);
			if (neighbors.length == 0)
				return null;

			  
			 for (long aNeighbor:neighbors){
				if (similarity.userSimilarity(scan_id, aNeighbor)>distance){
					similarityArray.add(aNeighbor);
					if (showDetailInfo){
						 System.out.println(//"Scan ID: " + scan_id +
						 "      aNeighbor Similarity:" + aNeighbor + " userSimilarity: " +
						 similarity.userSimilarity(scan_id, aNeighbor));
						 System.out.println("");
					}
				}
			 }
			return similarityArray;
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		System.out.println("Folder: Algorithm : Precision : Recall : FMeasure:noisePercentage");
		for (int i = 1; i <= 8; i++) {
			if (showDetailInfo) System.out.println("Algorithm " + i);
			FMeasure aFMeasure = new FMeasure();
			doCaculation(referenceID, 873, 875, 881, i, aFMeasure,topN);
			printF1Result(referenceID, i, aFMeasure);
		}
		System.out
				.println(referenceID
						+ "----------------------------------------------------------------------------------------------------");
		 referenceID = 873;
		 System.out.println("Algorithm : Precision : Recall : FMeasure");
		 for (int i = 1; i <= 8; i++) {
		 FMeasure aFMeasure = new FMeasure();
		 doCaculation(referenceID, 871, 875, 881, i, aFMeasure,topN);
		 printF1Result(referenceID, i, aFMeasure);
		 }
		 System.out
		 .println(referenceID
		 +
		 "----------------------------------------------------------------------------------------------------");
		 referenceID = 875;
		 System.out.println("Algorithm : Precision : Recall : FMeasure");
		 for (int i = 1; i <= 8; i++) {
		 FMeasure aFMeasure = new FMeasure();
		 doCaculation(referenceID, 871, 873, 881, i, aFMeasure,topN);
		 printF1Result(referenceID, i, aFMeasure);
		 }
		 System.out
		 .println(referenceID
		 +
		 "----------------------------------------------------------------------------------------------------");
		 referenceID = 881;
		 System.out.println("Algorithm : Precision : Recall : FMeasure");
		 for (int i = 1; i <= 8; i++) {
		 FMeasure aFMeasure = new FMeasure();
		 doCaculation(referenceID, 871, 873, 875, i, aFMeasure,topN);
		 printF1Result(referenceID, i, aFMeasure);
		 }
		 System.out
		 .println(referenceID
		 +
		 "----------------------------------------------------------------------------------------------------");
	}

	private void printF1Result( int referenceID, int i, FMeasure aFMeasure) {
		
		System.out.println
		(referenceID + "  ,   " + i + "  ,   " + aFMeasure.getPrecisionScore()
				+ "  ,   " + aFMeasure.getRecallScore() + "   ,     "
				+ aFMeasure.getFMeasure() + " , " + noisePercentage);
		// System.out.println(i + " , " + aFMeasure.getPrecisionScore());
		// System.out.println(" getRecallScore: " + aFMeasure.getRecallScore());
		// System.out.println(" getFMeasure: " + aFMeasure.getFMeasure());
		// System.out.println("END ------" + referenceID);
	}

	private void doCaculation(int referenceID, int folderOne, int folderTwo,
			int folderThree, int algorithm, FMeasure aFMeasure, int topN) {
		List<Long> references = null;
		List<Long> predictions = null;
//		List<Long> aLongArray = null;
		List<List<String>> aScanList = getScanList(referenceID);
		for (List aScan : aScanList) {
			if (showDetailInfo)
				System.out.println("Start..." + ":Glycan_ID is " + aScan.get(0).toString() + ", Scan_ID is" + aScan.get(1).toString()
						);
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
					for (List aReference : referenceList) {
						if (aReference != null){
							references.add(Long.parseLong((String) aReference
									.get(1)));
							if (showDetailInfo) {
								System.out.println("A reference glycan_ID is "
										+ aReference.get(0) + " and Scan_ID is "
										+ aReference.get(1) + ":");
							}
						
						}

					} // end for
					if (showDetailInfo) {
						System.out.println("------------------------");
						predictions = printMatchingInfo(algorithm, scan_id,5);
					}
					if (topN==0){
						if (referenceList.size()>0)
							predictions = matchMS(scan_id, algorithm,
								referenceList.size());
						else {
							predictions = matchMS(scan_id, algorithm,
									3);
							if (predictions != null){
								for (long oneLongArray : predictions) {
									references.add((long) 0);
									
								}
							}
						}
					} else  //(topN!=0)
						predictions = matchMS(scan_id, algorithm,
								topN);
//					if (predictions == null)
//						predictions = null;
//					else {
//						predictions = Longs.asList(aLongArray);
//					}
					aFMeasure.updateScores(references, predictions);
				}
				// else
				// System.out.println("Empty element: scan_id is  " + scan_id +
				// " and glycan_sequence_id is " + glycan_sequence_id);
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
  private void testOneMass(){
	  for (	int j =100000; j <MAX_SCAN_ID +1; j++) {
		try {
			for (int i = 1; i <= 9; i++) {
				if (showDetailInfo) System.out.println("Algorithm " + i);
				FMeasure aFMeasure = new FMeasure();

				if (showDetailInfo){
					try {
						
						System.out.println("--" + i + "--");
						
						printMatchingInfo(i,j,5);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				printF1Result(j, i, aFMeasure);
			}
		} catch (Exception e) {
			System.out.println ("error at scan_ID:" + j);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

  }
	private List<Long> printMatchingInfo(int algorithm, int scan_id, int topN)
			 {
		List<Long> aLongArray;
		if (TESTALL) 
			aLongArray = matchMSAll(scan_id, algorithm, topN);
		else
			aLongArray = matchMS(scan_id, algorithm, topN);
		int i = 0;
		try {
			for (long aScan_Id : aLongArray) {
				System.out.println(++i + " matching  glycan_ID is "
						+ aSearch.getGlycanSequenceID(aScan_Id).get(0).get(0)
						+ " and Scan_ID is " + aScan_Id + ":");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aLongArray;
	}

	
	public static void main(String[] args) throws Exception {
		TESTALL = true;
		Properties prop = new Properties();
//	        MyLogger.setup();
//	        LOGGER.setLevel(Level.INFO);
//		LOGGER.severe("asdsfsd");
//		LOGGER.info("aaa");
		Prepare aPrepare = new Prepare();
		MassSearchingTestOnly aMassSearchingF1 = new MassSearchingTestOnly();
		if (!TESTALL){
		aPrepare.getReferencewithPer(screenSize,0);
		aPrepare.insertTestData(screenSize);
		} else {
		//testAll
			noisePercentage = 0.4;
			screenSize = 0.25;
		aPrepare.getReferencewithPerAll(screenSize,noisePercentage);
		
		aPrepare.insertTestDataAll(screenSize,noisePercentage);
		}
		
		aMassSearchingF1.testOneMass();
		// aFMeasure.updateScores(references, predictions);
	}
}