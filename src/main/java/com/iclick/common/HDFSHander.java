package com.iclick.common;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.commons.lang.time.StopWatch;
public class HDFSHander {
	 static {
		 PropertyConfigurator.configure("src/main/resources/log4j.properties"); 
	 }
	private static Logger logger = Logger.getLogger(HDFSHander.class);
	public static Commonfig config = new Commonfig("config");
	public static Configuration conf = new Configuration();
	
	
	public static String AntimodelHdfsPath = config.getString("input.click.path");
	public static int maxReferFre = Integer.valueOf(config.getString("maxReferFre"));
	
	
	static {
		String HdfsConfigPath=config.getString("hadoop.hdfs.config.path");
		conf.addResource(new Path(HdfsConfigPath+"/core-site.xml"));
		conf.addResource(new Path(HdfsConfigPath+"/hdfs-site.xml"));
	}

	public static void main(String[] args) throws InterruptedException {
		StopWatch  stopWatch=new StopWatch();
		stopWatch.start();
		
		logger.info(AntimodelHdfsPath);
		
		System.out.println(AntimodelHdfsPath);
		System.out.println(maxReferFre);
		stopWatch.split();
		Thread.sleep(1000);
		stopWatch.stop();
		System.out.println(stopWatch.getTime());
	}

}
