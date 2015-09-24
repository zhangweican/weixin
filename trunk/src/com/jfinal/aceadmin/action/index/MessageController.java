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

public class MessageController extends Controller {
	private Logger logger = Logger.getLogger(MessageController.class.getName());

	/**
	 * 加载主页
	 */
	public void preSendTempleteMessage() {
		render("preSendTempleteMessage.html");
	}
	
	public void sendTempleteMessage() {
		String touser = getPara("touser");
		String template_id = getPara("template_id");
		String url = "http://mp.weixin.qq.com/wiki/17/304c1885ea66dbedf7dc170d84999a9d.html";
		String data = getPara("data");
		String post = "{"
           + "\"touser\":\"" + touser + "\","
           + "\"template_id\":\"" + template_id + "\","
           + "\"url\":\"" + url + "\", "       
           + "\"data\":" + data
           + "}";
		
		String errmsg = "";
		try {
			String u = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.post(u, post));
			errmsg = json.getString("errmsg");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		renderHtml("发送结果：" + errmsg);
	}
	
	public void preSendAllMessage(){
		render("preSendAllMessage");
	}
	public void sendAllMessage(){
		String msgtype = getPara("msgtype");
		String media_id = getPara("media_id");
		String content = getPara("content");
		String tStr = "";
		if("mpnews".equals(msgtype)){
			tStr = "\"mpnews\":{\"media_id\":\"" + media_id +"\"}";
		}
		else if("text".equals(msgtype)){
			tStr = "\"text\":{\"content\":\"" + content +"\"}";
		}
		String post = "{"
			+ "\"filter\":{\"is_to_all\":true,\"group_id\":\"2\"},"
			+ tStr + ","
		    + "\"msgtype\":\"text\""
		+ "}";
		String errmsg = "";
		try {
			String u = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.post(u, post));
			errmsg = json.getString("errmsg");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		renderHtml("发送结果：" + errmsg);
		
	}
}
