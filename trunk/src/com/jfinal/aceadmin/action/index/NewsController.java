package com.jfinal.aceadmin.action.index;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jfinal.aceadmin.utils.Utils;
import com.jfinal.aceadmin.utils.WeixinUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Logger;
import com.jfinal.upload.UploadFile;

public class NewsController extends Controller {
	private Logger logger = Logger.getLogger(NewsController.class.getName());

	private final static String Type = "news";
	
	public void list(){
		String post = "{"
		           + "\"type\":\"" + Type + "\","
		           + "\"offset\":0,"
		           + "\"count\":1000"       
		           + "}";
		int total_count = 0;
		int item_count = 0;
		Map<String,String> images = new HashMap<String,String>();
		try {
			String u = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.post(u, post));
			total_count = json.getInt("total_count");
			item_count = json.getInt("item_count");
			if(item_count > 0){
				JSONArray items = json.getJSONArray("item");
				for(int i = 0 ;i < items.size(); i++){
					JSONObject jo = items.getJSONObject(i);
					images.put(jo.getString("media_id"), jo.getString("title") + " | " 
							+ jo.getString("content"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		setAttr("total_count", total_count);
		setAttr("images", images);
		render("list.html");
	}
	public void del(){
		String media_id = getPara("media_id");
		String menu = "";
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.post(url, "{\"media_id\":\"" + media_id + "\"}"));
			menu = json.getString("errmsg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText("结果：" + menu);
	}
}
