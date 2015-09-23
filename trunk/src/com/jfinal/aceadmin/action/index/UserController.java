package com.jfinal.aceadmin.action.index;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jfinal.aceadmin.utils.WeixinUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Logger;

public class UserController extends Controller {
	private Logger logger = Logger.getLogger(UserController.class.getName());

	public void list() {
		int total = 0;
		Map<String,String> users = new HashMap<String,String>();
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.get(url));
			total = json.getInt("total");
			JSONArray ids = json.getJSONObject("data").getJSONArray("openid");
			if(ids != null && ids.size() > 0){
				for(int i = 0 ;i < ids.size() ;i ++){
					String id = ids.getString(i);
					String nickname = "信息缺失";
					String province = "未知";
					String city = "未知";
					int groupid = 0;
					//获取用户信息
					try {
						String url1 = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + WeixinUtil.getAccessToken() + "&openid=" + id +"&lang=zh_CN";
						JSONObject json1 = JSONObject.fromObject(HttpKit.get(url1));
						nickname = json1.getString("nickname");
						province = json1.getString("province");
						city = json1.getString("city");
						groupid = json1.getInt("groupid");
					} catch (Exception ee) {
						logger.error("获取用户信息失败", ee);
						ee.printStackTrace();
					}
					users.put(id, nickname + " " + "(" + province + "," + city +") 组ID：" + groupid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		for(Entry<String, String> e : users.entrySet()){
			sb.append(e.getKey() + " | " + e.getValue()).append("<br/>");
		}
		
		renderHtml("用户列表(总共" + total + "个)：<br/>" + sb.toString());
	}
}
