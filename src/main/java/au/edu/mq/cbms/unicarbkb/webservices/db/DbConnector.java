package au.edu.mq.cbms.unicarbkb.webservices.db;

/**
 * A DBConnection class is a general database connection utility.
 * It will be enhanced in data pool and LOB support in the furture.
 * Recently, it is just a simple data fecth tool for RDBMS
 * <P>
 * @author Jingyu ZHANG 
 * @version 0.99
 */
import java.sql.*;
import java.util.*;

import org.postgresql.ds.PGPoolingDataSource;

public class DbConnector 
{
	static PGPoolingDataSource source = null;
  private static Connection conn = null;
  private Statement stmt = null;

  /**
   * Constructor
   */
  public DbConnector() {
    super() ;
    try{
      LoadJdbcDriver();
    }
    catch (Throwable e){
      close();
    }
  }


  /**
   * Function close() release the used resouces
   * @author Jingyu ZHANG
   * @version 1.0
   * @param none
   * @return none
   */
  void close()
  {
    try{
      if(stmt != null){
        //stmt.close(); //--> left it to the garbage collector!!
        stmt = null;
      }
      if( conn!= null && !conn.isClosed()){
        conn.close();
        conn = null;
      }
    }
    catch(Throwable e)
    {
      e.printStackTrace();
    //  System.err.println("Exception occured in DBConnection::close() " + " " +e.getMessage());
    }
  }



  /**
   * Function LoadJdbcDriver() load the driver identified by
   * the DBconfig class.
   * @author Jing Yu
   * @version 1.0
   * @param none
   * @return none
   * @exception SQLException
   */

  private void LoadJdbcDriver() throws SQLException
  {
    try{
      Class.forName(DBConfig.POSTGRESQL).newInstance();
    }
    catch(Throwable e1){
        e1.printStackTrace();
 //       System.err.println("Exception occured in DBConnection::LoadJdbcDriver() "+ " " +e1.getMessage());
    }
  }
public static PGPoolingDataSource getUnicarbDS(){
	if (source != null) return source;
	source = new PGPoolingDataSource();
	source.setDataSourceName("msDS");
//	source.setServerName("115.146.93.194");
	source.setServerName("localhost");
	source.setDatabaseName("unicarblatest");
//	source.setDatabaseName("unicarblatest");
	source.setUser("postgres");
	source.setPassword("postgres");
	source.setMaxConnections(10);
	return source;
}

  /**
   * Function getConnection() return the connection with the DB
   * @author Jing Yu
   * @version 1.0
   * @param none
   * @return Connection
   * @exception SQLException
   */
  private Connection getConnection() throws SQLException
  {
    if(conn != null && !conn.isClosed()) return conn;

    try{
//      conn = DriverManager.getConnection("jdbc:derby:testdb;create=true");
//		String userName = "postgres";
//		String password = "postgres";
//		conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/unicarblatest",userName,password);

		conn = getUnicarbDS().getConnection();
//      System.out.println("begin connection in " + new java.util.Date());
    	
      return conn;
    }
    catch(SQLException e){
      e.printStackTrace();
      System.err.println("Exception occured in DBConnection::getConnection() "+ e.getErrorCode() + " " +e.getMessage());
      throw e;
    }
  }

  /**
   * Function getStatement() return the statement with the DB
   * @author Jing Yu
   * @version 1.0
   * @param none
   * @return Statement
   * @exception SQLException
   */
  private Statement getStatement() throws SQLException
  {
    try{
      stmt = getConnection().createStatement();
      stmt.setMaxFieldSize(DBConfig.iMaxFieldSize);
      stmt.setMaxRows(DBConfig.iMaxRows);
      this.setAutoCommit(true);
      return stmt;
    }
    catch(SQLException e){
      e.printStackTrace();
      System.err.println("Exception occured in DBConnection::getStatement() "+ e.getErrorCode() + " " +e.getMessage());
      throw e;
    }
  }

  /**
   * Function commit()
   * @author Jing Yu
   * @version 1.0
   * @param none
   * @return boolean
   */
   public boolean commit()
   {
      try{
        getConnection().commit();
      }
      catch(Throwable e){
        e.printStackTrace();
        System.err.println("Exception occured in DBConnection::commit() "+ " " +e.getMessage());
        return false;
      }
      return true;
   }

  /**
   * Function rollback()
   * @author Jing Yu
   * @version 1.0
   * @param none
   * @return boolean
   */
   public boolean rollback()
   {
      try{
        getConnection().rollback();
      }
      catch(Throwable e){
        e.printStackTrace();
        System.err.println("Exception occured in DBConnection::rollback() " + " " +e.getMessage());
        return false;
      }
      return true;
   }


