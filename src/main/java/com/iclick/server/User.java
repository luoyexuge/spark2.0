package com.iclick.server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class User {
	private String name;
	
	public  User read(DataInputStream dis) throws IOException{
		this.name=dis.readUTF();
		return this;
	}
	public void write(DataOutputStream dos) throws IOException{
		dos.writeUTF(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
