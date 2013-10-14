package au.edu.mq.cbms.unicarbkb.webservices.ext;
//following imports are include the eurocarb*jar collection
import java.util.List;

import org.eurocarbdb.dataaccess.core.seq.SubstructureQuery;
import org.eurocarbdb.sugar.SugarSequence;
import au.edu.mq.cbms.unicarbkb.webservices.db.Search;

public class ReduceEnd {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		List<List<String>> rs = geSequences("RES\n1b:o-dglc-HEX-0:0|1:aldi\n2b:b-dgal-HEX-1:5\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dgal-HEX-1:5\nLIN\n1:1o(4+1)2d\n2:2o(3+1)3d\n3:3d(2+1)4n\n4:3o(3+1)5d\n");
		for (int i = 0; i < rs.size(); i++) {
			System.out.println(rs.get(i).get(0));

		}
	}

	private static List<List<String>> geSequences(String aSequence) {
		SugarSequence seq = new SugarSequence(aSequence);
		SubstructureQuery query = new SubstructureQuery( seq.getSugar() );
		String queryTask = query.getQueryString();
		System.out.println( "this is the query string: " + queryTask);
		Search aSearch = new Search();
		try {
			return aSearch.getSequencesfromExt(queryTask);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return null;
		}
	}
}
