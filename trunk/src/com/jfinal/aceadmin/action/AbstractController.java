package com.jfinal.aceadmin.action;

import java.util.Calendar;

import com.jfinal.aceadmin.bean.Session;
import com.jfinal.aceadmin.utils.Commons;
import com.jfinal.aceadmin.utils.Utils;
import com.jfinal.core.Controller;
import com.tsc9526.monalisa.core.query.dao.Model;
import com.tsc9526.monalisa.core.tools.ClassHelper.FGS;

/**
 * 抽象一个公共的方法<br>
 * <font color=red>使用与已经登录的类，如果非登录的类请勿使用。如忘记密码、登录Controller勿用</font>
 * @author zhangweican
 *
 */
public abstract class AbstractController<T extends Model<T>> extends Controller {
	public T model = null;
	
	public AbstractController(T model){
		this.model = model;
	}
	
	/**
	 * 定义增删改查的总入口方法
	 */
	public final void curd(){
		String oper = getPara("oper");
		if("add".equals(oper)){
			Utils.requestToBean(model, getRequest());
			setCreateByCreateTime();
			save();
		}
		else if("edit".equals(oper)){
			Utils.requestToBean(model, getRequest());
			setUpdateByUpdateTime();
			edit();
		}
		else if("del".equals(oper)){
			del();
		}
		list();
	}
	/**
	 * 定义首页视图
	 */
	public void index(){
		render("index.html");
	}
	
	//=============================差异抽象处理==============================
	public abstract void list();
	public abstract void save();
	public abstract void edit();
	public abstract void del();
	
	
	//=================================扩展方法============================

	
	//================================tools=================================
	/**
	 * 获取登录的会话信息
	 */
	public Session getLoginedSession(){
		Object o = getRequest().getSession().getAttribute(Commons.Key_Logined_Session);
		if(o != null){
			return (Session) o;
		}
		return new Session();
	}
	protected void setCreateByCreateTime(){
		if(model == null){
			return;
		}
		for(FGS fgs : model.fields()){
			if("createBy".equals(fgs.getFieldName())){
				fgs.setObject(model, getLoginedSession().getUserId());
			}
			else if("createTime".equals(fgs.getFieldName())){
				fgs.setObject(model, Calendar.getInstance().getTime());
			}
		}
	}
	protected void setUpdateByUpdateTime(){
		if(model == null){
			return;
		}
		for(FGS fgs : model.fields()){
			if("updateBy".equals(fgs.getFieldName())){
				fgs.setObject(model, getLoginedSession().getUserId());
			}
			else if("updateTime".equals(fgs.getFieldName())){
				fgs.setObject(model, Calendar.getInstance().getTime());
			}
		}
	}
}
