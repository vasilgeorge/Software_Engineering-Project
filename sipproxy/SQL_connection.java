package gov.nist.sip.proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class SQL_connection {
	Connection connect;
	/*final static String driverClass = "com.mysql.jdbc.Driver";*/
	final static String url = "jdbc:mysql://127.0.0.1:3306/";
	final static String db = "talkiedb";
	final static String password = "lowfunded93";
	final static String userName ="root" ;
	 
	public SQL_connection() {
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection (url+db+"?"+"user="+userName+"&password="+password);
			System.out.println("DB Connection Worked");
		} catch (Exception e){
			e.printStackTrace();
		} 
	}
	
	public void close_connection(){
		try{
			connect.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public ResultSet SelectFromTable(String table,String fields, String constraints) 
	{	

		 try {
            String sql_query = "SELECT "+fields+" FROM "+table+" WHERE "+constraints;
            PreparedStatement stmt = connect.prepareStatement(sql_query);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet;
		 }
		 catch (Exception e) {
		 	e.printStackTrace(); // "handle" errors
		 	return null;
		 }

	}

	public boolean InsertToTable(String table, String fields, String values)
	{
		try {
	        // Prepare a statement to update a record
	        String sql_query = "INSERT INTO `"+table+"` ("+fields+") VALUES ("+values+")";
	        PreparedStatement stmt = connect.prepareStatement(sql_query);
	        int result = stmt.executeUpdate();	        
	        if (result>0){
	        	System.out.println("Insert is completed corectly.");
	        	return true;
	        }
	        else{
	        	System.out.println("Error on Insert into "+table+" fields "+fields+" values "+values);
	        	return false;
	        }
	    } 
		catch (SQLException e) {
	    	System.out.println("update error:" + e.getMessage());
	        return false;
	    }

	}
	
	public boolean UpdateTable(String table, String set, String constraints)
	{
		try {
	        String sql_query = "UPDATE "+table+" SET "+set+" WHERE "+constraints;	      
	        PreparedStatement stmt = connect.prepareStatement(sql_query);	        
	        int result = stmt.executeUpdate();	        
	        if (result>0){
	        	System.out.println("Update is completed corectly.");
	        	return true;
	        }
	        else{
	        	System.out.println("Error on Update "+table+" set "+set+" where "+constraints);
	        	return false;
	        }
	    } 
		catch (SQLException e) {
	    	System.out.println("update error:" + e.getMessage());
	    	return false;
	    }

	}
	
	public boolean DeleteFromTable(String table, String constraints)
	{
		try {
            String sql_query = "DELETE FROM "+table+" WHERE "+constraints;	        
	        PreparedStatement stmt = connect.prepareStatement(sql_query);	        
	        int result = stmt.executeUpdate();	        
	        if (result>0){
	        	System.out.println("Delete is completed corectly.");
	        	return true;
	        }
	        else{
	        	System.out.println("Error on Delete from"+table+" where "+constraints);
	        	return false;
	        }
	    }catch (SQLException e) {
	    	System.out.println("update error:" + e.getMessage());
	    	return false;
	    }

	}
	
	/*public static void main(String[] args)throws java.lang.InterruptedException {
		// TODO Auto-generated method stub
		SQL_connection sql_c = new SQL_connection();
		//boolean i = sql_c.InsertToTable("users", "`username`, `passwd`, `email`, `membership`,`total_bill`",
			//	 "'vasilis', '123456', 'mail1@mail.com', 'Bronze','123'");
		//boolean j = sql_c.DeleteFromTable("users", "`username` = 'vasilis'");
		boolean i = sql_c.UpdateTable("users", "`passwd` = '1234567'",
				 "`username` = 'vasilis '");
	}*/

}
