package com.jfinal.aceadmin.appcfg;

import java.io.File;
import java.util.Properties;

import com.jfinal.aceadmin.action.index.AdminController;
import com.jfinal.aceadmin.action.index.IndexController;
import com.jfinal.aceadmin.action.index.MenuController;
import com.jfinal.aceadmin.action.index.MessageController;
import com.jfinal.aceadmin.action.index.ToolsController;
import com.jfinal.aceadmin.action.index.UserController;
import com.jfinal.aceadmin.interceptor.AuthInterceptor;
import com.jfinal.aceadmin.interceptor.FreemarkerSessionInViewInterceptor;
import com.jfinal.aceadmin.utils.Commons;
import com.jfinal.aceadmin.utils.WeixinUtil;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.log.Logger;


public class JFinalAceAdminConfig extends JFinalConfig {
	private Logger logger = Logger.getLogger(JFinalAceAdminConfig.class.getName());
	
	public Properties loadProp(String pro, String dev) {
		try {return loadPropertyFile(pro);}
		catch (Exception e)
			{return loadPropertyFile(dev);}
	}
	
	public void configConstant(Constants me) {
		//设置环境变量的属性
		String path = System.getProperty("catalina.home");
		File cf = new File(path + File.separator + Commons.Env_Config_FileName);
		if(!cf.exists()){
			logger.error("配置文件读取错误");
		}
		loadPropertyFile(cf);
		// 如果生产环境配置文件存在，则优先加载该配置，否则加载开发环境配置文件
		//loadProp("a_little_config_pro.txt", "a_little_config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", false));
		
		//启动微信获取token
		WeixinUtil.start();
	}
	
	public void configRoute(Routes me) {
		//平台主页
		//登陆主页
		me.add("/", IndexController.class);
		me.add("/admin", AdminController.class, "/admin/index");
		me.add("/admin/menu", MenuController.class, "/admin/menu");
		me.add("/admin/user", UserController.class, "/admin/user");
		me.add("/admin/message", MessageController.class, "/admin/message");
		me.add("/admin/tools", ToolsController.class, "/admin/tools");
		
	}
	
	public void configPlugin(Plugins me) {
		//C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		//ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		
		//me.add(c3p0Plugin);
		//me.add(arp);
	}
	
	public void configInterceptor(Interceptors me) {
		me.add(new FreemarkerSessionInViewInterceptor());
		me.add(new AuthInterceptor());
		
	}
	
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("contextPath"));//设置上下文路径  防止样式丢失
		//me.add(new CharacterHandler());//设置request  response的编码
	}
	
	public static void main(String[] args) {
		JFinal.start("webapp", 8081, "/", 5);
	}
}
