package com.jfinal.aceadmin.process;

import com.jfinal.aceadmin.bean.UnSubscribeOutput;
import com.jfinal.aceadmin.bean.WeixinInput;
import com.jfinal.aceadmin.bean.WeixinOutput;
import com.jfinal.log.Logger;

/**
 * 解析接收到的微信xml，返回消息对象
 *
 */
public class UnSubscribeProcess implements IProcess{
	private Logger logger = Logger.getLogger(UnSubscribeProcess.class.getName());
	@Override
	public WeixinOutput process(WeixinInput e) {

		UnSubscribeOutput output = new UnSubscribeOutput();
		output.setTo(e.getFromUserName());
		output.setFrom(e.getToUserName());
		String content = "谢谢光临。欢迎再次关注我们官方微信";
		output.setContent(content);
		
		return output;
	}
}
