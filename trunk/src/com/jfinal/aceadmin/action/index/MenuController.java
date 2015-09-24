package com.jfinal.aceadmin.action.index;

import net.sf.json.JSONObject;

import com.jfinal.aceadmin.utils.WeixinUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

public class MenuController extends Controller {


	public void list() {
		String menu = "";
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.get(url));
			menu = json.getString("menu");
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText("菜单：" + menu);
	}
	public void preAdd() {
		render("preAdd.html");
	}
	public void add() {
		String menu = "";
		String data = getPara("menu", "");
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.post(url, data));
			menu = json.getString("errmsg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText("结果：" + menu);
	}
	public void del() {
		String menu = "";
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + WeixinUtil.getAccessToken();
			JSONObject json = JSONObject.fromObject(HttpKit.get(url));
			menu = json.getString("errmsg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderText("结果：" + menu);
	}

}
