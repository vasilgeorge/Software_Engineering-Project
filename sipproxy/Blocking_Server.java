package gov.nist.sip.proxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Blocking_Server {

	private static Blocking_Server instance = null;
	protected Blocking_Server() {
	      // Exists only to defeat instantiation.		   		   
		    
	}
	public static Blocking_Server getInstance(){
	      if (instance == null) {
	         instance = new Blocking_Server();
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
	public boolean isBlocked(String username,String target) throws SQLException{
		SQL_connection sql_c=new SQL_connection();
		int i1 = username_validation(username);
		int i2 = username_validation(target);
		if (i1==400 && i2==400){
			ResultSet rs1=sql_c.SelectFromTable("users","user_id", "`username` = '"+username+"'");
			ResultSet rs2=sql_c.SelectFromTable("users","user_id", "`username` = '"+target+"'");
			int id1=0; int id2=0;
			if (rs1.next()) { //<-- IMPORTANT!
			    id1 = rs1.getInt(1);
			}
			if (rs2.next()) { //<-- IMPORTANT!
			    id2 = rs2.getInt(1);
			}
			ResultSet RSet= sql_c.SelectFromTable("blocking","blockID", "`blockerID` = '"+id1+"' and `blockedID` = '"+id2+"'");
			if (RSet==null){
				return false;
			}
			else if (RSet.next()){
				return true;
			}
			else {
			 return false;
			}
		}
		else{
			return false;
		}
	}
	
	public int blockUser(String username,String target) throws SQLException{
		SQL_connection sql_c=new SQL_connection();
		int i1 = username_validation(username);
		int i2 = username_validation(target);
		if (i1==400 && i2==400){
			Blocking_Server bs=new Blocking_Server();
			boolean b=bs.isBlocked(username,target);//check if target is already blocked
			if (b){
				return 400;
			}
			else{
				ResultSet rs1=sql_c.SelectFromTable("users","user_id", "`username` = '"+username+"'");
				ResultSet rs2=sql_c.SelectFromTable("users","user_id", "`username` = '"+target+"'");
				int id1=0; int id2=0;
				if (rs1.next()) { //<-- IMPORTANT!
				    id1 = rs1.getInt(1);
				}
				if (rs2.next()) { //<-- IMPORTANT!
				    id2 = rs2.getInt(1);
				}
				boolean i=sql_c.InsertToTable("blocking","`blockerID`,`blockedID`,`blockedName`", " '"+id1+"', '"+id2+"', '"+target+"' ");
				if (i){
					return 400;
				}
				else {
				 return 222;
				}
			}
		}
		else{
			return 222;
		}
	}
	
	public int unblockUser(String username,String target) throws SQLException{
		SQL_connection sql_c=new SQL_connection();
		int i1 = username_validation(username);
		int i2 = username_validation(target);
		if (i1==400 && i2==400){
			ResultSet rs1=sql_c.SelectFromTable("users","user_id", "`username` = '"+username+"'");
			ResultSet rs2=sql_c.SelectFromTable("users","user_id", "`username` = '"+target+"'");
			int id1=0; int id2=0;
			if (rs1.next()) { //<-- IMPORTANT!
			    id1 = rs1.getInt(1);
			}
			if (rs2.next()) { //<-- IMPORTANT!
			    id2 = rs2.getInt(1);
			}
			boolean i=sql_c.DeleteFromTable("blocking","`blockerID`='"+id1+"' and `blockedID`='"+id2+"'");
			if (i){
				return 400;
			}
			else {
			 return 222;
			}
		}
		else{
			return 222;
		}
	}
	
	public ArrayList getBlocklist(String username) throws SQLException {
		ArrayList blocklist = new ArrayList();
		if (username_validation(username)==400){
			SQL_connection sql_c = new SQL_connection();
			ResultSet rs1=sql_c.SelectFromTable("users","user_id", "`username` = '"+username+"'");
			int id1=0; 
			if (rs1.next()) { //<-- IMPORTANT!
			    id1 = rs1.getInt(1);
			}
			ResultSet resultSet = sql_c.SelectFromTable("blocking", "blockedName", "`blockerID` = '"+id1+"'");
			if (resultSet == null){
				return null;
			}
			else{
				try {
					while (resultSet.next()) {
								blocklist.add(resultSet.getString(1));
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
				return blocklist;			
			}
        }
		else{
			return null;
		}		
	}
	
	/*public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Blocking_Server bs=new Blocking_Server();
		int g=bs.blockUser("giwrgos", "alex");
		ArrayList al= bs.getBlocklist("giwrgos");
		System.out.println(al);
		//boolean i=bs.isBlocked("giwrgos", "dimos");
		//System.out.print(i);
	
	}*/

}
