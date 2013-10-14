package au.edu.mq.cbms.unicarbkb.webservices.db;

import java.util.List;;

/**
 * A Search class is a class operate data search feature
 * <P>
 * @author Jingyu ZHANG 
 * @version 0.99
 */
public class Search extends DbConnector {

	public List<List<String>> getIDList(String id) throws Exception {
		String s_sql = "select glycan_sequence_id from core.glycan_sequence";
		return executeQuery(s_sql);
	}
	public List<List<String>> getSequencesfromExt(String s_sql) throws Exception {
//		String s_sql = "select glycan_sequence_id from core.glycan_sequence";
		return executeQuery(s_sql);
	}
	public int setMSPreference(int scanId, int mz, double intensity) throws Exception {
//		if (intensity>= 0.6){
		String s_sql = "insert into ms.ms_preferences (scan_id,mz,intensity_value) values (" + scanId + "," + mz + "," + intensity + ")";
		return executeUpdate(s_sql);
//		}
//		return 0;
	}
	public int removeMSPreference(int scan_id) throws Exception {
		String s_sql = "delete from  ms.ms_preferences where scan_id=" + scan_id;
		return executeUpdate(s_sql);
	}
	public List<List<String>> getGlycanSequenceID(long result_scan_id) throws Exception {
		String s_sql = "select glycan_sequence_id from hplc.lcmucin where scan_id=" + result_scan_id;
		return executeQuery(s_sql);
	}
	public List<List<String>> getScanList(int reference_id) throws Exception {
		String s_sql = "select glycan_sequence_id, scan_id from hplc.lcmucin as lc where lc.acquisition_id=" + reference_id + " and lc.evidence=" + reference_id; // scan_id=" + reference_id;
		return executeQuery(s_sql);
	}
	public List<List<String>> getReferences(int scan_id, int glycan_sequence_id, int folderOne,int folderTwo, int folderThree) throws Exception {

		String s_sql = "select glycan_sequence_id, scan_id,acquisition_id  from hplc.lcmucin as lc where scan_id<>" + scan_id + " and glycan_sequence_id =" + glycan_sequence_id 
						+ " and  ((lc.acquisition_id=" + folderOne + " and lc.evidence=" + folderOne + ") or (lc.acquisition_id=" +folderTwo + " and lc.evidence="
						+ folderTwo +" )  or (lc.acquisition_id=" + folderThree	 + " and lc.evidence=" + folderThree + "))";
		return executeQuery(s_sql);
	}
	public List<List<String>> getReferences(long scan_id) throws Exception {
		String s_sql = "select glycan_sequence_id, scan_id from hplc.lcmucin as lc where scan_id=" + scan_id;
		return executeQuery(s_sql);
	}
	public List<List<String>> getScanId(long glycan_sequence_id) throws Exception {
		String s_sql = "select scan_id from hplc.lcmucin as lc where glycan_sequence_id=" + glycan_sequence_id ;
		return executeQuery(s_sql);
	}
	public List<List<String>> getReferencesList(long scan_id, int resultNumber) throws Exception {
		String s_sql = "select v1.scan_id, sum(v1.intensity_value * v2.intensity_value) sum_value " +
				"from ms.ms_preferences v1, (select * from ms.ms_preferences v2 where scan_id  = " + scan_id + ") as v2 " +
				"where  v1.mz = v2.mz and v1.scan_id <> " + scan_id + " group by v1.scan_id order by sum_value desc limit " + resultNumber;
		return executeQuery(s_sql);
	}
	public List<List<String>> getReferencesListAll(long scan_id, int resultNumber) throws Exception {
		String s_sql = "select v1.scan_id, sum(v1.intensity_value * v2.intensity_value) sum_value " +
				"from ms.ms_preferences v1, (select * from ms.ms_preferences v2 where scan_id  = " + scan_id + ") as v2 " +
				"where  v1.mz = v2.mz and v1.scan_id <> " + scan_id + " group by v1.scan_id order by sum_value desc limit " + resultNumber;
		return executeQuery(s_sql);
	}
	public List<List<String>> getReferencesListF1All(long scan_id, int resultNumber) throws Exception {
		String s_sql = "select v1.scan_id, sum(v1.intensity_value * v2.intensity_value) sum_value " +
				"from ms.ms_preferences_Loglikelihood_test v1, (select * from ms.ms_preferences_Loglikelihood_test v2 where scan_id  = " + scan_id + ") as v2 " +
				"where  v1.mz = v2.mz and v1.scan_id <> " + scan_id + " group by v1.scan_id order by sum_value desc limit " + resultNumber;
		return executeQuery(s_sql);
	}
}
