package com.iclick.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class HttpURLConnectionPost {
	public static void main(String[] args) throws IOException {
		readContentFromPost();
	}
	
	public  static void readContentFromPost() throws IOException{
		URL  postUrl=new URL("http://www.wangzhiqiang87.cn");
		
//		URL  postUrl=new URL("http://www.baidu.com");
		
		
	    //打开链接
		HttpURLConnection  connection=(HttpURLConnection) postUrl.openConnection();
		 // 设置是否向connection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		 // 默认是 GET方式
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		
		// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
        // 进行编码
		connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		
		connection.connect();
		DataOutputStream  out=new DataOutputStream(connection.getOutputStream());
		String content="account=" + URLEncoder.encode("一个大肥人", "UTF-8");
		content +="&pswd="+URLEncoder.encode("两个个大肥人", "UTF-8");
		out.writeBytes(content);
		out.flush();
		out.close();
		
		BufferedReader  reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("d:\\wilson.zhou\\Desktop\\baidu.html"))));
		
		String str=null;
		while((str=reader.readLine())!=null){
			writer.append(str);
			writer.newLine();
		}
		writer.flush();
	    writer.close();
	    reader.close();
		
	}
	
}
