/*package com.iclick.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.buzzinate.bidding.antispam.util.AntispamConstants;
import com.buzzinate.bidding.antispam.util.Config;
import com.buzzinate.bidding.antispam.util.SendMail;
import com.buzzinate.common.util.DateTimeUtil;
import com.buzzinate.common.util.redis.RedisClient;

public class BlacklistApi {
	private static Logger logger = Logger.getLogger(BlacklistApi.class);
	
	private static RedisClient redisClient = new RedisClient(Config.getString("redis.hosts"));
	
	*//**
	 * @type type=1 返回所有黑名单,用于在线过滤,type = 0只返回用于离线过滤的黑名单
	 * blacklist 转化为json
	 * @return
	 *//*
    public static String blacklistJson(int type){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put(AntispamConstants.IP_PREFIX, ipSet(type));
    	map.put(AntispamConstants.COOKIE_PREFIX, redisClient.hkeys(AntispamConstants.COOKIE_PREFIX));
    	map.put(AntispamConstants.TAGID_PREFIX, redisClient.hkeys(AntispamConstants.TAGID_PREFIX));
    	map.put(AntispamConstants.DOMAIN_PREFIX, redisClient.hkeys(AntispamConstants.DOMAIN_PREFIX));
    	return JSON.toJSONString(map);
    }
    
    *//**
     * 
     * @param type type=1 返回所有ip集合,type = 0只返回用于离线过滤的ip集合
     * @return
     *//*
    public static Set<String> ipSet(int type){
    	if(type == 0){
    		Map<String, String> map = redisClient.hgetAll(AntispamConstants.IP_PREFIX);
    		Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
    		while(iterator.hasNext()){
    			Map.Entry<String, String> kv = iterator.next();
    			String value = kv.getValue(); //value格式 时间+","+过期时间+","+type
    			if(value != null){
    				String arr[] = value.split(",");
    				if(arr.length >= 3 && arr[2].equals("1")){ //type = 1表示只用于在线过滤,离线过滤需要排除掉
    					iterator.remove();
    				}
    			}
    		}
    		return map.keySet();
    	}else{
    		return redisClient.hkeys(AntispamConstants.IP_PREFIX);
    	}
    }
    
    *//**
     * 将过期的黑名单删除
     *//*
    private static void expireBlacklist(){
    	long ipSize = expireBlacklist(AntispamConstants.IP_PREFIX);
    	long cookieSize = expireBlacklist(AntispamConstants.COOKIE_PREFIX);
    	long tagidSize = expireBlacklist(AntispamConstants.TAGID_PREFIX);
    	long domainSize = expireBlacklist(AntispamConstants.DOMAIN_PREFIX);
    	logger.info("expire ips:"+ipSize+",cookies:"+cookieSize+",tagids:"+tagidSize+",domains:"+domainSize);
    }
    
    *//**
     * 删除过期黑名单
     * @param key
     *//*
    private static long expireBlacklist(String key){
    	StopWatch watch = new StopWatch();
    	watch.start();
    	Map<String, String> map = redisClient.hgetAll(key);
    	logger.info("hgetAll "+ key+" takes " + watch.getTime());
    	watch.split();
    	long currentTime = System.currentTimeMillis();
    	long count = 0;
    	List<String> pending = new ArrayList<String>();
    	if(map != null && !map.isEmpty()){
			for(Map.Entry<String, String> kv: map.entrySet()){
				String val = kv.getValue();
				if(val == null) continue;
				String timeExpire[] = val.split(","); //value格式：该条记录插入时间和过期时间用逗号分隔
				if(timeExpire.length >= 2){
					if(currentTime > Long.parseLong(timeExpire[0]) + Long.parseLong(timeExpire[1]) * 1000){ //已过期
						pending.add(kv.getKey());
						if(pending.size() >= 1000){
							count += redisClient.hdel(key, pending.toArray(new String[pending.size()]));
						}
					}
				}
			}//end for
			
			if(pending.size() > 0){
				count += redisClient.hdel(key, pending.toArray(new String[pending.size()]));
			}
			logger.info("expire "+key+" count:"+count+" takes " + watch.getSplitTime());
			watch.stop();
		}
    	return count;
    }
    
    *//**
     * 将点击日志同一bid对应的点击次数数据过期
     *//*
    public static void expireClickCnt(){
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(calendar.DATE,-1);
    	calendar.getTime();
    	String key = AntispamConstants.CLICK_CNT+DateTimeUtil.formatDate(calendar.getTime());
    	logger.info("delete "+key);
    	redisClient.delete(key);
    }
    public static void expire(){
    	expireBlacklist();
    	expireClickCnt();
    }
    
    *//**
     * 备份黑名单数据
     *//*
    private static void backup(){
    	String filename="antispam_"+DateTimeUtil.formatDate(new Date(), DateTimeUtil.FMT_DATE_YYMMDD);
    	try{
	    	Map<String, Map<String, String>> antispamRule = new HashMap<String, Map<String, String>>();
	    	Map<String, String> antispamIpMap = redisClient.hgetAll(AntispamConstants.IP_PREFIX);
	    	Map<String, String> antispamCookieMap = redisClient.hgetAll(AntispamConstants.COOKIE_PREFIX);
	    	Map<String, String> antispamDomainMap = redisClient.hgetAll(AntispamConstants.DOMAIN_PREFIX);
	    	Map<String, String> antispamTagidMap = redisClient.hgetAll(AntispamConstants.TAGID_PREFIX);
	    	antispamRule.put(AntispamConstants.IP_PREFIX, antispamIpMap);
	    	antispamRule.put(AntispamConstants.COOKIE_PREFIX, antispamCookieMap);
	    	antispamRule.put(AntispamConstants.DOMAIN_PREFIX, antispamDomainMap);
	    	antispamRule.put(AntispamConstants.TAGID_PREFIX, antispamTagidMap);
	    	FileUtils.writeStringToFile(new File(filename), JSON.toJSONString(antispamRule));
	    	logger.info("backup antispam success, filename="+filename);
	    	
	    	//删除3天前的备份文件
	    	if(antispamIpMap != null && antispamIpMap.size() > 50000){ //如果ip黑名单数据大于50000，认为redis中的数据正常，删除3天前的备份，否则不删除备份文件
	    		String threeDaysAgoFilename="antispam_"+DateTimeUtil.formatDate(DateTimeUtil.subtractDays(new Date(), 3), DateTimeUtil.FMT_DATE_YYMMDD);
	    		logger.info("delete antispam backup, filename="+threeDaysAgoFilename);
	    		new File(threeDaysAgoFilename).delete();
	    	}else{
	    		logger.info("blacklist ip is less than 50000,there may be a problem");
	    		SendMail.sendEmail("blacklist ip is less than 50000, there may be a problem", "blacklist ip is less than 50000, there may be a problem", Config.getString("mail.to","ds@i-click.com"));
	    	}
    	}catch(Exception e){
    		logger.error("backup antispam fail,filename="+filename, e);
    	}
    }
    
    *//**
     * 恢复黑名单数据
     *//*
    private static void restore(){
    	String filename="antispam_"+DateTimeUtil.formatDate(new Date(), DateTimeUtil.FMT_DATE_YYMMDD);
    	try{
    		String antispamJson = FileUtils.readFileToString(new File(filename));
    		TypeReference<Map<String,Map<String, String>>> type = new TypeReference<Map<String,Map<String, String>>>(){};
    		Map<String,Map<String, String>> antispamRule = JSON.parseObject(antispamJson, type);
    		//因为ip黑名单较多，每次导入1000条
    		Map<String, String> pending = new HashMap<String, String>();
    		Map<String, String> antispamIpMap = antispamRule.get(AntispamConstants.IP_PREFIX);
    		for(Map.Entry<String, String> kv: antispamIpMap.entrySet()){
    			pending.put(kv.getKey(), kv.getValue());
    			
    			if(pending.size() >= 1000){
    				redisClient.hset(AntispamConstants.IP_PREFIX, pending);
    				pending.clear();
    			}
    		}
    		if(pending.size() > 0){
				redisClient.hset(AntispamConstants.IP_PREFIX, pending);
				pending.clear();
			}
    		
    		redisClient.hset(AntispamConstants.COOKIE_PREFIX, antispamRule.get(AntispamConstants.COOKIE_PREFIX));
    		redisClient.hset(AntispamConstants.DOMAIN_PREFIX, antispamRule.get(AntispamConstants.DOMAIN_PREFIX));
    		redisClient.hset(AntispamConstants.TAGID_PREFIX, antispamRule.get(AntispamConstants.TAGID_PREFIX));
    		logger.info("restore antispam success, filename=" + filename);
    		
    		
    	}catch(Exception e){
    		logger.error("restore antispam fail, filename="+filename);
    	}
    }
    
    *//**
     * 导入白名单
     *//*
    private static void importWhitelist(){
    	try {
			List<String> list = FileUtils.readLines(new File("whitelist.txt"));
			for(String whitelist: list){
				if(StringUtils.isNotBlank(whitelist) && whitelist.length() < 100){
					redisClient.hset(AntispamConstants.WHITELIST_PREFIX,whitelist.trim(), String.valueOf(System.currentTimeMillis()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    public static void main(String[] args) {
    	if(args.length < 1){
    		expire();
    	}else if(args[0].equals("backup")){
    		backup();
    	}else if(args[0].equals("restore")){
    		restore();
    	}else if(args[0].equals("whitelist")){ //导入白名单
    		importWhitelist();
    	}
	}
}
*/