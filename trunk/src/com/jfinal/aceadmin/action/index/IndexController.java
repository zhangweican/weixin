package com.jfinal.aceadmin.action.index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aceadmin.bean.WeixinOutput;
import com.jfinal.aceadmin.process.ProcessFactory;
import com.jfinal.aceadmin.utils.SignUtil;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

public class IndexController extends Controller {
	private Logger logger = Logger.getLogger(IndexController.class.getName());

	/**
	 * 加载主页
	 */
	public void index() {
		render("index.html");
	}
	
	 public void sign(){
		 // 随机字符串
		 String echostr = getRequest().getParameter("echostr"); 
		 if(StringUtils.isNotEmpty(echostr)){
			logger.info("微信首次校验部分");
			// 微信加密签名
			String signature = getRequest().getParameter("signature");
			// 时间戮
			String timestamp = getRequest().getParameter("timestamp");
			// 随机数
			String nonce = getRequest().getParameter("nonce");

			logger.info("signature:" + signature + " timestamp:" + timestamp + " nonce:" + nonce + " echostr:" + echostr);
			// 通过检验 signature 对请求进行校验，若校验成功则原样返回 echostr，表示接入成功，否则接入失败
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				logger.info("sign success");
				renderText(echostr);
			} else {
				renderText("");
				logger.info("sign false");
			}
		 }else{
			StringBuffer buffer = new StringBuffer();
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(getRequest().getInputStream()));
				String line = "";
				while ((line = in.readLine()) != null) {
					buffer.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("[From微信]：" + buffer.toString());
			
			WeixinOutput output = ProcessFactory.process(buffer.toString());
			logger.info("[To微信]：" + output.formatOutput() + " (" + output.getContentType() +")");
			switch (output.getContentType()) {
			case XML:
				renderText(output.formatOutput(), output.getContentType().value());
				break;
			case JSON:
				renderJson(output.formatOutput());
				break;
			default:
				renderText(output.formatOutput());
				break;
			}
		 }
	 }
}
