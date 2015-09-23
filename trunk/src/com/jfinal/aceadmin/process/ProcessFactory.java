package com.jfinal.aceadmin.process;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.jfinal.aceadmin.bean.NullOutput;
import com.jfinal.aceadmin.bean.WeixinInput;
import com.jfinal.aceadmin.bean.WeixinOutput;
import com.jfinal.log.Logger;

public class ProcessFactory {
	private static Logger logger = Logger.getLogger(ProcessFactory.class.getName());
	
	private static String MsgType_Text = "text";
	private static String MsgType_Event = "event";
	private static String Event_subscribe = "subscribe";
	private static String Event_unsubscribe = "unsubscribe";
	public static WeixinOutput process(String strXml){
		WeixinInput entry = getMsgEntity(strXml);
		if(entry == null){
			return new NullOutput();
		}
		if(MsgType_Text.equalsIgnoreCase(entry.getMsgType())){	//接受文本消息处理
			return new ReceiveMessageProcess().process(entry);
		}
		else if(MsgType_Event.equalsIgnoreCase(entry.getMsgType())&& 
				Event_subscribe.equalsIgnoreCase(entry.getEvent())){	//订阅
			return new SubscribeProcess().process(entry);
		}
		else if(MsgType_Event.equalsIgnoreCase(entry.getMsgType())&& 
				Event_unsubscribe.equalsIgnoreCase(entry.getEvent())){	//订阅
			return new UnSubscribeProcess().process(entry);
		}
		else if(MsgType_Event.equalsIgnoreCase(entry.getMsgType())){	//接受文本菜单
			return new MenuProcess().process(entry);
		}
		return new NullOutput();
	}
	
	/**
	 * 解析微信xml消息
	 * @param strXml
	 * @return
	 */
	private static WeixinInput getMsgEntity(String strXml){
		WeixinInput msg = null;
		try {
			if (strXml.length() <= 0 || strXml == null)
				return null;
			 
			// 将字符串转化为XML文档对象
			Document document = DocumentHelper.parseText(strXml);
			// 获得文档的根节点
			Element root = document.getRootElement();
			// 遍历根节点下所有子节点
			Iterator<?> iter = root.elementIterator();
			
			// 遍历所有结点
			msg = new WeixinInput();
			//利用反射机制，调用set方法
			while(iter.hasNext()){
				Element ele = (Element)iter.next();
				//获取set方法中的参数字段（实体类的属性）
				Field field = msg.getClass().getDeclaredField(ele.getName());
				//获取set方法，field.getType())获取它的参数数据类型
				Method method = msg.getClass().getDeclaredMethod("set"+ele.getName(), field.getType());
				//调用set方法
				method.invoke(msg, ele.getText());
			}
		} catch (Exception e) {
			logger.error("xml 格式异常: "+ strXml,e);
			e.printStackTrace();
		}
		msg.set_allXML(strXml);
		return msg;
	}
}
