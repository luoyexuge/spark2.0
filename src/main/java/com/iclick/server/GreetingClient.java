package com.iclick.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
//Socket 客户端实例
public class GreetingClient {
	public static void main(String[] args) throws UnknownHostException, IOException {
		String serverName=args[0];
		int port=Integer.parseInt(args[1]);
		
		System.out.println("connect to "+serverName+"on port"+port);
		
		Socket client=new Socket(serverName,port);
		
		System.out.println("just connected to "+client.getRemoteSocketAddress());
		
		
		 OutputStream outToServer=client.getOutputStream();
		 DataOutputStream out=new DataOutputStream(outToServer);
		
		 out.writeUTF("hello from"+client.getLocalSocketAddress());
		 
		 InputStream  inFromServer=client.getInputStream();
		 DataInputStream  in=new  DataInputStream(inFromServer);
		 System.out.println("server says"+in.readUTF());
		 client.close();
		 
		 
	}
}
