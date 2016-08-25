package com.iclick.common;

import java.util.ResourceBundle;

public class Commonfig {

	private  String propertyFileName;
	private  ResourceBundle  resourceBundle;
	public Commonfig(){
		propertyFileName="/src/main/resources";
		resourceBundle=ResourceBundle.getBundle(propertyFileName);
	}
	public  Commonfig(String propertyFileName){
		this.propertyFileName=propertyFileName;
		this.resourceBundle=ResourceBundle.getBundle(this.propertyFileName);
	}
	public String getString(String key){
		if(key==null||key.equals("")||key.equals("null")){
			return "";
		}
		String  result="";
		try{
			result=resourceBundle.getString(key);
		}catch(Exception  e){
			e.printStackTrace();
		}
		
		return result;
	}
	
}
