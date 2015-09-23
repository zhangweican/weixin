package com.jfinal.aceadmin.process;

import net.sf.json.JSONObject;

import com.jfinal.aceadmin.bean.ReceiveMessageOutput;
import com.jfinal.aceadmin.bean.WeixinInput;
import com.jfinal.aceadmin.bean.WeixinOutput;
import com.jfinal.aceadmin.utils.WeixinUtil;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;

/**
 * 解析接收到的微信xml，返回消息对象
 *
 */
public class ReceiveMessageProcess implements IProcess{
	private Logger logger = Logger.getLogger(ReceiveMessageProcess.class.getName());
	@Override
	public WeixinOutput process(WeixinInput e) {

		ReceiveMessageOutput output = new ReceiveMessageOutput();
		output.setTo(e.getFromUserName());
		output.setFrom(e.getToUserName());
		
		//获取聊天内容
		String content = "外星语无法理解，冲输入吧。";
		try {
			String url = "http://i.itpk.cn/api.php?question=" + e.getContent() + "&api_key=2f284f8c79f3ac816640a159af888a1a&api_secret=dtgacun9aekl";
			content = HttpKit.get(url);
		} catch (Exception e2) {
			logger.error("用户的输入信息无法识别", e2);
			
		}
		output.setContent(content);
		
		return output;
	}
}
