package com.jfinal.aceadmin.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiveMessageNewsOutput extends ReceiveMessageOutput {
	private String to;
	private String from;
	
	private int ArticleCount;
	private List<ReceiveMessageNewsItem> items = new ArrayList<ReceiveMessageNewsItem>();

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<ReceiveMessageNewsItem> getItems() {
		return items;
	}

	public void setItems(List<ReceiveMessageNewsItem> items) {
		this.items = items;
	}

	@Override
	public String formatOutput() {
		StringBuffer sb = new StringBuffer();  
        Date date = new Date();  
        sb.append("<xml><ToUserName><![CDATA[");  
        sb.append(to);  
        sb.append("]]></ToUserName><FromUserName><![CDATA[");  
        sb.append(from);  
        sb.append("]]></FromUserName><CreateTime>");  
        sb.append(date.getTime());  
        sb.append("</CreateTime><MsgType><![CDATA[text]]></MsgType>");  
        sb.append("<ArticleCount>" + items.size() + "</ArticleCount>");  
        if(items.size() > 0 ){
        	sb.append("<Articles>");
        	for(ReceiveMessageNewsItem item : items){
        		sb.append("<item>");
        		sb.append("<Title><![CDATA[" + item.getTitle() +"]]></Title>");
        		sb.append("<Description><![CDATA[" + item.getDescription() + "]]></Description>");
        		sb.append("<PicUrl><![CDATA[" + item.getPicUrl() + "]]></PicUrl>");
        		sb.append("<Url><![CDATA[" + item.getUrl() + "]]></Url>");
        		sb.append("</item>");
        	}
        	sb.append("</Articles>");
        }
        sb.append("</xml>");  
        return sb.toString();
	}


}