  /**
   * Function setAutoCommit(),the system default value is true (auto commit)
   * @author Jing Yu
   * @version 1.0
   * @param boolean bAutoCommit
   * @return boolean
   */
   public boolean setAutoCommit(boolean bAutoCommit)
   {
      try{
        getConnection().setAutoCommit(true);//bAutoCommit);
      }
      catch(Throwable e){
        e.printStackTrace();
        System.err.println("Exception occured in DBConnection::setAutoCommit() " + " " +e.getMessage());
        return false;
      }
      return true;
   }

  /**
   * Function isSelect(sql) check whether the sql is query string or not
   * @author Jing Yu
   * @version 1.0
   * @param String sql
   * @return boolean isQueryStatement
   */
  protected boolean isSelect(String psql) {
	  String sql = psql.trim().toUpperCase();
    return sql.indexOf("SELECT ")!=0 ? false : true;
  }



  /**
   * Function execute(sql) is the lower function
   * <P><font color=red>pay close attention,this function DO NOT support C/BLOB recently!</font></P>
   * @author Jing Yu
   * @version 1.0
   * @param String sql
   * @return Object(qurey-->ArrayList[][],update/delete/insert/ddl-->integer)
   */
   private Object execute(String sql)
   {
      ResultSet rs = null;
      ArrayList<List<String>> vResult = null;

      // for debug target only
//      System.out.println("running sql command "+sql);

      try{
        if(isSelect(sql)){
          rs = getStatement().executeQuery(sql);
          int iColumnCount = rs.getMetaData().getColumnCount();
          vResult = new ArrayList<List<String>>();
          
          while(rs.next()){
              ArrayList<String> vTempArrayList = new ArrayList<String>();
              for(int i=0; i<iColumnCount;i++){
                // be carefull, we do not support the clob/blob recently
//                Object oTemp = rs.getObject(i+1);
                String sTemp = null;
//                if(oTemp instanceof oracle.sql.CLOB || oTemp instanceof oracle.sql.BLOB){
//                  sTemp = "DO NOT SUPPORT CLOB/BLOB TEMPERIALY";
//                  System.err.println(sTemp);
//                }
//                else
                  sTemp = rs.getString(i+1);
                vTempArrayList.add(sTemp == null ? "" : sTemp); // remove the null value
              }
              vResult.add(vTempArrayList);
          }
          rs.close();
          stmt.close(); // close statement
          return vResult;
        }
        else{ // insert/delete/update/ddl
          if(stmt != null)
            stmt.close(); // close statement
          return new Integer(getStatement().executeUpdate(sql));
        }
      }
      catch(SQLException e){
        e.printStackTrace();
        System.err.println("Exception occured in DBConnection::execute(sql) "+ e.getErrorCode() + " " +e.getMessage());
        return null;
      }
   }

