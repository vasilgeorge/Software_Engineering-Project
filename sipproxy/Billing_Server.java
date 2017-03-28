package gov.nist.sip.proxy;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Billing_Server {

	private static Billing_Server instance = null;
	protected Billing_Server() {
	      // Exists only to defeat instantiation.		   		   
		    
	}
	public static Billing_Server getInstance(){
	      if (instance == null) {
	         instance = new Billing_Server();
	      }
	      return instance;
	}
	public int username_validation(String username) throws SQLException{
		SQL_connection sql_c=new SQL_connection();
		ResultSet RSet= sql_c.SelectFromTable("users","username", "`username` = '"+username+"'");
		if (RSet==null){
			return 222;
		}
		else if (RSet.next()){
			return 400;
		}
		else {
		 return 200;
		}
	}
	
	
	public boolean ChangeMembership(String user,String membership) throws SQLException {
		SQL_connection sql_c = new SQL_connection();
		ResultSet resultSet = sql_c.SelectFromTable("users", "username", "`username` = '"+user+"'");
		if (resultSet.next() ){
			sql_c.UpdateTable("users", "`membership`='"+membership+"'", "`username` = '"+user+"'");
			return true;
		}
		else{
			return false;
		}
	}
	/*the calleeID may change*/
	public boolean InsertCall(int callerID, int calleeID,Timestamp call_start,Timestamp call_end) throws SQLException{
		SQL_connection sql_c = new SQL_connection();
		ResultSet resultSet1 = sql_c.SelectFromTable("users", "username", "`user_id` = '"+callerID+"'");
		ResultSet resultSet2 = sql_c.SelectFromTable("users", "username", "`user_id` = '"+calleeID+"'");
		if (resultSet1.next() && resultSet2.next() ){
			boolean i=sql_c.InsertToTable("calls","`callerID`,`calleeID`,`call_start`,`call_end`", " '"+callerID+"', '"+calleeID+"', '"+call_start+"','"+call_end+"' ");
			if (i){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	public boolean call_start(int callerID, int calleeID,Timestamp call_start) throws SQLException{
		SQL_connection sql_c = new SQL_connection();
		ResultSet resultSet1 = sql_c.SelectFromTable("users", "username", "`user_id` = '"+callerID+"'");
		ResultSet resultSet2 = sql_c.SelectFromTable("users", "username", "`user_id` = '"+calleeID+"'");
		if (resultSet1.next() && resultSet2.next() ){
			boolean i=sql_c.InsertToTable("calls","`callerID`,`calleeID`,`call_start`", " '"+callerID+"', '"+calleeID+"', '"+call_start+"' ");
			if (i){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	public boolean call_end(int callerID, int calleeID,Timestamp call_end) throws SQLException{
		SQL_connection sql_c = new SQL_connection();
		ResultSet resultSet1 = sql_c.SelectFromTable("users", "username", "`user_id` = '"+callerID+"'");
		ResultSet resultSet2 = sql_c.SelectFromTable("users", "username", "`user_id` = '"+calleeID+"'");
		if (resultSet1.next() && resultSet2.next() ){
			boolean i=sql_c.UpdateTable("calls","`call_end`= '"+call_end+"' ", " `callerID`='"+callerID+"'and `calleeID`= '"+calleeID+"'");
			if (i){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	/*change the the computation of cost*/
	public boolean updateBill(int callerID,long time) throws SQLException{
		double new_bill=0;
		SQL_connection sql_c = new SQL_connection();
		ResultSet resultSet1 = sql_c.SelectFromTable("users", "*", "`user_id` = '"+callerID+"'");
		if (resultSet1.next()){
			String memb=resultSet1.getString(5);
			if (memb=="Premium"){
				new_bill= time*0.1;
			}
			else { 
				new_bill=time*0.5;
			}
			double tb= resultSet1.getFloat(6);
			tb=tb+new_bill;
			sql_c.UpdateTable("users", "`total_bill`='"+tb+"'", "`user_id` = '"+callerID+"'");
			return true;
		}
		else{
			return false;
		}
	}
	public double getBill(String username) throws SQLException{
		double bill=0;
		SQL_connection sql_c = new SQL_connection();
		ResultSet resultSet1 = sql_c.SelectFromTable("users", "*", "`username` = '"+username+"'");
		if (resultSet1.next()){
			bill=resultSet1.getDouble(6);
			return bill;
			
		}
		else{
			return bill;
		}
	}	
		

	
	/*public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Billing_Server bs=new Billing_Server();
		/*int h1=bs.get_userid("giwrgos");
		System.out.println(h1);*/
		
		/*java.util.Date javaDate = new java.util.Date();
	    long javaTime = javaDate.getTime();
	    java.sql.Timestamp start_time = new java.sql.Timestamp((long) javaTime); 
		boolean b=bs.call_start(2,4,start_time);
	    System.out.println(javaTime);
	    
	    int i;
	    //for(i=0;i<500000;i++){System.out.println("hi");}
	    
	    java.util.Date javaDate2 = new java.util.Date();
	    long javaTime2 = javaDate2.getTime();
	    java.sql.Timestamp end_time = new java.sql.Timestamp((long) javaTime2);
	    b=bs.call_end(2,4,end_time);
	    System.out.println(javaTime2);
	    long res=(javaTime2-javaTime)/1000;
	    System.out.println(res);
		
	}*/

}
