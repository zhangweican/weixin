package com.jfinal.aceadmin.bean;

import com.jfinal.render.ContentType;

public abstract class WeixinOutput {
	private ContentType contentType = ContentType.XML;
	
	
	public abstract String formatOutput();
	
	
	
	public ContentType getContentType() {
		return contentType;
	}
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	
	
}
