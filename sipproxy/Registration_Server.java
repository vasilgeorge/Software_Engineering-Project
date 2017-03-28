package gov.nist.sip.proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Registration_Server {
	private static Registration_Server instance = null;
	protected Registration_Server() {
	      // Exists only to defeat instantiation.		   		   
		    
	}
	
	public static Registration_Server getInstance(){
	      if (instance == null) {
	         instance = new Registration_Server();
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
	
	public int get_userid(String username) throws SQLException{
		SQL_connection sql_c=new SQL_connection();
		ResultSet RSet= sql_c.SelectFromTable("users","*", "`username` = '"+username+"'");
		if (RSet==null){
			return -1;
		}
		else if (RSet.next()){
			return RSet.getInt(1);
		}
		else {
		 return -1;
		}
	}
	
	public int register_user (String username, String password,String email,String membership) throws SQLException{
		SQL_connection sql_c=new SQL_connection();
		boolean b =sql_c.InsertToTable("users", "`username`, `passwd`, `email`, `membership`,`total_bill`",
				" '"+username+"', '"+password+"','"+email+"','"+membership+"','0'");
		if (b){
			return 222;
		}
		else{
			return 200;
		}
	}
	
	public int sign_in(String username,String password) throws SQLException{
		int i = username_validation(username);
		if (i==400){
			SQL_connection sql_c=new SQL_connection();
			ResultSet RSet= sql_c.SelectFromTable("users","username",
					"`username` = '"+username+"' and `passwd` ='"+password+"'");
			if (RSet==null){
				return 222;
			}
			else if (RSet.next()){
				return 200;
			}
			else {
				return 401;
			}
		}
		else if (i==200){
			return 401;
		}
		else {
			return 222;
		}
	}
	
	/*public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Registration_Server sqlh=new Registration_Server();
		int i=sqlh.sign_in("dimos","zaxariadhs"); 
		System.out.println("" + i);
	}*/

}