  /**
   * Function excuteQuery(sql) is the callable function
   * attention: the result is 2D ArrayList (ArrayList[][])-->just like table
   * @author Jing Yu
   * @version 1.0
   * @param String sql
   * @return ArrayList
   * @exception SQLException
   */
   public List<List<String>> executeQuery(String sql) throws SQLException
   {
      return (List<List<String>>)execute(sql);
   }
   //Jusy for Bayesian
   public void executeBayesianQuery (ArrayList<List<String>> base, String sql, boolean isLast) throws SQLException{
	   ResultSet rs = null;
       rs = getStatement().executeQuery(sql);
       int iColumnCount = rs.getMetaData().getColumnCount();
       boolean isCreate = false;
       if(isCreate){

           while(rs.next()){
               ArrayList vTempArrayList = new ArrayList();
               for(int i=0; i<iColumnCount;i++){
                 String sTemp = null;
                   sTemp = rs.getString(i+1);
                 vTempArrayList.add(sTemp == null ? "" : sTemp); // remove the null value
               }
               base.add(vTempArrayList);
           }
           rs.close();
           stmt.close();
       }else {
    	   	if(isLast){
    	   		addLastWorkData(rs, base, iColumnCount);
    	   		//System.out.println(base.size());
    	   	}else{
    	   	   addWorkData(rs, base, iColumnCount);
    	   	}
           }
         }


private void addLastWorkData(ResultSet rs, ArrayList<List<String>> vResult, int iColumnCount) throws SQLException {
	int cycle =0;
//	int baseprev=0;
//	int arrayWork1[][] = new int[2][20];
	//performance could be improve later
//	 getFirst20(rs,arrayWork1);
	   while(rs.next()){
		   
	       ArrayList<String> vTempArrayList = (ArrayList<String>)vResult.get(cycle);
//	       for(int i=0; i<iColumnCount;i++){
	          double  preProb = Double.parseDouble((String)vTempArrayList.get(2));
	          double sTemp = rs.getDouble(3);//get prob
	           //ToDo 100 should be changed to 1000
//	           int sx = preProb*sTemp/100;
//	           System.out.println((preProb*sTemp/100 + "") + (String)vTempArrayList.elementAt(1));
	           (vResult.get(cycle)).set(2,preProb*sTemp/100 + "");
	           try {
				getStatement().executeUpdate(
						   "INSERT INTO WORK1 VALUES(" + rs.getInt(1) +" ,'" + (String)vTempArrayList.get(1) + "'," + preProb*sTemp/100 +")");
	           } catch (SQLException e) {
				// TODO Auto-generated catch block
	        	   e.printStackTrace();
	           }
//	       }
//	       vResult.addElement(vTempArrayList);
	       cycle++;
	   }
	   rs.close();
	   stmt.close(); // close statement
}
//  private int[][] getFirst20(ResultSet rs,int[][] arrayWork1){	  
//	  while(rs.next()){ 
//		  ArrayList vTempArrayList = (ArrayList)vResult.elementAt(cycle);
//      for(int i=0; i<iColumnCount;i++){
//         int  preProb = Integer.parseInt((String)vTempArrayList.elementAt(2));
//          int sTemp = rs.getInt(3);//get prob
//          //ToDo 100 should be changed to 1000
//          sx = preProb*sTemp/100;
//          System.out.println((Object)(sx + ""));
//          arrayWork1[0][cycle]
//          vTempArrayList.setElementAt((Object)(sx + ""),2);
//      }
//      vResult.addElement(vTempArrayList);
//      cycle++;
//      if (cycle>20) return arrayWork1;
//  }
//	  return null;
//  }
private void addWorkData(ResultSet rs, ArrayList<List<String>> vResult, int iColumnCount) throws SQLException {
	int cycle =0;
	   while(rs.next()){
	       ArrayList<String> vTempArrayList = (ArrayList<String>)vResult.get(cycle);
//	       for(int i=0; i<iColumnCount;i++){
	          double  preProb = Double.parseDouble((String)vTempArrayList.get(2));
	          double sTemp = rs.getDouble(3);//get prob
	           //ToDo 100 should be changed to 1000
//	           System.out.println(((preProb*sTemp/100) + ""));
	           ((ArrayList<String>)vResult.get(cycle)).set(2,((preProb*sTemp/100) + ""));
//	           System.out.println((String)((ArrayList)vResult.elementAt(cycle)).elementAt(2) + "   " + vResult.size() );
	           //                       vTempArrayList.setElementAt()
//                     vTempArrayList.addElement(sTemp == null ? "" : sTemp); // remove the null value
//	       }
//	       vResult.addElement(vTempArrayList);
	       cycle++;
	   }
	   rs.close();
	   stmt.close(); // close statement
}
  /**
   * Function excuteUpdate(sql) is the callable function
   * attention: the result is Integer,the jdbc return value
   * @author Jing Yu
   * @version 1.0
   * @param String sql
   * @return Integer
   * @exception SQLException
   */
   public int executeUpdate(String sql) throws SQLException
   {
      return ((Integer)execute(sql)).intValue();
   }
	public void findDiseases(String pSign)
	{
		
	}
	public PGPoolingDataSource getPoolingDataSource(){
		PGPoolingDataSource source = new PGPoolingDataSource();
		String userName = "postgres";
		String password = "postgres";
		source.setDataSourceName("msDS");
		source.setServerName("localhost");
		source.setDatabaseName("unicarblatest");
		source.setUser("postgres");
		source.setPassword("postgres");
		source.setMaxConnections(10);
		return source;
		
	}
  /**
   * main is the test function
   */
  public static void main(String[] args) {

    try{
      DbConnector dBConnection = new DbConnector();
      dBConnection.setAutoCommit(true);

      List<List<String>> rs = dBConnection.executeQuery("select * from users");
      
      for(int i=0;i < rs.size();i++){
    	  
        List<String> vRow = rs.get(i);
        for(int j=0;j<vRow.size();j++){
          String value = (String)vRow.get(j);
         System.out.println("table["+i+"]["+j+"]="+value+"\t");
       }
       System.out.println("");
      }
//      dBConnection.executeUpdate("insert into test values(33,'ddd')");
    }
    catch(SQLException e){
    }
  }

}

