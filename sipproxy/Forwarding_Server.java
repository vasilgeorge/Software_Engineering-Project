package gov.nist.sip.proxy;

import gov.nist.sip.proxy.Blocking_Server;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Forwarding_Server {
	private static Forwarding_Server instance = null;
	protected Forwarding_Server() {
	      // Exists only to defeat instantiation.		   		   
		    
	}
	
	public static Forwarding_Server getInstance(){
	      if (instance == null) {
	         instance = new Forwarding_Server();
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
	
	public String isForwarded(String username) throws SQLException{
		SQL_connection sql_c=new SQL_connection();
		if( username_validation(username)==400){
			ResultSet rs1=sql_c.SelectFromTable("users","user_id", "`username` = '"+username+"'");
			int id1=0;
			if (rs1.next()) { //<-- IMPORTANT!
			    id1 = rs1.getInt(1);
			}
			ResultSet RSet=sql_c.SelectFromTable("forwarding","*", "`user_id` = '"+id1+"'");
			if (RSet==null){
				return null;
			}
			else if (RSet.next()){	
				return RSet.getString(4);
			}
			else {
			 return null;
			}
		}
		else{
			return null;
		}
	}
	
	public boolean unForward(String username) throws SQLException{
		SQL_connection sql_c=new SQL_connection();
		if( username_validation(username)==400){
			ResultSet rs1=sql_c.SelectFromTable("users","user_id", "`username` = '"+username+"'");
			int id1=0;
			if (rs1.next()) { //<-- IMPORTANT!
			    id1 = rs1.getInt(1);
			}
			boolean b=sql_c.DeleteFromTable("forwarding", "`user_id` = '"+id1+"'");
			if (b==true){
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
	
	public boolean ForwardTo(String username,String target) throws SQLException{
		SQL_connection sql_c=new SQL_connection();
		Blocking_Server blServer = Blocking_Server.getInstance();
		if(( username_validation(username)==400)&& (username_validation(target)==400)){
			boolean block=blServer.isBlocked(target,username);
			if (block==false){
				ResultSet rs1=sql_c.SelectFromTable("users","user_id", "`username` = '"+username+"'");
				ResultSet rs2=sql_c.SelectFromTable("users","user_id", "`username` = '"+target+"'");
				int id1=0; int id2=0;
				if (rs1.next()) { //<-- IMPORTANT!
				    id1 = rs1.getInt(1);
				}
				if (rs2.next()) { //<-- IMPORTANT!
				    id2 = rs2.getInt(1);
				}
			
				String br=isForwarded(username);
				if (br==null){
					boolean b=sql_c.InsertToTable("forwarding", "`user_id`,`forwardToID`,`redirectedTo` "," '"+id1+"','"+id2+"','"+target+"' ");
					if (b==true){
						return true;
					}
					else {
					 return false;
					}
				}
				else{
					boolean b=sql_c.UpdateTable("forwarding", "`forwardToID`='"+id2+"',`redirectedTo`='"+target+"' " ," `user_id`='"+id1+"' ");
					if (b==true){
						return true;
					}
					else {
					 return false;
					}
				}
			}else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	public String getFinal(String callee) throws SQLException{
		Set<String> cycles = new HashSet<String>();
		String target=callee;
		cycles.add(target);
		String res=isForwarded(target);
		while (target !=null){
			target=isForwarded(res);
			if (target!=null){
				res=target;
				System.out.println(res);
			}
			if (cycles.add(target)==false){
				res=null;
				System.out.println("break");
				break;
			}
		}
		return res;
	}
	
	
	/*public static void main(String[] args) throws SQLException {
	// TODO Auto-generated method stub
	Forwarding_Server sqlh=new Forwarding_Server();
	boolean bb=sqlh.unForward("giwrgos");
	//System.out.println(bh);
	//boolean b=sqlh.ForwardTo("giwrgos","vasilis");
	//boolean b=sqlh.unForward("alex");
	//String b=sqlh.isForwarded("giwrgos");
	//String bh=sqlh.getFinal("dimos");
	//System.out.println(bh);
	}*/

}
