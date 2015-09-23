package com.jfinal.aceadmin.process;

import net.sf.json.JSONObject;

import com.jfinal.aceadmin.bean.SubscribeOutput;
import com.jfinal.aceadmin.bean.WeixinInput;
import com.jfinal.aceadmin.bean.WeixinOutput;
import com.jfinal.aceadmin.utils.WeixinUtil;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Logger;

/**
 * 解析接收到的微信xml，返回消息对象
 *
 */
public class SubscribeProcess implements IProcess{
	private Logger logger = Logger.getLogger(SubscribeProcess.class.getName());
	@Override
	public WeixinOutput process(WeixinInput e) {

		SubscribeOutput output = new SubscribeOutput();
		output.setTo(e.getFromUserName());
		output.setFrom(e.getToUserName());
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
		String content = "你好：" + nickname + "[" + province + "," + city +"],欢迎你来到鸟语学院！这里可以和鸟人（鸟鸟）聊天，赶快试试吧。";
		output.setContent(content);
		
		return output;
	}
}
