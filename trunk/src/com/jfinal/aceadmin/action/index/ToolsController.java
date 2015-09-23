package com.jfinal.aceadmin.action.index;

import java.net.URLEncoder;

import net.sf.json.JSONObject;

import com.jfinal.aceadmin.utils.Utils;
import com.jfinal.aceadmin.utils.WeixinUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Logger;
import com.jfinal.upload.UploadFile;

public class ToolsController extends Controller {
	private Logger logger = Logger.getLogger(ToolsController.class.getName());

	public void createTempQRCode() {
		String iframeSrc = "";
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + WeixinUtil.getAccessToken();
			String data = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}";
			JSONObject json = JSONObject.fromObject(HttpKit.post(url,data));
			String ticket = json.getString("ticket");
			ticket = URLEncoder.encode(ticket,"UTF-8");
			iframeSrc = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
			//String image = HttpKit.get(url1);
		} catch (Exception e) {
			logger.error("获取二维码失败", e);
			e.printStackTrace();
		}
		renderHtml("<img src=\"" + iframeSrc + "\"/>");
	}
}
