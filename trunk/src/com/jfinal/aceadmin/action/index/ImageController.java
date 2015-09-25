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

public class ImageController extends Controller {
	private Logger logger = Logger.getLogger(ImageController.class.getName());
	private final static String Type = "image";
	
	public void list(){
		String post = "{"
		           + "\"type\":\"" + Type + "\","
		           + "\"offset\":0,"
		           + "\"count\":3"       
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
					images.put(jo.getString("media_id"), jo.getString("name") + " | " 
							+ jo.getString("update_time") +  " | " 
							+ jo.getString("url"));
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
	
	
	public void preUploadImage2Media(){
		render("preUploadImage2Media.html");
	}
	public void uploadImage2Media(){
		UploadFile uFile = getFile("file");
		String msg = "";
		if(uFile == null || uFile.getFile() == null){
			msg = "上传的文件错误";
		}
		else if(!uFile.getOriginalFileName().toLowerCase().endsWith(".png") && !uFile.getOriginalFileName().toLowerCase().endsWith(".jpg")){
			msg = "文件格式不正确";
		}
		else if(uFile.getFile().length() > 1024 * 1024){ //1M
			msg = "文件太大";
		}
		else{
			try {
				String url = "https://api.weixin.qq.com/cgi-bin/material/add_material";
				JSONObject json = Utils.uploadMedia(url, uFile.getFile(), WeixinUtil.getAccessToken(), Type);
				String imageURL = json.getString("url");
				msg = "成功。URL:" + imageURL + "<br><img src=\"" + imageURL +"\">";
			} catch (Exception e) {
				logger.error("上传图片失败", e);
				msg = "上传图片失败";
				e.printStackTrace();
			}
		}
		renderHtml("结果：" + msg);
	}
}
