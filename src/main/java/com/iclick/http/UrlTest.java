package com.iclick.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

public class UrlTest {
	
	public static void main(String[] args) throws IOException {
		URL  url  =new URL("http://www.baidu.com");
		System.out.println(url.getAuthority());
		System.out.println(url.getPort());
		System.out.println(url.getHost());
	    
	
		BufferedReader  buff=new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("d:\\wilson.zhou\\Desktop\\baidu.html"))));
		
		String str=null;
		while((str=buff.readLine())!=null){
			writer.append(str);
			writer.newLine();
		}
		writer.flush();
	    writer.close();
	    buff.close();
		
		
	}


}
