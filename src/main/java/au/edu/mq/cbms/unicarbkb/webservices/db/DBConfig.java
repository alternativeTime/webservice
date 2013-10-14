package au.edu.mq.cbms.unicarbkb.webservices.db;
/**
 * A DBConfig class is just a configer, it contain the configurable data
 * for the using of DBConnection.
 * <P>
 * @author JingYu
 * @version 0.99
 */
public class DBConfig extends Object {
	public static String POSTGRESQL = "org.postgresql.Driver";
    public static int iMaxFieldSize = 16000;  // the max width for the field
    public static int iMaxRows = 65535;       // the max number we can fetch at on time
    public static int iMaxCursorNum = 100;    // the max number of the open cursors
  public DBConfig() {
    super(); 
  }
  
 public static void  getDerbyConn(){
    String driver = "org.postgresql.Driver";
    String protocol = "jdbc:postgresql://localhost:5432/unicarblatest";

 }
}