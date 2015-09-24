package com.jfinal.aceadmin.action.index;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jfinal.aceadmin.utils.WeixinUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Logger;

public class NewsController extends Controller {
	private Logger logger = Logger.getLogger(NewsController.class.getName());

	private final static String Type = "news";
	
	public void preUpdate(){
		String media_id = getPara("media_id");
		String title = "";
		String thumb_media_id = "";
		String author = "";
		String digest = "";
		int show_cover_pic = 0;
		String content = "";
		String content_source_url = "";
		String msg = "";
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.post(url, "{\"media_id\":\"" + media_id + "\"}"));
			JSONObject obj = json.getJSONArray("news_item").getJSONObject(0);
			msg = json.toString();
			title = obj.getString("title");
			thumb_media_id = obj.getString("thumb_media_id");
			author = obj.getString("author");
			digest = obj.getString("digest");
			show_cover_pic = obj.getInt("show_cover_pic");
			content = obj.getString("content");
			content_source_url = obj.getString("content_source_url");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setAttr("media_id",media_id);
		setAttr("title",title);
		setAttr("thumb_media_id",thumb_media_id);
		setAttr("author",author);
		setAttr("digest",digest);
		setAttr("show_cover_pic",show_cover_pic);
		setAttr("content",content);
		setAttr("content_source_url",content_source_url);
		
		setAttr("msg", msg);
		render("preUpdate.html");
	}
	public void preAdd(){
		render("preAdd.html");
	}
	public void add(){
		String title = getPara("title");
		String thumb_media_id = getPara("thumb_media_id");
		String author = getPara("author");
		String digest = getPara("digest");
		int show_cover_pic = getParaToInt("show_cover_pic");
		String content = getPara("content");
		String content_source_url = getPara("content_source_url");
		/*try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}*/
		String media_id = "";
		String data = "{"
				  + "\"articles\": [{"
				   		+ "\"title\": \"" + title + "\","
				   		+ "\"thumb_media_id\": \"" + thumb_media_id + "\","
				   		+ "\"author\": \"" + author + "\","
				   		+ "\"digest\": \"" + digest + "\","
				   		+ "\"show_cover_pic\": \"" + show_cover_pic + "\","
				   		+ "\"content\": \"" + content + "\","
				   		+ "\"content_source_url\": \"" + content_source_url + "\""
				  + "}]"
			+ "}";
		try {
			String u = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.post(u, data));
			media_id = json.getString("media_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText("media_id:" + media_id);
	}
	public void update(){
		String media_id = getPara("media_id");
		String title = getPara("title");
		String thumb_media_id = getPara("thumb_media_id");
		String author = getPara("author");
		String digest = getPara("digest");
		int show_cover_pic = getParaToInt("show_cover_pic");
		String content = getPara("content");
		String content_source_url = getPara("content_source_url");
		
		String errmsg = "";
		String data = "{"
				+ "\"media_id\":\"" + media_id +"\","
				+ "\"index\":0,"
				+ "\"articles\": [{"
				+ "\"title\": \"" + title + "\","
				+ "\"thumb_media_id\": \"" + thumb_media_id + "\","
				+ "\"author\": \"" + author + "\","
				+ "\"digest\": \"" + digest + "\","
				+ "\"show_cover_pic\": \"" + show_cover_pic + "\","
				+ "\"content\": \"" + content + "\","
				+ "\"content_source_url\": \"" + content_source_url + "\""
				+ "}]"
				+ "}";
		try {
			String u = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.post(u, data));
			errmsg = json.getString("errmsg");
		} catch (Exception e) {
			errmsg = "更新失败";
			e.printStackTrace();
		}
		renderText("errmsg:" + errmsg);
	}
	
	
	public void list(){
		String post = "{"
		           + "\"type\":\"" + Type + "\","
		           + "\"offset\":0,"
		           + "\"count\":20"       
		           + "}";
		int total_count = 0;
		int item_count = 0;
		String msg = "";
		Map<String,String> images = new HashMap<String,String>();
		try {
			String u = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.post(u, post));
			msg = json.toString();
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
		setAttr("msg", msg);
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
