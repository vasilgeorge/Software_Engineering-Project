package gov.nist.sip.proxy;

import java.awt.HeadlessException;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import gov.nist.sip.proxy.Registration_Server;
import gov.nist.sip.proxy.Billing_Server;
import gov.nist.sip.proxy.Blocking_Server;
import gov.nist.sip.proxy.Forwarding_Server;

/*Codes for services
 *100=Log in
 *101=Register
 *102=Change Bill
 *103=Show Bill
 *104=Block
 *105=Unblock
 *106=Show Blocked
 *107=Forward
 *108=Unforward
 *We have to change manually all the socket ports from the clients!!!!
 *=>AuthenticationSplash,RegistrationSplash,BillingSplash,BlockingSplash,ForwardingSplash run() methods in the end
 */
public class Service_Provider {

	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		/*String string = "100-giwrgos-1324";
		String[] parts = string.split("-");
		String part1 = parts[0]; // 004
		String part2 = parts[1];
		
		String part3 = parts[2];
		System.out.println(part1);
		System.out.println(part2);
		System.out.println(part3);*/
		Service_Provider Server = new Service_Provider();		
		Server.run();		

		
	}

	public void run() throws Exception {
		
		ServerSocket SrvSocket=new ServerSocket(444);
		while(true){ //it would have been better with threads
			Socket socket=SrvSocket.accept();
			InputStreamReader IR=new InputStreamReader(socket.getInputStream());
			BufferedReader BR =new BufferedReader(IR);
			String message=BR.readLine();
			System.out.println(message);
			
			if(message!=null){
				String[] parts = message.split("-");
				String part1 = parts[0]; 
				String part2 = parts[1];
				String reply="Message Received!";
				if(part1.equals("100")){
					String part3 = parts[2];
					Registration_Server Server = Registration_Server.getInstance();
					int i=Server.sign_in(part2, part3);
	                if (i!=200){
	                    reply="false";
	                }
	            }	
				if(part1.equals("101")){
					String part3 = parts[2];
					String part4 = parts[3];
					String part5 = parts[4];
					Registration_Server Server = Registration_Server.getInstance();
					int i;
		            try {
						 i= Server.register_user(part2, part3, part4, part5);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				if(part1.equals("102")){
					String part3 = parts[2];
					Billing_Server bServer = Billing_Server.getInstance();
		            try {
						if(bServer.ChangeMembership(part2, part3)) {
							reply= "Success";
						}
						else {
							 reply="Failed";
						}
		            }catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(part1.equals("103")){
					Billing_Server bServer = Billing_Server.getInstance();
		            try {
		            	double i=bServer.getBill(part2);
						if(i>=0) {
							reply=""+i;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(part1.equals("104")){
					String part3 = parts[2];
					Blocking_Server bServer = Blocking_Server.getInstance();
						if( bServer.blockUser(part2, part3) == 400) {
							reply="400";
						}
				}
				if(part1.equals("105")){
					String part3 = parts[2];				
					Blocking_Server bServer = Blocking_Server.getInstance();
					if(bServer.unblockUser(part2, part3)==400){
						reply="400";
					}
				}
				if(part1.equals("106")){
					Blocking_Server bServer = Blocking_Server.getInstance();
		        	ArrayList blist = null;
		        	try {
						blist = bServer.getBlocklist(part2);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	String fString="";
		        	for(int i =0;i<blist.size();i++){
		        		fString=fString+"  "+blist.get(i);
		        	}
		        	reply=fString;
				}
				if(part1.equals("107")){
					String part3 = parts[2];
					Forwarding_Server fServer = Forwarding_Server.getInstance();
					if(fServer.ForwardTo(part2,part3)) {
						reply="Success";
					}
					
				}
				if(part1.equals("108")){
					Forwarding_Server fServer = Forwarding_Server.getInstance();
					if(fServer.unForward(part2)) {
						reply="Success";
					}
				}
				PrintStream PS=new PrintStream(socket.getOutputStream());
				PS.println(reply);
				
			}
		}
	} 
}
