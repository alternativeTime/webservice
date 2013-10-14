package au.edu.mq.cbms.unicarbkb.webservices.db;

import java.sql.SQLException;

public class Prepare extends DbConnector {
//	public int getReference(double screenSize) throws Exception {
//		executeUpdate("delete FROM  ms.ms_preferences");
//		String s_sql = "";//"insert into ms.ms_preferences select scan_id,round(cast(mz_value as numeric)*2,0)/2 as mz, max(intensity_value) as intensity_value  "
//						//+ "  from ms.ms_library_all group by scan_id,mz order by  scan_id,mz";
//		
//		s_sql = "insert into ms.ms_preferences " +
//				"select scan_id," + screenSize + "*100*(round(cast(mz_value as numeric)/" + screenSize +",0)) as mz, max(intensity_value) as intensity_value  " +
//				"from ms.ms_library_all group by scan_id,mz order by  scan_id,mz";
//		return executeUpdate(s_sql);
//	}
	public int getReferencewithTopRowNumber(double screenSize, int topNumber) throws Exception {
		executeUpdate("delete FROM  ms.ms_preferences");
		String s_sql = "insert into ms.ms_preferences" +
				"		select scan_id, mz, max(intensity_value) from (" +
					"		select scan_id, mz, intensity_value, row_number() over (partition by scan_id order by intensity_value desc) as rank" +
					"		from (" +
					"				select scan_id," + screenSize + "*100*(round(cast(mz_value as numeric)/" + screenSize + ",0)) as mz, " +
//					"               max(intensity_value) as intensity_value  " + //before normalize
					"               intensity_value/(max(intensity_value) over (partition by scan_id)) as intensity_value  " + //after normalize
					"								 from ms.ms_library_all " +
					"		) as a" +
				"		) as t" +
				"		 where rank<=" + topNumber + " group by scan_id, mz order by scan_id,mz";;

		return executeUpdate(s_sql);
	}
	public int getReferencewithTopDenseRank(double screenSize, int topNumber) throws Exception {
		executeUpdate("delete FROM  ms.ms_preferences");
		String s_sql = "insert into ms.ms_preferences" +
				"		select scan_id, mz, max(intensity_value) from (" +
					"		select scan_id, mz, intensity_value, dense_rank() over (partition by scan_id order by intensity_value desc) as rank" +
					"		from (" +
					"				select scan_id," + screenSize + "*100*(round(cast(mz_value as numeric)/" + screenSize + ",0)) as mz, " +
//					"               max(intensity_value) as intensity_value  " + //before normalize
					"               intensity_value/(max(intensity_value) over (partition by scan_id)) as intensity_value  " + //after normalize
					"								 from ms.ms_library_all " +
					"		) as a" +
				"		) as t" +
				"		 where rank<=" + topNumber + " group by scan_id, mz order by scan_id,mz";;

		return executeUpdate(s_sql);
	}
	public int getReferencewithTopRank(double screenSize, int topNumber) throws Exception {
		executeUpdate("delete FROM  ms.ms_preferences");
		String s_sql = "insert into ms.ms_preferences" +
				"		select scan_id, mz, max(intensity_value) from (" +
					"		select scan_id, mz, intensity_value, rank() over (partition by scan_id order by intensity_value desc) as rank" +
					"		from (" +
					"				select scan_id," + screenSize + "*100*(round(cast(mz_value as numeric)/" + screenSize + ",0)) as mz, " +
//					"               max(intensity_value) as intensity_value  " + //before normalize
					"               intensity_value/(max(intensity_value) over (partition by scan_id)) as intensity_value  " + //after normalize
					"								 from ms.ms_library_all " +
					"		) as a" +
				"		) as t" +
				"		 where rank<=" + topNumber + " group by scan_id, mz order by scan_id,mz";;

		return executeUpdate(s_sql);
	}
	public int getReferencewithPer(double screenSize, double noisePercentage) throws Exception {
		executeUpdate("delete FROM  ms.ms_preferences");
		String s_sql = "insert into ms.ms_preferences" +
				" select scan_id, mz, max (per) as intensity_value " +
				"from (" +  //after normalize
//				"select scan_id, mz, max (intensity_value) as intensity_value from (" +   ////before normalize
					"select scan_id, " + screenSize + "*100*(round(cast(mz_value as numeric)/" + screenSize + ")) as mz, " +
					"intensity_value, intensity_value/(max(intensity_value) over (partition by scan_id)) as per " +
					"from ms.ms_library_all)" +
				" as t " +
				"where per>" + noisePercentage + " group by scan_id, mz order by scan_id,mz";
//		System.out.println(s_sql);
		return executeUpdate(s_sql);
	}	
	public int getReferencewithPerAll(double screenSize, double noisePercentage) throws Exception {
		executeUpdate("delete FROM  ms.ms_preferences_all");
		String s_sql = "insert into ms.ms_preferences_all" +
				" select scan_id, mz, max (per) as intensity_value " +
				"from (" +  //after normalize
//				"select scan_id, mz, max (intensity_value) as intensity_value from (" +   ////before normalize
					"select scan_id, " + screenSize + "*100*(round(cast(mz_value as numeric)/" + screenSize + ")) as mz, " +
					"intensity_value, intensity_value/(max(intensity_value) over (partition by scan_id)) as per " +
					"from ms.ms_alldata)" +
				" as t " +
				"where per>" + noisePercentage + " group by scan_id, mz order by scan_id,mz";
//		System.out.println(s_sql);
		return executeUpdate(s_sql);
	}		
	public int insertTestData(double screenSize) throws SQLException{
		String s_sql = "insert into ms.ms_preferences  " +
				"select 100000, " + screenSize + "*100*(round(cast(mz as numeric)/" + screenSize + ")) as mz," +
						" intensity/(select max(intensity) from ms.blind) from ms.blind";
		return executeUpdate(s_sql);
	}
	
