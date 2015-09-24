package com.jfinal.aceadmin.utils;

import java.util.HashSet;
import java.util.Set;

public class Commons {
	public final static String UTF8 = "UTF-8";
    /**
     * 与接口配置信息中的 token 要一致，这里赋予什么值，在接口配置信息中的Token就要填写什么值，
     * 两边保持一致即可，建议用项目名称、公司名称缩写等，我在这里用的是项目名称weixinface
     */
    public static String token = "leyogame";
	/**
	 * 定义配置文件的位置，如果是本地开发，可以拷贝文件，到Eclipse安装目录下
	 */
	public final static String Env_Config_FileName = "appcfg/env.cfg";
	public final static String Username_Admin = "admin";
	public final static String Key_Logined_Session = "colee_logined_session_id";
	public final static String Key_Encrypt = "cOlEe_Encrypt_key_@#$#%$#^%$^$DFG%&^*W@DF$";
	public final static Integer Status_Ok = 1;
	public final static Integer Status_Fail = 0;
	public final static Integer Status_Login_Ok = 1;
	public final static Integer Status_Login_Fail = 0;
	public final static Integer Session_MaxInactiveInterval = 8 * 3600;	//8 hour
	public final static Set<String> Not_Check_Perm_URI = new HashSet<String>();
	public final static Set<String> Not_Check_Session_URI = new HashSet<String>();
	
	
	static{
		Not_Check_Perm_URI.add("/login");
		Not_Check_Perm_URI.add("/logout");
		Not_Check_Perm_URI.add("/admin/noPerm");
		Not_Check_Perm_URI.add("/showchangepassword");
		Not_Check_Perm_URI.add("/changepassword");
		
		Not_Check_Session_URI.add("/login");
		Not_Check_Session_URI.add("/logout");
		Not_Check_Session_URI.add("/sign");
		Not_Check_Session_URI.add("/menu");
		
	}
	
	
	
}
