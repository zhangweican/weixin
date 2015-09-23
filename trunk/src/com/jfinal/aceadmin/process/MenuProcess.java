package com.jfinal.aceadmin.process;

import net.sf.json.JSONObject;

import com.jfinal.aceadmin.bean.HotMenuOutput;
import com.jfinal.aceadmin.bean.NullOutput;
import com.jfinal.aceadmin.bean.WeixinInput;
import com.jfinal.aceadmin.bean.WeixinOutput;
import com.jfinal.aceadmin.bean.ZanMenuOutput;
import com.jfinal.aceadmin.utils.WeixinUtil;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Logger;

/**
 * 解析接收到的微信xml，返回消息对象
 *
 */
public class MenuProcess implements IProcess{
	private Logger logger = Logger.getLogger(MenuProcess.class.getName());
	@Override
	public WeixinOutput process(WeixinInput e) {
		
		String nickname = e.getFromUserName();
		String province = "未知";
		String city = "未知";
		//获取用户信息
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + WeixinUtil.getAccessToken() + "&openid=" + e.getFromUserName() +"&lang=zh_CN";
			JSONObject json = JSONObject.fromObject(HttpKit.get(url));
			nickname = json.getString("nickname");
			province = json.getString("province");
			city = json.getString("city");
		} catch (Exception ee) {
			logger.error("获取用户信息失败", ee);
			ee.printStackTrace();
		}
		//热销皮套
		if("Hot".equals(e.getEventKey())){
			HotMenuOutput output = new HotMenuOutput();
			output.setTo(e.getFromUserName());
			output.setFrom(e.getToUserName());
			String content = "热销的皮套有：<br>"
					+ "<ul>"
					+ "<li>1.<a href='www.baidu.com/'>红色皮套</a></li>"
					+ "<li>2.红色皮套</li>"
					+ "<li>3.红色皮套</li>"
					+ "<li>4.红色皮套</li>"
					+ "<li>5.红色皮套</li>"
			+ "</ul>";
			output.setContent(content);
			return output;
			
		}
		else if("520Zan".equals(e.getEventKey())){
			ZanMenuOutput output = new ZanMenuOutput();
			output.setTo(e.getFromUserName());
			output.setFrom(e.getToUserName());
			
			String content = "谢谢你【" + nickname + "】来赞我们。";
			output.setContent(content);
			return output;
		}
		
		return new NullOutput();
	}
}