	public int insertTestDataAll(double screenSize, double noisePercentage) throws SQLException{
		String s_sql = "insert into ms.ms_preferences_all  " +
				" select scan_id, mz, max (per) as intensity_value " +
				"from (" +  //after normalize
				"select scan_id, " + screenSize + "*100*(round(cast(mz as numeric)/" + screenSize + ")) as mz," +
						" intensity/(max(intensity) over (partition by scan_id)) as per " +
						" from ms.blind)" +
						" as t " +
						"where per>" + noisePercentage + " group by scan_id, mz order by scan_id,mz";
		return executeUpdate(s_sql);
	}
	public int insertTestDataAllTop(double screenSize, int topNumber, String algorithmTable) throws SQLException{
//		String s_sql = "insert into ms.ms_preferences_all  " +
//				" select scan_id, mz, max (per) as intensity_value " +
//				"from (" +  //after normalize
//				"select scan_id, " + screenSize + "*100*(round(cast(mz as numeric)/" + screenSize + ")) as mz," +
//						" intensity/(max(intensity) over (partition by scan_id)) as per " +
//						" from ms.blind)" +
//						" as t " +
//						"where per>" + noisePercentage + " group by scan_id, mz order by scan_id,mz";
//		return executeUpdate(s_sql);
		
		
		String s_sql = "insert into ms.ms_preferences_cosine"  +
				"		select scan_id, mz, max(intensity_value) from (" +
					"		select scan_id, mz, intensity_value, rank() over (partition by scan_id order by intensity_value desc) as rank" +
					"		from (" +
					"				select scan_id," + screenSize + "*100*(round(cast(mz_value as numeric)/" + screenSize + ",0)) as mz, " +
//					"               max(intensity_value) as intensity_value  " + //before normalize
					"               intensity_value/(max(intensity_value) over (partition by scan_id)) as intensity_value  " + //after normalize
					"								 from ms.blind " +
					"		) as a" +
				"		) as t" +
				"		 where rank<=" + 3 + " group by scan_id, mz order by scan_id,mz";;

		 executeUpdate(s_sql);
		s_sql = "insert into ms.ms_preferences_loglikelihood"  +
				"		select scan_id, mz, max(intensity_value) from (" +
					"		select scan_id, mz, intensity_value, rank() over (partition by scan_id order by intensity_value desc) as rank" +
					"		from (" +
					"				select scan_id," + screenSize + "*100*(round(cast(mz_value as numeric)/" + screenSize + ",0)) as mz, " +
//					"               max(intensity_value) as intensity_value  " + //before normalize
					"               intensity_value/(max(intensity_value) over (partition by scan_id)) as intensity_value  " + //after normalize
					"								 from ms.blind " +
					"		) as a" +
				"		) as t" +
				"		 where rank<=" + 15 + " group by scan_id, mz order by scan_id,mz";;

		return executeUpdate(s_sql);
	}
//	public int doNormalize(double min, double max) throws Exception {
//
//		String s_sql = "";//"insert into ms.ms_preferences select scan_id,round(cast(mz_value as numeric)*2,0)/2 as mz, max(intensity_value) as intensity_value  "
//						//+ "  from ms.ms_library_all group by scan_id,mz order by  scan_id,mz";
//		
//		s_sql = "insert into ms.ms_preferences select scan_id," + "" + "*100*(round(cast(mz_value as numeric)/" + "" +",0)) as mz, max(intensity_value) as intensity_value  " +
//				"from ms.ms_library_all group by scan_id,mz order by  scan_id,mz";
//		return executeUpdate(s_sql);
//	}
}
