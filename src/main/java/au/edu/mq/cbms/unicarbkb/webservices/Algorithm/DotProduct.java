package au.edu.mq.cbms.unicarbkb.webservices.Algorithm;

import java.util.List;
import java.util.Map;

import au.edu.mq.cbms.unicarbkb.webservices.db.Search;

public class DotProduct {
	Search aSearch = new Search();
	 public List<List<String>> doDotProduct(long scan_id,int numberOfResult) throws Exception{
		return aSearch.getReferencesList(scan_id, numberOfResult);
	}
	 public List<List<String>> doDotProductAll(int scan_id,int numberOfResult) throws Exception{
		return aSearch.getReferencesListAll(scan_id, numberOfResult);
	}
	 public List<List<String>> doDotProductF1All(int scan_id,int numberOfResult) throws Exception{
		return aSearch.getReferencesListF1All(scan_id, numberOfResult);
	}
}
