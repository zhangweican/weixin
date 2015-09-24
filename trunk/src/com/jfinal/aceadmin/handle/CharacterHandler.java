package com.jfinal.aceadmin.handle;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.aceadmin.utils.Commons;
import com.jfinal.handler.Handler;
import com.jfinal.kit.StrKit;

/**
 * Provide a context path to view if you need.
 * <br>
 * Example:<br>
 * In JFinalFilter: handlers.add(new ContextPathHandler("CONTEXT_PATH"));<br>
 * in freemarker: <img src="${BASE_PATH}/images/logo.png" />
 */
public class CharacterHandler extends Handler {
	
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		try {
			request.setCharacterEncoding(Commons.UTF8);
			response.setCharacterEncoding(Commons.UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		nextHandler.handle(target, request, response, isHandled);
	}
}
