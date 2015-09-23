package com.jfinal.aceadmin.utils;

import java.util.HashSet;
import java.util.Set;




import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;


public class WeixinUtil {
	private Logger logger = Logger.getLogger(WeixinUtil.class.getName()); 
	
	private static WeixinUtil util = new WeixinUtil();
	private static String accessToken = "not_get_right_access_token";
	private static boolean isRunning = false;
    private static Set<String> ips = new HashSet<String>();
	
	
	private WeixinUtil(){
    };
    
    public static WeixinUtil get(){
    	return util;
    }

    public synchronized static void start(){
    	if(isRunning == false){
    		util.new AccessTokenThread().start();
    		isRunning = true;
    	}
    }
    public synchronized static void stop(){
    	isRunning = false;
    }
    
    public static String getAccessToken(){
    	return accessToken;
    }
    public static Set<String> getIps(){
    	return ips;
    }
    
    class AccessTokenThread extends Thread{

		private  void getAccessToken(){
			try {
				String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" 
						+ PropKit.get("weixin.appid","") 
						+ "&secret=" + PropKit.get("weixin.appsecret","");
				JSONObject json = JSONObject.fromObject(HttpKit.get(url));
				accessToken = json.getString("access_token");
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("获取到的access token是： " + accessToken);
		}
		private void getIPS(){
			try {
				String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + accessToken;
				JSONObject json = JSONObject.fromObject(HttpKit.get(url));
				JSONArray array = json.getJSONArray("ip_list");
				for(int i = 0 ; i < array.size() ; i++){
					ips.add(array.getString(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("获取到的微信IP个数： " + ips.size());
		}
		public void run() {
			while(isRunning){
	    		try {
					getAccessToken();
					if(accessToken != null ){
						getIPS();
					}
					try {
						Thread.sleep(2 * 3600 * 1000); //2小时
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
	    	}
		}
    	
    }
    
}