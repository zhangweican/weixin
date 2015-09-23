package com.jfinal.aceadmin.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jfinal.aceadmin.bean.Session;
import com.jfinal.aceadmin.utils.Commons;
import com.jfinal.aceadmin.utils.Utils;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

/**
 * 1.记录登陆日志
 * 2.判断是否登陆过期，是否登陆
 * 3.判断是否有权限
 * @author zhangweican
 *
 */
public class AuthInterceptor implements Interceptor {
	private Logger logger = Logger.getLogger(AuthInterceptor.class.getName());
	@Override
	public void intercept(ActionInvocation ai) {
		
		Controller c = ai.getController();
		HttpSession hs = c.getRequest().getSession();
		HttpServletRequest request = c.getRequest();
		logger.info("[Request]URL:" + request.getRequestURL());
		logger.info("[Request]Param:" + Utils.map2String(request.getParameterMap()));
		
		
		long start = System.currentTimeMillis();
		//记录日志
		ai.invoke();
		//保存日志
	}



}
