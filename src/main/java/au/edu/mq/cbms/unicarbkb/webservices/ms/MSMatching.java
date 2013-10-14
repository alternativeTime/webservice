package au.edu.mq.cbms.unicarbkb.webservices.ms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import au.edu.mq.cbms.unicarbkb.webservices.Algorithm.DotProduct;
import au.edu.mq.cbms.unicarbkb.webservices.db.DbConnector;
import au.edu.mq.cbms.unicarbkb.webservices.db.Search;

public class MSMatching {
	static JDBCDataModel dataModel = new PostgreSQLJDBCDataModel(
			DbConnector.getUnicarbDS(), "ms.ms_preferences", "scan_id", "mz",
			"intensity_value", "timestamp");
	private  double distance = 0.5;
	 Search aSearch = new Search();
	 int topN = 3;
	 Map<Long, Long> aHashMap = new HashMap<Long, Long>();
	 List<Long> similarityArray = new ArrayList<Long>();

	public  long matchMS(int scan_id, int algorithm, int numberOfResult) {
		if (algorithm > 9 || algorithm < 0)
			algorithm = 0;
		try {
			DataModel aReloadFromJDBCDataModel = new ReloadFromJDBCDataModel(
					dataModel);
			UserSimilarity similarity = null;
//			List<Long> similarityArray = new ArrayList<Long>();
//			algorithm = 0;
			// dataModel= new
			// PostgreSQLJDBCDataModel(DbConnector.getUnicarbDS(),
			// "ms.ms_preferences", "scan_id", "mz", "intensity_value",
			// "timestamp");
			if (algorithm == 1)
				similarity = new EuclideanDistanceSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 2)
				similarity = new PearsonCorrelationSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 3)
				similarity = new UncenteredCosineSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 4)
				similarity = new LogLikelihoodSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 5)
				similarity = new CityBlockSimilarity(aReloadFromJDBCDataModel);
			else if (algorithm == 6)
				similarity = new SpearmanCorrelationSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 7)
				similarity = new TanimotoCoefficientSimilarity(
						aReloadFromJDBCDataModel);
			else if (algorithm == 8) {
				try {
					List<List<String>> dotProctList = (new DotProduct())
							.doDotProductAll(scan_id, numberOfResult);
					for (List<String> aStringList : dotProctList) {
						// scan_id
						return Long.parseLong(aStringList.get(0));
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			} else { // for dot product + loglilikelyhood function
				return returnDotProductwithLogLikelihood(scan_id, numberOfResult,aReloadFromJDBCDataModel);

			}
			if (algorithm != 9 || algorithm != 0) {
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(
						numberOfResult, similarity, aReloadFromJDBCDataModel);// aReloadFromJDBCDataModel);
				long[] neighbors = neighborhood.getUserNeighborhood(scan_id);
				if (neighbors.length == 0)
					return 0;
				System.out.println(neighbors[0]);
				return neighbors[0];
			}
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	private  void putDotScanID(int scan_id, Map<Long, Long> aHashMap,
			List<List<String>> dotProctList) throws Exception {
		int i = 0;		
		
		for (List<String> aStringList : dotProctList) {
			// scan_id
			i++;
			Float isdistance;
			if ((isdistance = Float.parseFloat(aStringList.get(1))) > distance) {
				long tempSimilartiy = Long
						.parseLong(aSearch
								.getGlycanSequenceID(
										Long.parseLong(aStringList.get(0)))
								.get(0).get(0));
				System.out.println("aNeighbor:" + isdistance + "  : Scan_id: "
						+ scan_id + " :Glan_id: " + tempSimilartiy + ":Dis:"
						+ aStringList.get(1));
				if (aHashMap.containsKey(tempSimilartiy)) {
					if (Math.round(isdistance*100)==100) // two items are equal
						aHashMap.put(tempSimilartiy,
								aHashMap.get(tempSimilartiy) + topN*topN);
					else
					aHashMap.put(tempSimilartiy,
							aHashMap.get(tempSimilartiy) + (long) (topN+1-i)*topN);
				} else {
					if (Math.round(isdistance*100)==100)
						aHashMap.put(tempSimilartiy,(long)topN*topN);
					else
						aHashMap.put(tempSimilartiy, (long) (topN+1-i)*topN);
				}
				// aHashMap.put(tempSimilartiy, (long) 1);
			}
//			System.out.println("");
			// sum value

		}
	}

	private  int getMaxGlycan(Map<Long, Long> aHashMap) {
		long tempGlycan = 0;
		long maxKey = 0;
		for (Long key : aHashMap.keySet()) {
			if (aHashMap.get(key) > tempGlycan) {
				maxKey = key;
				tempGlycan = aHashMap.get(key);
			}
		}
		try {
			return Integer.parseInt(aSearch.getScanId(maxKey).get(0).get(0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Glycan: " + maxKey);
		return 0;
	}
	private  void putScanID(int scan_id, UserSimilarity aSimilarity,
			long[] aNeighbors, Map<Long, Long> aHashMap) throws TasteException {
		if (aNeighbors.length == 0)
			return;
		int i =0;
		System.out.println("___");
		for (long aNeighbor : aNeighbors) {
			i++;
			if (aSimilarity.userSimilarity(scan_id, aNeighbor) > distance) {
				updateScore(scan_id, aSimilarity, aHashMap, i, aNeighbor);
			}
		}
	}

	private  void updateScore(int scan_id, UserSimilarity aSimilarity,
			Map<Long, Long> aHashMap, int i, long aNeighbor) {
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
	private  int returnDotProductwithLogLikelihood(int scan_id,
			int numberOfResult,DataModel aReloadFromJDBCDataModel) {

		try {
			UserSimilarity loglikehoodSimilarity = new LogLikelihoodSimilarity(
					aReloadFromJDBCDataModel);
			UserNeighborhood loglikehoodNeighborhood = new NearestNUserNeighborhood(
					numberOfResult, loglikehoodSimilarity,
					aReloadFromJDBCDataModel);
			List<List<String>> dotProctList = (new DotProduct())
					.doDotProductAll(scan_id, numberOfResult);
			topN = numberOfResult;
			putDotScanID(scan_id, aHashMap, dotProctList);
			putScanID(scan_id, loglikehoodSimilarity,
					loglikehoodNeighborhood.getUserNeighborhood(scan_id),
					aHashMap);
			for (List<String> aStringList : dotProctList) {
				// scan_id
				long isdistance;
				if ((isdistance = Long.parseLong(aStringList.get(0))) > distance) {
					long tempSimilartiy = Long.parseLong(aSearch
							.getGlycanSequenceID(isdistance).get(0).get(0));
					System.out.println("aNeighbor:" + isdistance
							+ "  : Scan_id: " + scan_id + " :Glan_id: "
							+ tempSimilartiy + ":Dis:" + aStringList.get(1));
				}
				System.out.println("");
				// sum value
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (int) getMaxGlycan(aHashMap);
		
	}

	public static void main(String[] args) throws Exception {

		long a = Math.round(1418.7 * 2) * 10 / 2;
		System.out.println(a);
		long b = a * 10 / 2;
		MSMatching aMSMatching = new MSMatching();
		aMSMatching.matchMS(10000, 0, 1);
	}
}
