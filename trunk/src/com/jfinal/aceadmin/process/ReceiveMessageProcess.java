package com.jfinal.aceadmin.process;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aceadmin.bean.ReceiveMessageNewsItem;
import com.jfinal.aceadmin.bean.ReceiveMessageNewsOutput;
import com.jfinal.aceadmin.bean.ReceiveMessageOutput;
import com.jfinal.aceadmin.bean.WeixinInput;
import com.jfinal.aceadmin.bean.WeixinOutput;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Logger;

/**
 * 解析接收到的微信xml，返回消息对象
 *
 */
public class ReceiveMessageProcess implements IProcess{
	private static final int AD = 0;
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
		
		int random = (int)(Math.random()*10);
		if(AD == random){
			logger.error("启动一次广告时间给用户:" + e.getFromUserName());
			ReceiveMessageNewsOutput ot = new ReceiveMessageNewsOutput();
			ot.setTo(e.getFromUserName());
			ot.setFrom(e.getToUserName());
			int totalItem = 2;
			List<ReceiveMessageNewsItem> items = new ArrayList<ReceiveMessageNewsItem>();
			for(int i = 0 ;i < totalItem ; i ++){
				ReceiveMessageNewsItem item = new ReceiveMessageNewsItem();
				item.setTitle("测试标题" + i);
				item.setDescription("我是描述" + i);
				item.setPicUrl("http://image.photophoto.cn/nm-6/018/030/0180300244.jpg");
				item.setUrl("http://mp.weixin.qq.com/wiki/14/89b871b5466b19b3efa4ada8e577d45e.html#.E5.9B.9E.E5.A4.8D.E5.9B.BE.E6.96.87.E6.B6.88.E6.81.AF");
				
				items.add(item);
			}
			ot.setArticleCount(totalItem);
			return ot;
		}
		else{
			return output;
		}
	}
}
