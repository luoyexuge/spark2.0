package com.iclick.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

//Socket 服务端实例
public class GreetingServer extends Thread {
	private ServerSocket serverSocket;

	public GreetingServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.serverSocket.setSoTimeout(10000);
	}

	public void run() {
		while (true) {
			try {
				System.out.println("wainting for client on port"
						+ serverSocket.getLocalPort() + "....");
				Socket server = serverSocket.accept();
				System.out.println("just connect to "
						+ server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(
						server.getInputStream());
				System.out.println(in.readUTF());

				DataOutputStream out = new DataOutputStream(
						server.getOutputStream());
				out.writeUTF("thank you for connexting to"
						+ server.getLocalSocketAddress() + "\ngoodbye");
				server.close();

			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	public static void main(String[] args) throws IOException {
		int port=Integer.parseInt(args[0]);
		Thread t=new GreetingServer(port);
		t.start();
	}

}
