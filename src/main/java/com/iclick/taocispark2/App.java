package com.iclick.taocispark2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        String path=App.class.getResource(".").getFile().toString();//获得该类的class所在的目录
        System.out.println(path);
        System.out.println(System.getProperty("user.dir")); //获得工程的路径
         
        //或者用如下的方法
        File  file=new File("");
        System.out.println(file.getCanonicalPath());
        System.out.println(file.getAbsolutePath());
        
       BufferedReader  ffFileWriter=   new BufferedReader(  new InputStreamReader( new FileInputStream(new File("pom.xml"))));
        int count=1;
      String str= ffFileWriter.readLine();
      while(str!=null){
    	  System.out.println(str);
    	  count++;
    	  if(count==3){
    		  break;
    	  }
    	  str=ffFileWriter.readLine();
      }
       
       
        
    }
}
