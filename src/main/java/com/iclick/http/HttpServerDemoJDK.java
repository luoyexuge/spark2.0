package com.iclick.http;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.List;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
@SuppressWarnings("restriction")
public class HttpServerDemoJDK {
	public static void main(String[] args) throws IOException {
		InetSocketAddress addr = new InetSocketAddress("192.168.118.16", 8082);
		System.out.println(addr.getAddress());
	
		HttpServer server = HttpServer.create(addr, 0);
		server.createContext("/", new MyHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server is listening on port 8082");

	}
}

@SuppressWarnings("restriction")
class MyHandler implements HttpHandler {

	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		System.out.println("处理新请求:" + requestMethod);
//		  URI uri= exchange.getRequestURI();
//		  System.out.println(uri.getRawQuery());
//	      Map<String, String> params = parseRawQuery(uri.getRawQuery());
		
		if (requestMethod.equalsIgnoreCase("GET")) {
			Headers reponseheaders = exchange.getResponseHeaders();
			reponseheaders.set("Content-Type", "text/plain");
			exchange.sendResponseHeaders(200, 0);

			/*
			 OutputStream responseBody = exchange.getResponseBody();  
	            Headers requestHeaders = exchange.getRequestHeaders();  
	            Set<String> keySet = requestHeaders.keySet();  
	            Iterator<String> iter = keySet.iterator();  
	            while (iter.hasNext()) {  
	                String key = iter.next();  
	                List values = requestHeaders.get(key);  
	                String s = key + " = " + values.toString() + "\n";  
	                responseBody.write(s.getBytes());  
	            }  
	            responseBody.close();  */
			 
			output(exchange, "TEST");

		}
	}

	private void output(HttpExchange exchange, String text) throws IOException {
		OutputStream responseBody = exchange.getResponseBody();
		responseBody.write(text.getBytes());
		responseBody.close();
	}

	private Map<String, String> parseRawQuery(String rawQuery) {
		Map<String, String> result = new HashMap<String, String>();
		String kvs[] = rawQuery.split("&");
		for (String kv : kvs) {
			String arr[] = kv.split("=");
			if (arr.length == 2) {
				result.put(arr[0], arr[1]);
			}
		}
		return result;
	}

}
