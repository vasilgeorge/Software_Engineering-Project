package net.java.sip.communicator.gui;

import java.io.*;
import java.net.*;

public class Client {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		Client cl=new Client();
		cl.run();

	}

	public void run() throws Exception{
		Socket sock=new Socket("192.168.1.71",444);
		PrintStream ps=new PrintStream(sock.getOutputStream());
		ps.println("105-alex-SipUser");
		
		InputStreamReader ir=new InputStreamReader(sock.getInputStream());
		BufferedReader br=new BufferedReader(ir);
		String message=br.readLine();
		System.out.println(message);
	}
}
